package org.infinispan.springboot.repository;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.springboot.cache.RemoteCacheManagerFactory;
import org.infinispan.springboot.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This is the cache repository that is used to interact with the data. Any class will have to use this repository class to interact with data.
 */
@Component
public class ProductCacheRepository {

	private static final Logger logger =  LoggerFactory.getLogger(org.infinispan.springboot.repository.ProductCacheRepository.class);
	private static final String CACHE_NAME = "product";

	private RemoteCache<Long, Product> remoteCache;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	public ProductCacheRepository(RemoteCacheManagerFactory rcm) throws IOException {
	    List<Class> classes = new ArrayList<Class>();
        classes.add(Product.class);

        rcm.setProtobufConfig("product", classes);
		this.remoteCache = rcm.getCache(CACHE_NAME);
		//This way you can bind the listener you created to the cache.
		this.remoteCache.addClientListener(new ProductCacheListener());
	}

	public Product find(Long itemId){
		return remoteCache.get(itemId);
	}

	public Product insert(Product product){
		return remoteCache.put(product.getItemId(), product);
	}

	public void delete(Long itemId){
		remoteCache.remove(itemId);
	}

    public Product insertWithTTL(Product product, Long ttl){
        return remoteCache.put(product.getItemId(), product, ttl, TimeUnit.SECONDS);
    }

	/**
	 * A nested listener class created for listening cache entry creations. When a cache entry is created
	 * same object should be saved in db. So we listen the cache creation and once it is successfull we save the data to db.
	 */
	@ClientListener
	public class ProductCacheListener {

		@ClientCacheEntryCreated
		public void cacheEntryCreated(ClientCacheEntryCreatedEvent<Long> event) {
			logger.info("Cache Entry created with key " + event.getKey());
			Product product = find(event.getKey());
			productRepository.save(product);
		}
	}
}

package org.infinispan.springboot.service;

import org.infinispan.springboot.model.Product;
import org.infinispan.springboot.repository.ProductCacheRepository;
import org.infinispan.springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**ProductRepository
 * This is the service class for product. Both repositories ProductCacheRepository and
 * ProductRepository used here to manage crud operations on both cache and db.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductCacheRepository productCacheRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * First cache repository is checked whether there is any data with the id
     * If not then db repository is checked. If there is data it is put in the cache
     * and returned. If there is no data, null will return.
     *
     * If cacheOnly=true operation only will be done on cache repository.
     *
     * @param itemId
     * @param cacheOnly
     * @return
     */
    public Product find(Long itemId, boolean cacheOnly) {
        Product product = productCacheRepository.find(itemId);
        if(!cacheOnly) {
            if (null == product) {
                product = productRepository.findOne(itemId);
                if(null != product){
                    productCacheRepository.insert(product);
                }
            }
        }
        return product;
    }

    /**
     * Data is put in the cache repository. Since there is a listener on the "product" cache,
     * no need to put the data in the db here again. Please check the ProductCacheRepository class.
     * @param product
     * @return
     */
    public Product insert(Product product) {
        return productCacheRepository.insert(product);
    }

    /**
     *
     * First deletes the db repository, after that deletes the cache repository.
     *
     * TODO: Has to be implemented like insert, with listeners.
     *
     * If cacheOnly=true operation only will be done on cache repository.
     *
     * @param itemId
     * @param cacheOnly
     */
    public void delete(Long itemId, boolean cacheOnly) {
        if(!cacheOnly){
            productRepository.delete(itemId);
        }
        productCacheRepository.delete(itemId);
    }

    /**
     * Inserts data only into cache, with a ttl.
     *
     *  TODO: Can be implemented to insert the data to db with listeners. TTL won't affect the db data.
     *
     * @param product
     * @param ttl
     * @return
     */
    public Product insertWithTTL(Product product, Long ttl) {
        Product savedProduct = productRepository.save(product);
        return productCacheRepository.insertWithTTL(savedProduct, ttl);
    }
}

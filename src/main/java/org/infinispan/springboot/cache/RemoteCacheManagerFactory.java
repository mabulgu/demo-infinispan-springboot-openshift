package org.infinispan.springboot.cache;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.springboot.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * This is the cache manager factory that creates a cache manager
 */
@Component
public class RemoteCacheManagerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteCacheManagerFactory.class);
    private static final String PROTOBUF_PACKAGE_NAME = "infinispan.remotecache";

	private final RemoteCacheManager cacheManager;

	@Autowired
	public RemoteCacheManagerFactory(RemoteCacheManager cacheManager) throws IOException {
		this.cacheManager = cacheManager;
    }

	public RemoteCache<Long, Product> getCache(String cacheName){
	    return cacheManager.getCache(cacheName);
    }

    public void setProtobufConfig(String name, List<Class> classes) throws IOException {
        SerializationContext serCtx =
                ProtoStreamMarshaller.getSerializationContext(this.cacheManager);
        ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
        protoSchemaBuilder
                .fileName(name.concat(".proto"))
                .packageName(PROTOBUF_PACKAGE_NAME);
        for (Class oneClass : classes) {
            protoSchemaBuilder = protoSchemaBuilder.addClass(oneClass);
        }
        protoSchemaBuilder.build(serCtx);
    }
}

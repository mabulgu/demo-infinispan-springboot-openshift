package org.infinispan.springboot.service;

import org.infinispan.springboot.model.Product;

public interface ProductService {

    Product find(Long itemId, boolean cacheOnly);

    Product insert(Product product);

    void delete(Long itemId, boolean cacheOnly);

    Product insertWithTTL(Product product, Long ttl);
}
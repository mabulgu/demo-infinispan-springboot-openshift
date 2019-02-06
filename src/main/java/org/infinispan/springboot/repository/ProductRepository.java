package org.infinispan.springboot.repository;

import org.infinispan.springboot.model.Product;
import org.springframework.data.repository.CrudRepository;


/**
 * A spring data crud repository interface. Spring data automatically creates the implementation for crud operations.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

}
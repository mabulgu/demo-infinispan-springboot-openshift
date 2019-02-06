package org.infinispan.springboot.controller;

import org.infinispan.springboot.model.Product;
import org.infinispan.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * A simple controller class that holds the find, insert, delete, insertttl methods for crud operations on product.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * the getAll method retrieves all food items in the database. This is mapped to hte GET rest action
     *
     * @return A List() of Reservation items
     **/
    @RequestMapping(method = RequestMethod.GET, path = "/find")
    public Product find(@RequestParam("itemId") Long itemId, @RequestParam("cacheOnly") boolean cacheOnly) throws IOException {
        return productService.find(itemId, cacheOnly);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/insert")
    public Product insert(@RequestBody Product product) throws IOException {
        return productService.insert(product);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete")
    public void delete(@RequestParam("itemId") Long itemId, @RequestParam("cacheOnly") boolean cacheOnly) throws IOException {
         productService.delete(itemId, cacheOnly);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/insertttl")
    public Product insertWithTTL(@RequestParam("ttl") Long ttl, @RequestBody Product product) throws IOException {
        return productService.insertWithTTL(product, ttl);
    }
}

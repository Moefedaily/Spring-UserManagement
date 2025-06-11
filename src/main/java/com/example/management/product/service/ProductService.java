package com.example.management.product.service;

import com.example.management.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    boolean productExists(Long id);
    boolean productNameExists(String name);
    List<Product> findProductsByName(String name);
    List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice);
    List<Product> getBundles();
    List<Product> getSimpleProducts();
    Product duplicateProduct(Long id);
    Product createBundle(String name, List<Long> sourceIds);
    boolean hasCircularDependency(Long productId, Long sourceId);
    void validateBundleCreation(List<Long> sourceIds);
    long getProductCount();
}
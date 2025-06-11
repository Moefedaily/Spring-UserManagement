package com.example.management.product.service;

import com.example.management.product.model.Product;
import com.example.management.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean productNameExists(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> findProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> getBundles() {
        return productRepository.findBundles();
    }

    @Override
    public List<Product> getSimpleProducts() {
        return productRepository.findSimpleProducts();
    }

    @Override
    public Product duplicateProduct(Long id) {
        Optional<Product> originalProduct = productRepository.findById(id);

        if (originalProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + id);
        }

        Product original = originalProduct.get();

        Product duplicate = new Product();
        duplicate.setName(original.getName() + " (copy)");
        duplicate.setPrice(original.getPrice());
        if (original.isBundle()) {
            duplicate.setSources(original.getSources());
        }

        return productRepository.save(duplicate);
    }

    @Override
    public Product createBundle(String name, List<Long> sourceIds) {
        if (sourceIds == null || sourceIds.isEmpty()) {
            throw new IllegalArgumentException("Source products are required for bundle creation");
        }

        validateBundleCreation(sourceIds);

        List<Product> sourceProducts = sourceIds.stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Source product not found with ID: " + id)))
                .collect(Collectors.toList());

        double totalPrice = sourceProducts.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        if (name == null || name.trim().isEmpty()) {
            name = sourceProducts.stream()
                    .map(Product::getName)
                    .collect(Collectors.joining(" + "));
        }

        Product bundle = new Product();
        bundle.setName(name);
        bundle.setPrice(totalPrice);
        bundle.setSources(sourceProducts);

        return productRepository.save(bundle);
    }

    @Override
    public boolean hasCircularDependency(Long productId, Long sourceId) {
        return productRepository.hasCircularDependency(productId, sourceId);
    }

    @Override
    public void validateBundleCreation(List<Long> sourceIds) {
        for (Long sourceId : sourceIds) {
            if (!productRepository.existsById(sourceId)) {
                throw new RuntimeException("Source product not found with ID: " + sourceId);
            }
        }

        long uniqueSourceIds = sourceIds.stream().distinct().count();
        if (uniqueSourceIds != sourceIds.size()) {
            throw new IllegalArgumentException("Duplicate source products are not allowed in bundle");
        }

    }

    @Override
    public long getProductCount() {
        return productRepository.count();
    }
}
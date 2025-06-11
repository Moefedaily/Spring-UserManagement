package com.example.management.product.controller;

import com.example.management.product.dto.CreateBundleRequest;
import com.example.management.product.dto.ProductDto;
import com.example.management.product.mapper.ProductMapper;
import com.example.management.product.model.Product;
import com.example.management.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = ProductMapper.toDtoList(products);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById( @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            ProductDto productDto = ProductMapper.toDto(product.get());
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (productService.productNameExists(productDto.getName())) {
            return ResponseEntity.badRequest().body("Product name already exists: " + productDto.getName());
        }

        try {
            Product product = ProductMapper.toEntityForCreation(productDto);
            Product savedProduct = productService.saveProduct(product);
            ProductDto responseDto = ProductMapper.toDto(savedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating product: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        if (!productService.productExists(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            productDto.setId(id);
            Product product = ProductMapper.toEntity(productDto);
            Product updatedProduct = productService.saveProduct(product);
            ProductDto responseDto = ProductMapper.toDto(updatedProduct);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (!productService.productExists(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<?> duplicateProduct(
           @PathVariable Long id) {
        if (!productService.productExists(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Product duplicatedProduct = productService.duplicateProduct(id);
            ProductDto responseDto = ProductMapper.toDto(duplicatedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error duplicating product: " + e.getMessage());
        }
    }

    @PostMapping("/bundle")
    public ResponseEntity<?> createBundle(@Valid @RequestBody CreateBundleRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            productService.validateBundleCreation(request.getSourceIds());
            Product bundle = productService.createBundle(request.getName(), request.getSourceIds());
            ProductDto responseDto = ProductMapper.toDto(bundle);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating bundle: " + e.getMessage());
        }
    }

    @GetMapping("/bundles")
    public ResponseEntity<List<ProductDto>> getBundles() {
        List<Product> bundles = productService.getBundles();
        List<ProductDto> bundleDtos = ProductMapper.toDtoList(bundles);
        return ResponseEntity.ok(bundleDtos);
    }

    @GetMapping("/simple")
    public ResponseEntity<List<ProductDto>> getSimpleProducts() {
        List<Product> simpleProducts = productService.getSimpleProducts();
        List<ProductDto> simpleDtos = ProductMapper.toDtoList(simpleProducts);
        return ResponseEntity.ok(simpleDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.findProductsByName(name);
        List<ProductDto> productDtos = ProductMapper.toDtoList(products);
        return ResponseEntity.ok(productDtos);
    }
}
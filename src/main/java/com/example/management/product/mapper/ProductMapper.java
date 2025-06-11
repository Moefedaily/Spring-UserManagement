package com.example.management.product.mapper;

import com.example.management.product.dto.ProductDto;
import com.example.management.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        List<Long> sourceIds = null;
        List<String> sourceNames = null;

        if (product.getSources() != null && !product.getSources().isEmpty()) {
            sourceIds = product.getSources().stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());

            sourceNames = product.getSources().stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                sourceIds,
                sourceNames
        );
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product(
                dto.getId(),
                dto.getName(),
                dto.getPrice()
        );

        return product;
    }

    public static Product toEntityForCreation(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        return new Product(
                dto.getName(),
                dto.getPrice()
        );
    }

    public static List<ProductDto> toDtoList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<Product> toEntityList(List<ProductDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static Product toBundleEntity(ProductDto dto, List<Product> sourceProducts) {
        if (dto == null) {
            return null;
        }

        Product product = new Product(
                dto.getName(),
                dto.getPrice(),
                sourceProducts
        );

        return product;
    }

    public static ProductDto toSimpleDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
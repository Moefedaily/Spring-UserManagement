package com.example.management.product.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
    private Double price;

    private List<Long> sourceIds;
    private List<String> sourceNames;
    private boolean isBundle;
    private int sourcesCount;

    public ProductDto() {
    }

    public ProductDto(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductDto(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public ProductDto(Long id, String name, Double price, List<Long> sourceIds, List<String> sourceNames) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sourceIds = sourceIds;
        this.sourceNames = sourceNames;
        this.isBundle = sourceIds != null && !sourceIds.isEmpty();
        this.sourcesCount = sourceIds != null ? sourceIds.size() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Long> getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
        this.isBundle = sourceIds != null && !sourceIds.isEmpty();
        this.sourcesCount = sourceIds != null ? sourceIds.size() : 0;
    }

    public List<String> getSourceNames() {
        return sourceNames;
    }

    public void setSourceNames(List<String> sourceNames) {
        this.sourceNames = sourceNames;
    }

    public boolean isBundle() {
        return isBundle;
    }

    public void setBundle(boolean bundle) {
        isBundle = bundle;
    }

    public int getSourcesCount() {
        return sourcesCount;
    }

    public void setSourcesCount(int sourcesCount) {
        this.sourcesCount = sourcesCount;
    }
}
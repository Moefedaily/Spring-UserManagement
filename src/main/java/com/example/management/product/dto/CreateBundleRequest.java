package com.example.management.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateBundleRequest {

    @Size(max = 100, message = "Bundle name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Source product IDs are required")
    @NotEmpty(message = "At least one source product is required")
    private List<Long> sourceIds;

    public CreateBundleRequest() {
    }

    public CreateBundleRequest(String name, List<Long> sourceIds) {
        this.name = name;
        this.sourceIds = sourceIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
    }

    @Override
    public String toString() {
        return "CreateBundleRequest{" +
                "name='" + name + '\'' +
                ", sourceIds=" + sourceIds +
                '}';
    }
}
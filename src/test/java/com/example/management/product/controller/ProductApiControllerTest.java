package com.example.management.product.controller;

import com.example.management.product.model.Product;
import com.example.management.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", 999.99),
                new Product(2L, "Mouse", 29.99)
        );
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(999.99))
                .andExpect(jsonPath("$[1].name").value("Mouse"))
                .andExpect(jsonPath("$[1].price").value(29.99));

        verify(productService).getAllProducts();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        Product product = new Product(1L, "Laptop", 999.99);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService).getProductById(1L);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturn404() throws Exception {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService).getProductById(999L);
    }

    @Test
    void createProduct_WithValidData_ShouldCreateProduct() throws Exception {
        Product savedProduct = new Product(1L, "New Laptop", 1299.99);
        when(productService.productNameExists(anyString())).thenReturn(false);
        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        String productJson = """
            {
                "name": "New Laptop",
                "price": 1299.99
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99));

        verify(productService).productNameExists("New Laptop");
        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturn400() throws Exception {
        String invalidProductJson = """
            {
                "name": "",
                "price": -10.0
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());

        verify(productService, never()).saveProduct(any(Product.class));
    }

    @Test
    void createProduct_WithDuplicateName_ShouldReturn400() throws Exception {

        when(productService.productNameExists("Existing Product")).thenReturn(true);

        String productJson = """
            {
                "name": "Existing Product",
                "price": 199.99
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Product name already exists")));

        verify(productService).productNameExists("Existing Product");
        verify(productService, never()).saveProduct(any(Product.class));
    }

    @Test
    void updateProduct_WithValidData_ShouldUpdateProduct() throws Exception {
        Product updatedProduct = new Product(1L, "Updated Laptop", 1499.99);
        when(productService.productExists(1L)).thenReturn(true);
        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);

        String productJson = """
            {
                "name": "Updated Laptop",
                "price": 1499.99
            }
            """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1499.99));

        verify(productService).productExists(1L);
        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldReturn404() throws Exception {
        when(productService.productExists(999L)).thenReturn(false);

        String productJson = """
            {
                "name": "Updated Product",
                "price": 199.99
            }
            """;

        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isNotFound());

        verify(productService).productExists(999L);
        verify(productService, never()).saveProduct(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() throws Exception {
        when(productService.productExists(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).productExists(1L);
        verify(productService).deleteProduct(1L);
    }

    @Test
    void deleteProduct_WhenProductNotExists_ShouldReturn404() throws Exception {
        when(productService.productExists(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService).productExists(999L);
        verify(productService, never()).deleteProduct(anyLong());
    }

    @Test
    void duplicateProduct_WhenProductExists_ShouldDuplicateProduct() throws Exception {
        Product duplicatedProduct = new Product(2L, "Laptop (copy)", 999.99);
        when(productService.productExists(1L)).thenReturn(true);
        when(productService.duplicateProduct(1L)).thenReturn(duplicatedProduct);

        mockMvc.perform(post("/api/products/1/duplicate"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Laptop (copy)"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService).productExists(1L);
        verify(productService).duplicateProduct(1L);
    }

    @Test
    void duplicateProduct_WhenProductNotExists_ShouldReturn404() throws Exception {
        when(productService.productExists(999L)).thenReturn(false);

        mockMvc.perform(post("/api/products/999/duplicate"))
                .andExpect(status().isNotFound());

        verify(productService).productExists(999L);
        verify(productService, never()).duplicateProduct(anyLong());
    }

    @Test
    void createBundle_WithValidData_ShouldCreateBundle() throws Exception {
        Product bundle = new Product(3L, "Gaming Bundle", 1999.99);
        when(productService.createBundle(eq("Gaming Bundle"), eq(Arrays.asList(1L, 2L))))
                .thenReturn(bundle);

        String bundleJson = """
            {
                "name": "Gaming Bundle",
                "sourceIds": [1, 2]
            }
            """;

        mockMvc.perform(post("/api/products/bundle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bundleJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Gaming Bundle"))
                .andExpect(jsonPath("$.price").value(1999.99));

        verify(productService).validateBundleCreation(Arrays.asList(1L, 2L));
        verify(productService).createBundle("Gaming Bundle", Arrays.asList(1L, 2L));
    }

    @Test
    void createBundle_WithEmptySourceIds_ShouldReturn400() throws Exception {
        String bundleJson = """
            {
                "name": "Empty Bundle",
                "sourceIds": []
            }
            """;

        mockMvc.perform(post("/api/products/bundle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bundleJson))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createBundle(anyString(), anyList());
    }

    @Test
    void getBundles_ShouldReturnOnlyBundles() throws Exception {
        List<Product> bundles = Arrays.asList(
                new Product(3L, "Gaming Bundle", 1999.99),
                new Product(4L, "Office Bundle", 899.99)
        );
        when(productService.getBundles()).thenReturn(bundles);

        mockMvc.perform(get("/api/products/bundles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gaming Bundle"))
                .andExpect(jsonPath("$[1].name").value("Office Bundle"));

        verify(productService).getBundles();
    }

    @Test
    void getSimpleProducts_ShouldReturnOnlySimpleProducts() throws Exception {
        List<Product> simpleProducts = Arrays.asList(
                new Product(1L, "Laptop", 999.99),
                new Product(2L, "Mouse", 29.99)
        );
        when(productService.getSimpleProducts()).thenReturn(simpleProducts);
        mockMvc.perform(get("/api/products/simple"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));

        verify(productService).getSimpleProducts();
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        List<Product> searchResults = Arrays.asList(
                new Product(1L, "Gaming Laptop", 1999.99)
        );
        when(productService.findProductsByName("laptop")).thenReturn(searchResults);

        mockMvc.perform(get("/api/products/search")
                        .param("name", "laptop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gaming Laptop"));

        verify(productService).findProductsByName("laptop");
    }
}
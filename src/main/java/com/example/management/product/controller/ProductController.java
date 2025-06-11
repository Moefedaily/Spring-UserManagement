package com.example.management.product.controller;

import com.example.management.product.dto.ProductDto;
import com.example.management.product.mapper.ProductMapper;
import com.example.management.product.model.Product;
import com.example.management.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = ProductMapper.toDtoList(products);
        long productCount = productService.getProductCount();
        long bundleCount = productService.getBundles().size();

        model.addAttribute("products", productDtos);
        model.addAttribute("productCount", productCount);
        model.addAttribute("bundleCount", bundleCount);

        return "products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "products/add";
        }

        if (productService.productNameExists(productDto.getName())) {
            result.rejectValue("name", "error.product", "Product name already exists: " + productDto.getName());
            return "products/add";
        }

        try {
            Product product = ProductMapper.toEntityForCreation(productDto);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully: " + productDto.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            ProductDto productDto = ProductMapper.toDto(productOptional.get());
            model.addAttribute("productDto", productDto);
            return "products/edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found with ID: " + id);
            return "redirect:/products";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductDto productDto,
                                BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "products/edit";
        }

        try {
            productDto.setId(id);
            Product product = ProductMapper.toEntity(productDto);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully: " + productDto.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isPresent()) {
                productService.deleteProduct(id);
                redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/duplicate/{id}")
    public String duplicateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Product duplicatedProduct = productService.duplicateProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product duplicated successfully: " + duplicatedProduct.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error duplicating product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/bundle")
    public String showBundleForm(Model model) {
        List<Product> simpleProducts = productService.getSimpleProducts();
        List<ProductDto> simpleProductDtos = ProductMapper.toDtoList(simpleProducts);

        model.addAttribute("simpleProducts", simpleProductDtos);
        model.addAttribute("bundleName", "");

        return "products/bundle";
    }

    @PostMapping("/bundle")
    public String createBundle(@RequestParam String bundleName,
                               @RequestParam(required = false) List<Long> sourceIds,
                               RedirectAttributes redirectAttributes) {

        if (bundleName == null || bundleName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bundle name is required");
            return "redirect:/products/bundle";
        }

        if (sourceIds == null || sourceIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "At least one source product must be selected");
            return "redirect:/products/bundle";
        }

        try {
            productService.validateBundleCreation(sourceIds);
            Product bundle = productService.createBundle(bundleName.trim(), sourceIds);
            redirectAttributes.addFlashAttribute("successMessage", "Bundle created successfully: " + bundle.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating bundle: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/bundles")
    public String listBundles(Model model) {
        List<Product> bundles = productService.getBundles();
        List<ProductDto> bundleDtos = ProductMapper.toDtoList(bundles);
        long bundleCount = bundles.size();

        model.addAttribute("products", bundleDtos);
        model.addAttribute("productCount", bundleCount);
        model.addAttribute("bundleCount", bundleCount);
        model.addAttribute("viewType", "bundles");

        return "products/list";
    }

    @GetMapping("/simple")
    public String listSimpleProducts(Model model) {
        List<Product> simpleProducts = productService.getSimpleProducts();
        List<ProductDto> simpleDtos = ProductMapper.toDtoList(simpleProducts);
        long simpleCount = simpleProducts.size();

        model.addAttribute("products", simpleDtos);
        model.addAttribute("productCount", simpleCount);
        model.addAttribute("bundleCount", 0L);
        model.addAttribute("viewType", "simple");

        return "products/list";
    }
}
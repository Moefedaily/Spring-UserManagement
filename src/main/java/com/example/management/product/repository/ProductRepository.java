package com.example.management.product.repository;

import com.example.management.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    @Query("SELECT p FROM Product p WHERE SIZE(p.sources) > 0")
    List<Product> findBundles();

    @Query("SELECT p FROM Product p WHERE SIZE(p.sources) = 0")
    List<Product> findSimpleProducts();
    boolean existsByName(String name);
    List<Product> findByPrice(Double price);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Product p JOIN p.sources s WHERE p.id = :sourceId AND s.id = :productId")
    boolean hasCircularDependency(@Param("productId") Long productId, @Param("sourceId") Long sourceId);

}
package com.ecom.product_api.repository;

import com.ecom.product_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepo extends JpaRepository<Product, String> {
    @Query("SELECT p FROM product p WHERE " +
            "LOWER(p.productId) LIKE %:searchText% OR " +
            "LOWER(p.description) LIKE %:searchText% OR " +
            "LOWER(p.unitPrice) LIKE %:searchText%")
    Page<Product> searchProducts(@Param("searchText") String searchText, Pageable pageable);
}

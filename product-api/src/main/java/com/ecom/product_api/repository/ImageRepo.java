package com.ecom.product_api.repository;

import com.ecom.product_api.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Images, String> {
}

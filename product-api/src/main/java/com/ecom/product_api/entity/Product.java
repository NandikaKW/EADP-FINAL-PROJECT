package com.ecom.product_api.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Set;

@Entity(name="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(name = "product_id",length = 80,nullable = false)
    private String  productId;

    @Column(name = "description",length = 255)
    private String description;

    @Column(name = "unit_price",precision = 2)
    private String unitPrice;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Images> images;



}

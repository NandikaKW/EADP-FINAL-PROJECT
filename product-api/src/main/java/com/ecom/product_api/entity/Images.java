package com.ecom.product_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity(name="product_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Images {
    @Id
    private String id;

    @Embedded
    private FileResource fileResource;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

package com.ecom.product_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestProductDto {


    private String productId;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotBlank(message = "Unit price cannot be blank")
    private String unitPrice;

    @NotBlank(message = "Quantity cannot be blank")
    private int quantity;

}

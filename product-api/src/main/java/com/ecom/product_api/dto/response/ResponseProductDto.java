package com.ecom.product_api.dto.response;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProductDto {

    private String productId;

    private String description;

    private String unitPrice;

    private int quantity;

    private List<ResponseImage> images;
}

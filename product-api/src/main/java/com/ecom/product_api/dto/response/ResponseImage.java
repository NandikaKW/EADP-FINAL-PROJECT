package com.ecom.product_api.dto.response;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseImage {
    private String id;
    private String resourceUrl;
}

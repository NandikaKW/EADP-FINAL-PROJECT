package com.ecom.product_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Blob;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonFileSavedBinaryDataDto {
    private Blob hash;
    private Blob filename;
    private Blob resourceUrl;
    private Blob directory;
}

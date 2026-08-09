package com.ecom.product_api.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Blob;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class FileResource {

    @Lob
    private Blob hash;

    @Lob
    private Blob fileName;

    @Lob
    private Blob resourceUrl;

    @Lob
    private Blob directory;
}

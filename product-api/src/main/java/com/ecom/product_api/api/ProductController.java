package com.ecom.product_api.api;

import com.ecom.product_api.dto.request.RequestProductDto;
import com.ecom.product_api.dto.response.ResponseProductDto;
import com.ecom.product_api.dto.response.paginate.ResponseProductPaginate;
import com.ecom.product_api.service.ProductService;
import com.ecom.product_api.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/products-service/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    // Create Product
    @PostMapping public ResponseEntity<StandardResponse> createProduct(
            @Valid @RequestParam("data") String data,
            @Valid @RequestParam("image") MultipartFile file)  {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestProductDto requestProductDto = objectMapper.readValue(data, RequestProductDto.class);
        productService.createProduct(requestProductDto, file); return ResponseEntity.status(HttpStatus.CREATED)
                .body(StandardResponse.builder() .code(HttpStatus.CREATED.value())
                        .message("Product created successfully") .build()); }

    // Update Product
    @PutMapping("/update/{productId}")
    public ResponseEntity<StandardResponse> updateProduct(
            @RequestBody RequestProductDto dto,
            @PathVariable String productId) {

        productService.updateProduct(dto, productId);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("Product updated successfully")
                        .build()
        );
    }


    // Delete Product
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<StandardResponse> deleteProduct(
            @PathVariable String productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("Product deleted successfully")
                        .build()
        );
    }


    // Find Product By ID
    @GetMapping("/find-by-id/{productId}")
    public ResponseEntity<StandardResponse> findProductById(
            @PathVariable String productId) {

        ResponseProductDto response =
                productService.findProductById(productId);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .data(response)
                        .message("Product found successfully")
                        .build()
        );
    }


    // Search Products with Pagination
    @GetMapping("/search-products")
    public ResponseEntity<StandardResponse> searchAllProduct(
            @RequestParam(required = false, defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ResponseProductPaginate response =
                productService.searchAllProduct(searchText, page, size);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .data(response)
                        .message("Products retrieved successfully")
                        .build()
        );
    }


    // Update Product Image
    @PutMapping("/update-image/{imageId}")
    public ResponseEntity<StandardResponse> updateImage(
            @PathVariable String imageId,
            @RequestPart("file") MultipartFile file) {

        productService.updateImage(imageId, file);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("Product image updated successfully")
                        .build()
        );
    }


    // Delete Product Image
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<StandardResponse> deleteImage(
            @PathVariable String imageId) {

        productService.deleteImage(imageId);

        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("Product image deleted successfully")
                        .build()
        );
    }

}

package com.ecom.product_api.service;

import com.ecom.product_api.dto.request.RequestProductDto;
import com.ecom.product_api.dto.response.ResponseProductDto;
import com.ecom.product_api.dto.response.paginate.ResponseProductPaginate;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    public void createProduct(RequestProductDto dto, MultipartFile file);
    public void updateProduct(RequestProductDto dto, String productId);
    public void deleteProduct(String productId);
    public ResponseProductDto findProductById(String productId);
    public ResponseProductPaginate searchAllProduct(String searchText, int page, int size);
    public void updateImage(String imageId, MultipartFile file);
    public void deleteImage(String imageId);
}

package com.ecom.product_api.service.Impl;

import com.ecom.product_api.dto.CommonFileSavedBinaryDataDto;
import com.ecom.product_api.dto.request.RequestProductDto;
import com.ecom.product_api.dto.response.ResponseImage;
import com.ecom.product_api.dto.response.ResponseProductDto;
import com.ecom.product_api.dto.response.paginate.ResponseProductPaginate;
import com.ecom.product_api.entity.FileResource;
import com.ecom.product_api.entity.Images;
import com.ecom.product_api.entity.Product;
import com.ecom.product_api.exception.EntryNotFoundException;
import com.ecom.product_api.repository.ImageRepo;
import com.ecom.product_api.repository.ProductRepo;
import com.ecom.product_api.service.FileService;
import com.ecom.product_api.service.ProductService;
import com.ecom.product_api.util.FileDataExtractor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final FileService fileService;
    private final ImageRepo imageRepo;
    private FileDataExtractor fileDataExtractor;
    private final ProductRepo productRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${bucketName}")
    private String bucketName;

    @Override
    public void createProduct(RequestProductDto dto, MultipartFile file) {
        CommonFileSavedBinaryDataDto resource = null;
        try {
            // Upload image
            resource = fileService.createFile(
                    file,
                    "products/images/srilanka/",
                    bucketName
            );

            // Create FileResource
            FileResource fileResource = FileResource.builder()
                    .hash(resource.getHash())
                    .fileName(resource.getFilename())
                    .resourceUrl(resource.getResourceUrl())
                    .directory(resource.getDirectory())
                    .build();

            // Create Images set
            Set<Images> images = new HashSet<>();
            images.add(
                    Images.builder()
                            .id(UUID.randomUUID().toString())
                            .fileResource(fileResource)
                            .build()
            );

            // Create Product
            Product product = Product.builder()
                    .productId(UUID.randomUUID().toString())
                    .quantity(dto.getQuantity())
                    .description(dto.getDescription())
                    .unitPrice(dto.getUnitPrice())
                    .images(images)
                    .build();

            // Save product
            productRepo.save(product);

        } catch (Exception e) {
            // Rollback: delete uploaded file if product creation fails
            if (resource != null) {
                String directory = fileDataExtractor.blobToString(resource.getDirectory());
                String fileName = fileDataExtractor.blobToString(resource.getFilename());
                fileService.deleteFile(
                        directory,
                        fileName,
                        bucketName
                );
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProduct(RequestProductDto dto, String productId) {
      Optional<Product> selectedProductData =productRepo.findById(productId);
      if (selectedProductData.isEmpty()) {
        throw new EntryNotFoundException("Product not found");
      }
      Product product = selectedProductData.get();
      product.setQuantity(dto.getQuantity());
      product.setDescription(dto.getDescription());
      product.setUnitPrice(dto.getUnitPrice());
      productRepo.save(product);

    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found"));

        // Delete image files from S3
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (Images image : product.getImages()) {
                String directory = fileDataExtractor.blobToString(image.getFileResource().getDirectory());
                String fileName = fileDataExtractor.blobToString(image.getFileResource().getFileName());
                fileService.deleteFile(
                        directory,
                        fileName,
                        bucketName
                );
            }
        }

        // Delete product
        productRepo.delete(product);



    }

    @Override
    public ResponseProductDto findProductById(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + productId));
        return convertToResponseDto(product);
    }


    @Override
    public ResponseProductPaginate searchAllProduct(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (searchText != null && !searchText.trim().isEmpty()) {
            productPage = productRepo.searchProducts(
                    searchText.trim().toLowerCase(),
                    pageable
            );
        } else {
            productPage = productRepo.findAll(pageable);
        }

        List<ResponseProductDto> responseList =
                productPage.getContent().stream()
                        .map(this::convertToResponseDto)
                        .toList();

        return ResponseProductPaginate.builder()
                .count(productPage.getTotalElements())
                .dataList(responseList)
                .build();
    }

    @Override
    public void updateImage(String imageId, MultipartFile file) {
        CommonFileSavedBinaryDataDto resource = null;
        try {
            // Find image using ImageRepo
            Images image = imageRepo.findById(imageId)
                    .orElseThrow(() -> new EntryNotFoundException("Image not found with id: " + imageId));

            Product product = image.getProduct();
            if (product == null) {
                throw new EntryNotFoundException("Product not found for image");
            }

            String directory = "products/images/srilanka/";

            // Delete old file from S3 first
            String oldDirectory = fileDataExtractor.blobToString(image.getFileResource().getDirectory());
            String oldFileName = fileDataExtractor.blobToString(image.getFileResource().getFileName());
            fileService.deleteFile(oldDirectory, oldFileName, bucketName);

            // Upload new file
            resource = fileService.updateFile(
                    file,
                    directory,
                    bucketName
            );

            // Create new FileResource with Blob data
            FileResource fileResource = FileResource.builder()
                    .hash(resource.getHash())
                    .fileName(resource.getFilename())
                    .resourceUrl(resource.getResourceUrl())
                    .directory(resource.getDirectory())
                    .build();

            // Update image
            image.setFileResource(fileResource);
            imageRepo.save(image);  // Using ImageRepo to save

        } catch (Exception e) {
            // Rollback: delete uploaded file if update fails
            if (resource != null) {
                String directory = fileDataExtractor.blobToString(resource.getDirectory());
                String fileName = fileDataExtractor.blobToString(resource.getFilename());
                fileService.deleteFile(
                        directory,
                        fileName,
                        bucketName
                );
            }
            throw new RuntimeException("Failed to update image: " + e.getMessage(), e);
        }

    }

    @Override
    public void deleteImage(String imageId) {
        // Find image using ImageRepo
        Images image = imageRepo.findById(imageId)
                .orElseThrow(() -> new EntryNotFoundException("Image not found with id: " + imageId));

        Product product = image.getProduct();
        if (product == null) {
            throw new EntryNotFoundException("Product not found for image");
        }

        // Delete image from S3
        String directory = fileDataExtractor.blobToString(image.getFileResource().getDirectory());
        String fileName = fileDataExtractor.blobToString(image.getFileResource().getFileName());
        fileService.deleteFile(
                directory,
                fileName,
                bucketName
        );

        // Remove relationship
        product.getImages().remove(image);

        // Delete image entity using ImageRepo
        imageRepo.delete(image);  // Using ImageRepo to delete
    }


    private ResponseProductDto convertToResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        List<ResponseImage> imageList = new ArrayList<>();
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (Images image : product.getImages()) {
                // Convert Blob resourceUrl to String
                String resourceUrl = fileDataExtractor.blobToString(image.getFileResource().getResourceUrl());
                imageList.add(ResponseImage.builder()
                        .id(image.getId())
                        .resourceUrl(resourceUrl)
                        .build());
            }
        }

        return ResponseProductDto.builder()
                .productId(product.getProductId())
                .unitPrice(product.getUnitPrice())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .images(imageList)
                .build();
    }


}

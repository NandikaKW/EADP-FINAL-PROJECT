package com.ecom.product_api.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ecom.product_api.dto.CommonFileSavedBinaryDataDto;
import com.ecom.product_api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;

    @Override
    public CommonFileSavedBinaryDataDto createFile(MultipartFile file, String directory, String bucket) throws IOException, SQLException {
        String originalFilename = file.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + originalFilename;

        String fullPath = directory.endsWith("/") ? directory + newFileName : directory + "/" + newFileName;

        PutObjectResult putObjectResult = amazonS3Client.putObject(
                new PutObjectRequest(bucket, fullPath,
                        file.getInputStream(),
                        new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return CommonFileSavedBinaryDataDto.builder()
                .directory(new SerialBlob(directory.getBytes()))
                .filename(new SerialBlob(newFileName.getBytes()))
                .hash(new SerialBlob(putObjectResult.getContentMd5().getBytes()))
                .resourceUrl(new SerialBlob(amazonS3Client.getResourceUrl(bucket, fullPath).getBytes()))
                .build();
    }

    @Override
    public CommonFileSavedBinaryDataDto updateFile(MultipartFile file, String directory, String bucket) {
        try {
            String originalFilename = file.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + originalFilename;
            String fullPath = directory.endsWith("/") ? directory + newFileName : directory + "/" + newFileName;

            PutObjectResult putObjectResult = amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fullPath,
                            file.getInputStream(),
                            new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead)
            );

            return CommonFileSavedBinaryDataDto.builder()
                    .directory(new SerialBlob(directory.getBytes()))
                    .filename(new SerialBlob(newFileName.getBytes()))
                    .hash(new SerialBlob(putObjectResult.getContentMd5().getBytes()))
                    .resourceUrl(new SerialBlob(amazonS3Client.getResourceUrl(bucket, fullPath).getBytes()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String directory, String fileName, String bucket) {
        String fullPath = directory.endsWith("/") ? directory + fileName : directory + "/" + fileName;
        amazonS3Client.deleteObject(bucket, fullPath);
    }

    @Override
    public CommonFileSavedBinaryDataDto getFile(String directory, String bucket) {
        return null;
    }
}

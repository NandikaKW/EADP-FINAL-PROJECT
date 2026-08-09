package com.ecom.product_api.service;


import com.ecom.product_api.dto.CommonFileSavedBinaryDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface FileService {
    CommonFileSavedBinaryDataDto createFile(
            MultipartFile file,
            String directory,
            String bucket
    ) throws IOException, SQLException;

    CommonFileSavedBinaryDataDto updateFile(
            MultipartFile file,
            String directory,
            String bucket
    );

    void deleteFile(
            String directory,
            String fileName,
            String bucket
    );

    CommonFileSavedBinaryDataDto getFile(
            String directory,
            String bucket
    );
}

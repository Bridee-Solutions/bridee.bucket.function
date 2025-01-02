package com.bridee.bucket.function.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.bridee.bucket.function.dto.FileRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class BucketService {

    @Value("${azure.blob-storage.connection-string}")
    private String azureConnectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureContainerName;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init(){
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(azureConnectionString).buildClient();
    }

    public byte[] downloadFile(String filename) {
        byte[] binaries = null;
        if (Objects.isNull(filename)){
            return null;
        }
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(filename);
        try{
            binaries = blobClient.downloadContent().toBytes();
        }catch (Exception e){
            log.error("Failed to download file with error message: {}", e.getMessage());
        }
        return binaries;
    }

    public void uploadFile(FileRequest fileRequest) {

        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(fileRequest.getFileName());
        try {
            MultipartFile multipartFile = fileRequest.getFile();
            blobClient.upload(multipartFile.getInputStream(), multipartFile.getSize(), true);
        } catch (IOException e) {
            log.error("Failed to upload file with error message: {}", e.getMessage());
        }

    }


}

package com.bridee.bucket.function.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.bridee.bucket.function.dto.FileRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

@Service
public class BucketService {

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureContainerName;

    private BlobServiceClient blobServiceClient;

    public BucketService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
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
        }
        return binaries;
    }

    public String uploadFile(FileRequest fileRequest) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(fileRequest.getFileName());
        InputStream fileContent = fromEncodedString(fileRequest.getFile());
        blobClient.upload(fileContent,true);
        return "File uploaded sucessfully";
    }

    private InputStream fromEncodedString(String fileContent){
        byte[] content = Base64.getDecoder().decode(fileContent);
        return new ByteArrayInputStream(content);
    }


}

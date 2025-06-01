package com.bridee.bucket.function.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.bridee.bucket.function.dto.FileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketService {

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureContainerName;

    private final BlobServiceClient blobServiceClient;

    public String downloadFile(String filename) {
        String fileUrl = null;
        if (Objects.isNull(filename)){
            return null;
        }
        try{
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(filename);
            fileUrl = blobClient.getBlobUrl();
        }catch (Exception e){
            log.error("Error while trying to download file: %s, with the following error: %s".formatted(filename, e.getMessage()));
        }
        return fileUrl;
    }

    public void uploadFile(FileRequest fileRequest) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(fileRequest.getFileName());
        InputStream fileContent = fromEncodedString(fileRequest.getFile());
        blobClient.upload(fileContent,true);
    }

    private InputStream fromEncodedString(String fileContent){
        byte[] content = Base64.getDecoder().decode(fileContent);
        return new ByteArrayInputStream(content);
    }


}

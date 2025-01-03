package com.bridee.bucket.function.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.bridee.bucket.function.dto.FileRequest;
import com.microsoft.azure.functions.ExecutionContext;
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

    public byte[] downloadFile(String filename) {
        byte[] binaries = null;
        if (Objects.isNull(filename)){
            return null;
        }
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(azureContainerName).getBlobClient(filename);
        try{
            binaries = blobClient.downloadContent().toBytes();
        }catch (Exception e){
            log.error("Error while trying to download file: %s, with the following error: %s".formatted(filename, e.getMessage()));
        }
        return binaries;
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

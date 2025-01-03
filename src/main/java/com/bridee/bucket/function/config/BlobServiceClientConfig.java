package com.bridee.bucket.function.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlobServiceClientConfig {

    @Value("${azure.blob-storage.connection-string}")
    private String azureConnectionString;

    @Bean
    public BlobServiceClient blobServiceClient(){
        return new BlobServiceClientBuilder().connectionString(azureConnectionString).buildClient();
    }

}

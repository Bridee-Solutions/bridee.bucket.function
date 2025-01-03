package com.bridee.bucket.function.function;

import com.bridee.bucket.function.dto.FileRequest;
import com.bridee.bucket.function.service.BucketService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class BucketFunction {

    private final BucketService bucketService;

    public BucketFunction(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @FunctionName("uploadFile")
    public HttpResponseMessage uploadFile(@HttpTrigger(name = "request", methods = HttpMethod.POST, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<FileRequest> request,
                                          ExecutionContext executionContext){

        if (Objects.isNull(request)){
            throw new IllegalArgumentException("request cannot be null");
        }

        executionContext.getLogger().info("Trying to upload the file with name %s".formatted(request.getBody().getFileName()));
        bucketService.uploadFile(request.getBody());
        executionContext.getLogger().info("File uploaded successfully!");
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .build();
    }

}

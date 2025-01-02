package com.bridee.bucket.function.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {

    private MultipartFile file;
    private String fileName;

}

package com.ecommerce.backend.service.File;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface FileService {
    String uploadImage(String path, MultipartFile file) throws IOException;
}

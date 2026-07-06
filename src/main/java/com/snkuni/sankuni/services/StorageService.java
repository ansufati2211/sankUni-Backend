package com.snkuni.sankuni.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file, String subfolder);

    Resource load(String rutaRelativa);

    void delete(String rutaRelativa);
}

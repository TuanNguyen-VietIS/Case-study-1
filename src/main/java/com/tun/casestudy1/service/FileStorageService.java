package com.tun.casestudy1.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageService {
    void init();

    String save(MultipartFile file) throws IOException;

    Resource load(String filename) throws MalformedURLException;
}

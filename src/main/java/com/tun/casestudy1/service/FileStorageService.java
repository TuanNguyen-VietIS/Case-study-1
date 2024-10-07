package com.tun.casestudy1.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root = Paths.get("./uploads");

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path destinationFile = this.root.resolve(filename).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), destinationFile);

        return destinationFile.getFileName().toString();
    }

    public Resource load(String filename) throws MalformedURLException {
        Path file = root.resolve(filename);
        Resource resource = (Resource) new UrlResource(file.toUri());
        return resource;
    }
}

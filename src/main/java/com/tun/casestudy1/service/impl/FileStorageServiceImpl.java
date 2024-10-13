package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.service.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root = Paths.get("./uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FOLDER_CREATION_FAILED);
        }
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path destinationFile = this.root.resolve(filename).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), destinationFile);

        return destinationFile.getFileName().toString();
    }

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path file = root.resolve(filename);
        Resource resource = (Resource) new UrlResource(file.toUri());
        return resource;
    }
}

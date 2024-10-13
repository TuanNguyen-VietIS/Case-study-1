package com.tun.casestudy1.service;
import com.tun.casestudy1.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceImplTest {
    @Mock
    MultipartFile multipartFile;
    @Mock
    FileStorageServiceImpl fileStorageServiceImpl;
    private final Path root = Paths.get("./uploads");

    @BeforeEach
    void setUp() {
        fileStorageServiceImpl.init();
    }

    @Test
    void testLoad() throws MalformedURLException {
        String filename = "testfile.png";
        Path filePath = root.resolve(filename);
        Resource mockResource = Mockito.mock(UrlResource.class);

        Mockito.when(mockResource.exists()).thenReturn(true);
        Mockito.when(mockResource.isReadable()).thenReturn(true);
        Mockito.when(fileStorageServiceImpl.load(filename)).thenReturn(mockResource);

        Resource result = fileStorageServiceImpl.load(filename);

        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.isReadable());
    }
    @Test
    void testSave() throws IOException {
        String originalFilename = "testfile.txt";
        String generatedFilename = UUID.randomUUID() + "_" + originalFilename;
        Path destinationPath = root.resolve(generatedFilename).normalize().toAbsolutePath();

        Mockito.when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        Mockito.when(multipartFile.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/testfile.txt"));

        String resultFilename = fileStorageServiceImpl.save(multipartFile);

        assertEquals(generatedFilename, resultFilename);
        assertTrue(Files.exists(destinationPath));
    }
}
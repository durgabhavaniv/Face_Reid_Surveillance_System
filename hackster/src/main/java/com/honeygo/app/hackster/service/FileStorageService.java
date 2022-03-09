package com.honeygo.app.hackster.service;

import com.honeygo.app.hackster.config.ApplicationProperties;
import com.honeygo.app.hackster.exception.FileStorageException;
import com.honeygo.app.hackster.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private Path fileStorageLocation;
    private Path fileOutputLocation;
    private String scriptfile;

    @Autowired
    private ApplicationProperties applicationProperties;

    public String storeFile(Path inPath,MultipartFile file) {
        this.fileStorageLocation = Paths.get(applicationProperties.getUploadDir())
                .toAbsolutePath().normalize();
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = inPath!= null ? inPath : this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName; //.replaceFirst("[.][^.]+$", "");

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(Path outPath,String fileName) {
        this.fileOutputLocation = Paths.get(applicationProperties.getOutDir())
                .toAbsolutePath().normalize();
        try {
            outPath = outPath != null? outPath : fileOutputLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(outPath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
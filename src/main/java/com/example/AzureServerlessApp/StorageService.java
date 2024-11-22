package com.example.AzureServerlessApp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class StorageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    public String uploadFile(MultipartFile file) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("uploads");

            BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return "File uploaded successfully: " + blobClient.getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Azure Blob Storage", e);
        }
    }
}

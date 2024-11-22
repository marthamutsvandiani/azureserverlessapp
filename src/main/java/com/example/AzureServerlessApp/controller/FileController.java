package com.example.AzureServerlessApp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClientBuilder;

@Controller
@RequestMapping("/files")
public class FileController {

    // Inject the Azure Storage connection string from application.properties
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    // Specify the container name in Azure Blob Storage
    @Value("${azure.storage.container-name}")
    private String containerName;

    /**
     * Serve the file upload form.
     */
    @GetMapping("/upload")
    public String uploadPage(Model model) {
        model.addAttribute("message", "Upload a file to Azure Blob Storage");
        return "upload"; // This should return the 'upload.html' Thymeleaf template
    }

    /**
     * Upload a file to Azure Blob Storage.
     * @param file Multipart file uploaded by the user.
     * @return ResponseEntity with success or error message.
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Build the BlobClient for the uploaded file
            String blobUrl = new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .blobName(file.getOriginalFilename())
                    .buildClient()
                    .getBlobUrl();

            // Upload the file to Azure Blob Storage
            new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .blobName(file.getOriginalFilename())
                    .buildClient()
                    .upload(file.getInputStream(), file.getSize(), true);

            // Add success message to the model
            model.addAttribute("message", "File uploaded successfully! Blob URL: " + blobUrl);

        } catch (Exception e) {
            // Handle errors and return an error message
            model.addAttribute("message", "File upload failed: " + e.getMessage());
        }

        return "upload"; // Return the same page with a message
    }
}

package com.example.functions;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.AnalyzeImageOptionalParameter;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;

public class ImageProcessingFunction {

    // Replace with your Azure endpoint and API key
    private static final String endpoint = "https://marthaazure.cognitiveservices.azure.com/";
    private static final String subscriptionKey = "325hGF7wokNahWJyBL13qlJw6bDwNDJBmLaXWeVkVY9okkPU2SkPJQQJ99AKACYeBjFXJ3w3AAAFACOGA5Zl";

    // Initialize the Computer Vision client
    private static ComputerVisionClient getClient() {
        return ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);
    }

    public static void analyzeImage(String imageUrl) {
        ComputerVisionClient client = getClient();

        try {
            // Define the features you want to extract
            List<VisualFeatureTypes> features = new ArrayList<>();
            features.add(VisualFeatureTypes.DESCRIPTION);
            features.add(VisualFeatureTypes.CATEGORIES);
            features.add(VisualFeatureTypes.TAGS);

            // Create AnalyzeImageOptionalParameter with the specified features
            AnalyzeImageOptionalParameter options = new AnalyzeImageOptionalParameter()
                    .withVisualFeatures(features);

            // Analyze the image
            ImageAnalysis analysis = client.computerVision()
                    .analyzeImage(imageUrl, options);

            // Output image description
            if (analysis.description() != null && analysis.description().captions().size() > 0) {
                System.out.println("Description: " + analysis.description().captions().get(0).text());
            } else {
                System.out.println("No description found for the image.");
            }
        } catch (Exception e) {
            System.err.println("Error analyzing image: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage with the provided image URL
        String imageUrl = "https://serverlessappstorag.blob.core.windows.net/uploads/background.jpg?sp=r&st=2024-11-22T17:38:57Z&se=2024-11-23T01:38:57Z&spr=https&sv=2022-11-02&sr=b&sig=7%2FcU3aYPXNL2YWMGV3O5ljcUAfPUof005Bk6qQoGoOA%3D";
        analyzeImage(imageUrl);
    }
}

package risinget.commander.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtils {

    public static void saveBase64AsImage(String base64Image, String filePath) {
        // Remove the data URL prefix if present (e.g., "data:image/png;base64,")

        // Decode the Base64 string to a byte array
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Write the byte array to a file
        File outputFile = Paths.get(filePath).toFile();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("Image saved to: " + filePath);
    }
}
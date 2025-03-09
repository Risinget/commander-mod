package risinget.commander.events;

import com.cloudinary.utils.ObjectUtils;
import java.util.Map;
import com.cloudinary.Cloudinary;
import risinget.commander.config.ConfigCommander;

public class CloudinaryScreenshot {

    public static void uploadImage(String path){
        try {
            Cloudinary cloudinary = new com.cloudinary.Cloudinary(ObjectUtils.asMap(
                    "cloud_name", ConfigCommander.getCloudName(),
                    "api_key", ConfigCommander.getApiKeyCloudinary(),
                    "api_secret", ConfigCommander.getApiSecretCloudinary())
            );
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true,
                    "access_control", "[{\"access_type\":\"token\"}]"  // Establecer acceso privado
                    //"access_control", "[{\"access_type\":\"anonymous\",\"start\":\"2025-03-08T12:00Z\",\"end\":\"2025-03-09T12:00Z\"}]"  // Establecer acceso público
            );

            Map uploadResult = cloudinary.uploader().upload(
                    path,
                    params1
            );
            System.out.println(uploadResult);
            String publicId = (String) uploadResult.get("public_id");

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

package risinget.commander.events;

import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import com.cloudinary.Cloudinary;
import risinget.commander.config.ConfigCommander;

public class CloudinaryScreenshot {

    public static boolean uploadImage(String path) throws IOException {



        Cloudinary cloudinary = new com.cloudinary.Cloudinary(ObjectUtils.asMap(
                "cloud_name", ConfigCommander.getCloudName(),
                "api_key", ConfigCommander.getApiKeyCloudinary(),
                "api_secret", ConfigCommander.getApiSecretCloudinary())
        );
        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true,
                "access_control", "[{\"access_type\":\"token\"}]"  // as private
                //"access_control", "[{\"access_type\":\"anonymous\",\"start\":\"2025-03-08T12:00Z\",\"end\":\"2025-03-09T12:00Z\"}]"  // as public with limited time
        );

        Map uploadResult = cloudinary.uploader().upload(
                path,
                params1
        );
        System.out.println(uploadResult);
        String publicId = (String) uploadResult.get("public_id");
        return true;

    }
}

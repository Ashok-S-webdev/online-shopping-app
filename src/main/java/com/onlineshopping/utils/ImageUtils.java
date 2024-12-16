package com.onlineshopping.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class ImageUtils {

    // Method for converting Blob into Base64 String
    public static String Base64Converter(Blob image) {
        if (image == null) {
            return null;
        }

        try (InputStream is = image.getBinaryStream();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = os.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

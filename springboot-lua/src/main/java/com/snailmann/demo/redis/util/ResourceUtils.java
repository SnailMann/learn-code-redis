package com.snailmann.demo.redis.util;

import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author liwenjie
 */
public class ResourceUtils {

    /**
     * script
     */
    public static String getLua(String path) {
        try {
            InputStream input = new FileInputStream(org.springframework.util.ResourceUtils.getFile(path));
            byte[] by = new byte[input.available()];
            input.read(by);
            return new String(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * resource
     */
    public static ClassPathResource resource(String path) {
        try {
            return new ClassPathResource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ClassPathResource("xxx");
    }


}

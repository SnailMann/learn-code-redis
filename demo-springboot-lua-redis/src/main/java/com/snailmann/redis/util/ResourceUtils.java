package com.snailmann.redis.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {

    /**
     * 获得resource下的lua脚本
     *
     * @param path
     * @return
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

}

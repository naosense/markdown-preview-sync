package com.pingao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


/**
 * Created by pingao on 2018/8/3.
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static String readAsString(String path) {
        try {
            return Files.lines(Paths.get(path), Charset.forName("UTF-8")).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOGGER.error("Error occurs on reading content of {}", path, e);
        }
        return "";
    }

    public static byte[] readAllBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            LOGGER.error("Error occurs on reading byte of {}", path, e);
        }
        return new byte[0];
    }

    public static byte[] getBytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error occurs on get bytes of {}", string, e);
        }
        return new byte[0];
    }
}

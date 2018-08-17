package com.pingao.utils;

import org.pmw.tinylog.Logger;

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
    private FileUtils() {
    }

    public static String readAsString(String path) {
        try {
            return Files.lines(Paths.get(path), Charset.forName("UTF-8")).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            Logger.error("Error occurs on reading content of {}", path, e);
        }
        return "";
    }

    public static byte[] readAllBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            Logger.error("Error occurs on reading byte of {}", path, e);
        }
        return new byte[0];
    }

    public static byte[] getBytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.error("Error occurs on get bytes of {}", string, e);
        }
        return new byte[0];
    }
}

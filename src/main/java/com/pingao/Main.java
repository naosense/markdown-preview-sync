package com.pingao;

import com.pingao.server.SocketServer;
import org.pmw.tinylog.Logger;

import java.io.File;


/**
 * Created by pingao on 2018/7/12.
 */
public class Main {
    public static final String ROOT_PATH = resolveRootPath();

    private static String resolveRootPath() {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        File source = new File(path);
        if (source.isDirectory()) {
            return source.getAbsolutePath().replace("%20", " ");
        } else {
            return source.getParentFile().getAbsolutePath().replace("%20", " ");
        }
    }

    public static void main(String[] args) {
        try {
            new SocketServer().start();
        } catch (Exception e) {
            Logger.error("Error occurs on socket server start", e);
        }
    }
}



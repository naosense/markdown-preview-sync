package com.pingao;

import com.pingao.server.MarkDownServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 * Created by pingao on 2018/7/12.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
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
        if (args.length < 2) {
            throw new IllegalArgumentException("Please provide server port and theme");
        }

        MarkDownServer server = MarkDownServer.getInstance();
        server.start(Integer.parseInt(args[0]));
        server.setTheme(args[1]);
        Runtime.getRuntime().addShutdownHook(new Thread(server::destroy));
    }
}



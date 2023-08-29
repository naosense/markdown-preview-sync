package com.pingao.enums;

/**
 * Created by pingao on 2018/7/13.
 */
public enum MiMeType {
    HTML("text/html; charset=UTF-8"),
    PLAIN("text/plain; charset=UTF-8"),
    JAVA_SCRIPT("application/javascript; charset=UTF-8"),
    CSS("text/css; charset=UTF-8"),
    JSON("application/json; charset=UTF-8"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml"),
    ;

    private String type;

    MiMeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

package com.pingao.utils;

import java.nio.charset.Charset;
import java.util.Base64;


/**
 * Created by pingao on 2018/8/1.
 */
public class Base64Utils {
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public static String decode2String(String encode) {
        return new String(DECODER.decode(encode), Charset.forName("UTF-8"));
    }
}

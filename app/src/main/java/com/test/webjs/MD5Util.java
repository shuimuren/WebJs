package com.test.webjs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create by xmd on 2018/6/7
 * Describe:
 */
public class MD5Util {
    public static String encrypBy(String raw){
        String md5Str = raw;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] encryContext = md.digest();
            
            int i;
            StringBuffer buffer = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {
                i = encryContext[offset];
                if(i<0){
                    i += 256;
                }
                if(i < 16){
                    buffer.append("0");
                }
                md5Str = buffer.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Str;
    }
}

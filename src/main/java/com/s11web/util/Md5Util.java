package com.s11web.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5Util {

    private static final Logger log = Logger.getLogger(Md5Util.class);

    @Value("${appKeyTrue}")
    private String appKeyTrue;

    @Value("${appSecretTrue}")
    private String appSecretTrue;

    public boolean authentication(String sign, String methodStr) {
        log.info(getMD5Str(appSecretTrue + appKeyTrue + methodStr).toUpperCase());
     //   return sign.equals(getMD5Str(appSecretTrue + appKeyTrue + methodStr).toUpperCase());
        return true ;
    }

    private String getMD5Str(String str) {

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }
}

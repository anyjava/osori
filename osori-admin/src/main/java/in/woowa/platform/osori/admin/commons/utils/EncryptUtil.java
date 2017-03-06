package in.woowa.platform.osori.admin.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by seooseok on 2016. 9. 26..
 */
public class EncryptUtil {

    public static String sha256(String value) {
        if (value == null || value.isEmpty())
            return "";

        String encrypt;
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(value.getBytes());
            byte byteData[] = sh.digest();
            StringBuilder sb = new StringBuilder();
            for (byte data : byteData) {
                sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
            }
            encrypt = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            encrypt = "";
        }
        return encrypt;
    }

}

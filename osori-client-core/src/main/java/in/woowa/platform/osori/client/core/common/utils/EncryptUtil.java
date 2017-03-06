package in.woowa.platform.osori.client.core.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * Created by seooseok on 2016. 9. 26..
 * 암호화 유틸
 */
public class EncryptUtil {

    public static String sha256(String value) throws GeneralSecurityException {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("encrypt value is empty.");

        String encrypt;

        MessageDigest sh = MessageDigest.getInstance("SHA-256");
        sh.update(value.getBytes());
        byte byteData[] = sh.digest();

        StringBuilder sb = new StringBuilder();
        for (byte data : byteData) {
            sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
        }
        encrypt = sb.toString();

        return encrypt;
    }


    public static String aes128(String key, String value) throws GeneralSecurityException {

        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("encrypt value is empty.");

        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("encrypt key is empty.");

        // use key coss2
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(value.getBytes());

        if (encrypted == null || encrypted.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(encrypted.length * 2);
        String hexNumber;

        for (int x = 0; x < encrypted.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & encrypted[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }
}

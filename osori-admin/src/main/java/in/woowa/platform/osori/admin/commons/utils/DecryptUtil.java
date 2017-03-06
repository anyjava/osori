package in.woowa.platform.osori.admin.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * Created by seooseok on 2016. 10. 17..
 */
public class DecryptUtil {

    /**
     * AES 방식의 복호화
     *
     * @param value 복호화 대상 문자열
     * @return String 복호화 된 문자열
     * @throws Exception
     */
    public static String aes128(String key, String value) throws GeneralSecurityException {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("encrypt value is empty.");

        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("encrypt key is empty.");

        // use key coss2
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] ba = new byte[value.length() / 2];

        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(value.substring(2 * i, 2 * i + 2), 16);
        }

        return new String(cipher.doFinal(ba));
    }


}

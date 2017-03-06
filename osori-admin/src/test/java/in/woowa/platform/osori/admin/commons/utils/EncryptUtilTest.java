package in.woowa.platform.osori.admin.commons.utils;

import org.junit.Test;

/**
 * Created by seooseok on 2016. 10. 12..
 */
public class EncryptUtilTest {
    @Test
    public void sha256() throws Exception {
        //Given

        //When
        System.out.println(EncryptUtil.sha256("admin"));
        //Then
    }

}

package crypt;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by rask on 05.03.2017.
 */
public class md5Crypt {

    public static String md5Apache(String st) {
        String md5Hex = DigestUtils.md5Hex(st);

        return md5Hex;
    }

}

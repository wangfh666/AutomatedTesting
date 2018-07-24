package com.miyuan.rpc.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @author hust
 * @ClassName: AESUtils
 * @Description:
 * @date 2016年11月14日 下午3:16
 */
public class AESUtils {

    public static final String AESKEY = "hy-datasec#*&^!#";

    // 算法/模式/补码方式
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    // 私钥.
    // AES固定格式为128/192/256 bits.即：16/24/32bytes。
    // DES固定格式为128bits，即8bytes。
    private static final byte[] KEY = AESKEY.getBytes(PassportUtils.defaultCharset);
    private static final byte[] IV = new byte[]{104, 117, 115, 104, 117, 110, 116, 105, 110, 103, 95, 95, 95, 95, 95, 95};

    public static void main(String[] args) throws Exception {
        String p = "hejiaoyu_datasec";
        String s = PassportUtils.generatePassword(p);
        System.out.println(s.length());
        System.out.println(s);
        System.out.println(AESUtils.decryptAes(s));
        System.out.println("===="+PassportUtils.checkPasswordParams("0fd1004e111f360e2a66d430b4458c4085108672117210c6741e38144d5f6135a23f3953abe15be3c862f837b9a5cb75"));

//        System.out.println(Arrays.toString("hushunting______".getBytes()));
    }


    /**
     * AES加密
     * @param text
     */
    public static String encryptAes(String text) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec iv = new IvParameterSpec(IV);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(text.getBytes(Charset.forName("utf-8")));
        return Hex.encodeHexString(encrypted);
    }

    /**
     * AES解密
     * @param text
     */
    public static String decryptAes(String text) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec iv = new IvParameterSpec(IV);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] textBytes = Hex.decodeHex(text.toCharArray());
        byte[] encrypted = cipher.doFinal(textBytes);
        return new String(encrypted);
    }

}


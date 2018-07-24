package com.miyuan.rpc.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author hust
 * @ClassName: PassportUtils
 * @Description:
 * @date 2016年11月15日 下午4:37
 */
public class PassportUtils {

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public final static Charset defaultCharset = Charset.forName("utf-8");

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String fuzz = generatePassword("hejiaoyu_datasec");
        System.out.println(fuzz);
        String sha256 = checkPasswordParams(fuzz);
        System.out.println(sha256);

        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000 + "s");
    }

    /**
     * 1. AES解密
     * 2. 时间+hmac+明文 => 13位+8位+明文 = 21 + 明文长度
     * 3. 数据库存 sha256(明文)
     * @param text
     * @return
     */
    public static String checkPasswordParams(String text) throws Exception {
        String decryptAes = AESUtils.decryptAes(text);
        if (!(decryptAes.length() > 21)) {
            return "";
        }
        String t = decryptAes.substring(0,13);
        String h = decryptAes.substring(13,21);
        String password = decryptAes.substring(21,decryptAes.length());
        String hmac = encryptHMAC(password, t); // hmac
        if (StringUtils.isBlank(hmac)) {
            return "";
        }
        if (!h.equals(hmac.substring(0, 8))) {
            return "";
        }
        return sha256(password);
    }

    /**
     * 生成password参数值
     * com.cmcc.datasec.controller.AuthenticateController#authenticateApplication(HttpServletRequest)
     *
     * @param text 明文
     * @return AES(时间+hmac+明文)
     */
    public static String generatePassword(String text) throws Exception {
        String nowTime = String.valueOf(new Date().getTime());
        String temp = nowTime + encryptHMAC(text, nowTime).substring(0, 8) + text;
        return AESUtils.encryptAes(temp);
    }


    public static String encryptHMAC(String data, String key) {
        if (Strings.isNullOrEmpty(data)) {
            return "";
        }
        byte[] bytes = encryptHMAC(data.getBytes(defaultCharset), key);
        return byteArrayToHexString(bytes);
    }


    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteToHexString(byte b) {
        int ret = b;
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * HmacSHA1
     * @param data
     * @param key
     * @return
     */
    private static byte[] encryptHMAC(byte[] data, String key) {
        SecretKey secretKey;
        byte[] bytes = null;
        try {
            secretKey = new SecretKeySpec(key.getBytes(defaultCharset), "HmacSHA1");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * sha256
     * @param decript
     * @return
     */
    public static String sha256(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(decript.getBytes(defaultCharset));
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * sha256
     * @param inStr
     * @return
     */
    public static byte[] sha256Bytes(String inStr) {
        MessageDigest md = null;
        byte[] digest = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            digest = md.digest(inStr.getBytes("utf-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return digest;
    }
}


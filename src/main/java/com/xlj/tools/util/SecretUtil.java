package com.xlj.tools.util;

import cn.hutool.core.codec.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 密码学，加解密
 *
 * @author legend xu
 * @date 2021/12/30
 */
public class SecretUtil {
    public static void main(String[] args) throws Exception {
        // 原文
        String source = "hello world";
        // 定义的key，如果使用DES进行加密，密钥必须是8个字节； 如果使用 AES 进行加密，则需要16个字节
        String key = "12345678";
        // 算法
        String transformation = "DES";
        // 加密类型
        String algorithm = "DES";
        // 加密
        String encryptStr = encryption(source, key, transformation, algorithm);
        // 解密
        decryption(key, transformation, algorithm, encryptStr);
    }

    /**
     * des 对称解密
     *
     * @param key
     * @param transformation
     * @param algorithm
     * @param encryptStr
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static void decryption(String key, String transformation, String algorithm, String encryptStr) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        // 将密文进行 base64 解码
        byte[] decode = Base64.decode(encryptStr);
        // 使用 cipher 进行解密
        byte[] bytes = cipher.doFinal(decode);
        System.out.println("解密后的原文：" + new String(bytes));
    }

    /**
     * des 对称加密
     *
     * @throws Exception
     */
    private static String encryption(String source, String key, String transformation, String algorithm) throws Exception {
        System.out.println("经过Base64直接进行编码后：" + Base64.encode(source.getBytes()));
        // JDK自带的加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 加密规则（key的字节，加密类型）
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 加密初始化（加/解密模式，加密规则）
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        // 调用加密方法
        byte[] bytes = cipher.doFinal(source.getBytes());
        // 未经过 base64 编码的密文会出现乱码，原因是：ASCII编码表上找不到对应的字符
        System.out.println("未经过Base64编码的密文：" + new String(bytes));
        String encode = Base64.encode(bytes);
        System.out.println("经过DES对称加密且Base64编码后的密文：" + encode);
        return encode;
    }
}

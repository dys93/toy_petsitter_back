package com.toy.toy_petsitter_back.util;

import com.toy.toy_petsitter_back.properties.CryptoProperties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Crypto {

    public static String encodeAES256(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CNC/PKCS5Padding");
        cipher.init(
                Cipher.ENCRYPT_MODE, new SecretKeySpec(CryptoProperties.getAeskey256().getBytes(Charset.forName("UTF-8")), "AES"),
                new IvParameterSpec(CryptoProperties.getIvspec().getBytes())
        );
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(Charset.forName("UTF-8"))));
    }

    public static String decodeAES256(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(
                Cipher.DECRYPT_MODE, new SecretKeySpec(CryptoProperties.getAeskey256().getBytes(StandardCharsets.UTF_8), "AES"),
                new IvParameterSpec(CryptoProperties.getIvspec().getBytes())
        );
        return new String(cipher.doFinal(Base64.getDecoder().decode(str)), StandardCharsets.UTF_8);
    }

    public static String encodeSHA128(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(str.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    public static String encodeSHA256(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(str.getBytes(StandardCharsets.UTF_8));
        return String.format("%064x", new BigInteger(1, digest.digest()));
    }

    public static String encodeSHA512(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(str.getBytes(StandardCharsets.UTF_8));
        return String.format("%0128x", new BigInteger(1, digest.digest()));
    }

}

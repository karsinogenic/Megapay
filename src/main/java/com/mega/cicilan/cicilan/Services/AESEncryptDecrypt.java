package com.mega.cicilan.cicilan.Services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESEncryptDecrypt {
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    // @Value("${aes.static.iv}")
    // private String STATIC_IV; // example static IV

    public String encrypt(String plaintext, String key, String STATIC_IV) throws Exception {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(STATIC_IV.getBytes(StandardCharsets.UTF_8));
        // System.out.println("static iv: " + STATIC_IV);

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String ciphertext, String key, String STATIC_IV) throws Exception {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(STATIC_IV.getBytes(StandardCharsets.UTF_8));
        // System.out.println("static iv: " + STATIC_IV);

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        System.out.println("masuk");
        byte[] cipherText = Base64.getDecoder().decode(ciphertext);
        byte[] plainText = cipher.doFinal(cipherText);

        return new String(plainText, StandardCharsets.UTF_8);
    }
}

package com.dac.dac.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class EncryptionService {



    private SecretKey key;
    private Cipher encryptionCipher;

    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_SIZE = 256;

    public EncryptionService(String secretKeyString) throws Exception {
        init(secretKeyString);
    }

    public void init(String secretKeyString) throws Exception {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        key = generateSecretKey(secretKeyString);
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
    }

    public String encrypt(String data) throws Exception {
        byte[] iv = generateIv();
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptedBytes = encryptionCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        byte[] ivAndEncryptedBytes = new byte[IV_LENGTH_BYTE + encryptedBytes.length];
        System.arraycopy(iv, 0, ivAndEncryptedBytes, 0, IV_LENGTH_BYTE);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, IV_LENGTH_BYTE, encryptedBytes.length);

        return Base64.getUrlEncoder().encodeToString(ivAndEncryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        byte[] decodedCipherText = Base64.getUrlDecoder().decode(encryptedData);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        byte[] encryptedBytes = new byte[decodedCipherText.length - IV_LENGTH_BYTE];
        System.arraycopy(decodedCipherText, 0, iv, 0, IV_LENGTH_BYTE);
        System.arraycopy(decodedCipherText, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKey generateSecretKey(String keyString) {
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}

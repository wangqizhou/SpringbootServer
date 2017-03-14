package com.evistek.mediaserver.service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
public interface IAuthService {
    String ALGORITHM_AES = "AES";
    String ALGORITHM_DES = "DES";
    String ALGORITHM_RSA = "RSA";

    String CHARSET = "UTF-8";

    /*
     *  The string used to generate key for AES or DES algorithm.
     */
    String KEY_SOURCE = "key.evistek.com";

    /*
     *  RSA public key with 1024 length.
     */
    String PUBLIC_KEY = "rsa_pub_key_1024.pem";

    /*
     *  RSA private key with 1024 length encoded in PKCS8.
     */
    String PRIVATE_KEY = "pkcs8_rsa_priv_key_1024.pem";

    String getAlgorithm();

    /*
     *  Use keySource to generate AES or DES key.
     */
    SecretKey generateKey(String keySource);

    SecretKey generateKeyFromFile(String filePath);

    SecretKey getKeyForPassword();

    /*
     * Get RSA public key from file PUBLIC_KEY.
     */
    RSAPublicKey getPublicKey();

    /*
     * Get RSA private key from file PRIVATE_KEY.
     */
    RSAPrivateKey getPrivateKey();

    String generateRandomString(int length);

    byte[] encrypt(String content, Key key);

    String encryptToBase64String(String content, Key key);

    byte[] decrypt(byte[] content, Key key);

    byte[] decryptFromBase64String(String content, Key key);
}

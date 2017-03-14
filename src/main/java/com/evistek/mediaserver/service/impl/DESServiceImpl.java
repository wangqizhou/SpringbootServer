package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.service.IAuthService;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Service("DESService")
public class DESServiceImpl implements IAuthService {
    private static SecretKey mKeyForPassword;
    private static final int KEY_LENGHT = 8;

    @Override
    public String getAlgorithm() {
        return IAuthService.ALGORITHM_DES;
    }

    @Override
    public SecretKey generateKey(String key) {
        try {
            if (key.length() < KEY_LENGHT) {
                for (int i = 0; i < (KEY_LENGHT - key.length()); i++) {
                    key += "0";
                }
            }
            DESKeySpec keySpec = new DESKeySpec(key.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            return keyFactory.generateSecret(keySpec);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public SecretKey generateKeyFromFile(String filePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer stringBuffer = new StringBuffer();

        SecureRandom secureRandom = new SecureRandom();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(buffer.charAt(secureRandom.nextInt(range)));
        }

        return stringBuffer.toString();
    }

    @Override
    public SecretKey getKeyForPassword() {
        if (mKeyForPassword == null) {
            mKeyForPassword = generateKey(KEY_SOURCE);
        }
        return mKeyForPassword;
    }

    @Override
    public byte[] encrypt(String content, Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content.getBytes(CHARSET));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String encryptToBase64String(String content, Key key) {
        return Base64.getEncoder().encodeToString(encrypt(content, key));
    }

    @Override
    public byte[] decrypt(byte[] content, Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decryptFromBase64String(String content, Key key) {
        return decrypt(Base64.getDecoder().decode(content), key);
    }

    @Override
    public RSAPublicKey getPublicKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        // TODO Auto-generated method stub
        return null;
    }
}

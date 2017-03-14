package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.service.IAuthService;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/19.
 */
@Service("RSAService")
public class RSAServiceImpl implements IAuthService {
    public static final String PATH_WINDOWS = "d:" + File.separator + "keystore" + File.separator;
    public static final String PATH_LINUX = "/root/keystore/";

    private Cipher mCipher;
    private RSAPublicKey mPublicKey;
    private RSAPrivateKey mPrivateKey;
    private boolean mIsWindowsSystem;

    public RSAServiceImpl() {

        try {
            mCipher = Cipher.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //mLogger.error("No such algorithm: " + ALGORITHM_RSA);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            //mLogger.error("No such padding");
        }

        String os = System.getProperty("os.name");
        //mLogger.info("OS: " + os);
        mIsWindowsSystem = os.startsWith("Windows");
    }

    @Override
    public String getAlgorithm() {
        return IAuthService.ALGORITHM_RSA;
    }

    @Override
    public RSAPublicKey getPublicKey() {
        if (mPublicKey == null) {
            String dir = mIsWindowsSystem ? PATH_WINDOWS : PATH_LINUX;
            try {
                FileInputStream inputStream = new FileInputStream(dir + PUBLIC_KEY);
                mPublicKey = loadPublicKey(inputStream);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //mLogger.error("File not found: " + dir + PRIVATE_KEY);
            } catch (IOException e) {
                e.printStackTrace();
                // mLogger.error("Failed to load file: " + dir + PRIVATE_KEY);
            }
        }

        return mPublicKey;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        if (mPrivateKey == null) {
            String dir = mIsWindowsSystem ? PATH_WINDOWS : PATH_LINUX;
            try {
                FileInputStream inputStream = new FileInputStream(dir + PRIVATE_KEY);
                mPrivateKey = loadPrivateKey(inputStream);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //mLogger.error("File not found: " + dir + PRIVATE_KEY);
            } catch (IOException e) {
                e.printStackTrace();
               // mLogger.error("Failed to load file: " + dir + PRIVATE_KEY);
            }
        }
        return mPrivateKey;
    }

    @Override
    public byte[] encrypt(String content, Key publicKey) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content.getBytes(CHARSET));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String encryptToBase64String(String content, Key publicKey) {
        return Base64.getEncoder().encodeToString(encrypt(content, publicKey));
    }

    @Override
    public byte[] decrypt(byte[] content, Key privateKey) {
        try {
            mCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return mCipher.doFinal(content);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            //mLogger.error("Invalid key!");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            //mLogger.error("Illegal blockSize: " + mCipher.getBlockSize());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            //mLogger.error("Bad padding");
        }

        return null;
    }

    @Override
    public byte[] decryptFromBase64String(String content, Key privateKey) {
        try {
            mCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return mCipher.doFinal(Base64.getDecoder().decode(content));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            //mLogger.error("Invalid key!");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            //mLogger.error("Illegal blockSize: " + mCipher.getBlockSize());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            //mLogger.error("Bad padding");
        }

        return null;
    }

    private RSAPrivateKey loadPrivateKey(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    /*
                     *  With '\r' at the end of each line, we should use Base64.getMimeDecoder(),
                     *  otherwise use the default Base64.getDecoder().
                     */
                    //sb.append('\r');
                }
            }
            return loadPrivateKey(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //mLogger.error("IOException!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            //mLogger.error("NullPointerException!");
        }

        return null;
    }

    private RSAPrivateKey loadPrivateKey(String privateKeyStr) {
        //byte[] buffer = Base64.getMimeDecoder().decode(privateKeyStr);
        byte[] buffer = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //mLogger.error("No Such Algorithm: " + ALGORITHM_RSA);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            //mLogger.error("Invalid PKCS8EncodedKeySpec!");
        }

        return null;
    }

    private RSAPublicKey loadPublicKey(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    /*
                     *  With '\r' at the end of each line, we should use Base64.getMimeDecoder(),
                     *  otherwise use the default Base64.getDecoder().
                     */
                    //sb.append('\r');
                }
            }
            return loadPublicKey(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //mLogger.error("IOException!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            //mLogger.error("NullPointerException!");
        }

        return null;
    }

    private RSAPublicKey loadPublicKey(String publicKeyStr) {
        //byte[] buffer = Base64.getMimeDecoder().decode(privateKeyStr);
        byte[] buffer = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(buffer);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //mLogger.error("No Such Algorithm: " + ALGORITHM_RSA);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            //mLogger.error("Invalid PKCS8EncodedKeySpec!");
        }

        return null;
    }

    @Override
    public SecretKey generateKey(String keySource) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SecretKey generateKeyFromFile(String filePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SecretKey getKeyForPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateRandomString(int length) {
        // TODO Auto-generated method stub
        return null;
    }
}

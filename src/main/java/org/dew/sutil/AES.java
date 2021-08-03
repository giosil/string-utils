package org.dew.sutil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

import java.security.spec.KeySpec;

import java.util.Base64;

/**
 * Advanced Encryption Standard
 */
public 
class AES 
{
  private static final String SECRET_KEY  = "14d4debf-8f97-4251-9a74-a90016b0af0d";
  private static final String SALT        = "f26abbcb-ac74-4422-8a30-edb644bbc1a9";
  private static final byte[] INIT_VECTOR = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  
  public static 
  String encrypt(String s) 
    throws Exception
  {
    IvParameterSpec ivspec = new IvParameterSpec(INIT_VECTOR);
    
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 1000, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
    return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes(StandardCharsets.UTF_8)));
  }
  
  public static 
  String decrypt(String s)
    throws Exception
  {
    IvParameterSpec ivspec = new IvParameterSpec(INIT_VECTOR);
    
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 1000, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
    return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
  }
}

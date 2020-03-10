package jp.col.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
 
public class AesUtil {
     
    private static final String KEY = "cloutofliberty_k";
    private static final String IV = "cloutofliberty_i";
     
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";
     
    private static final String token = "4AaWABnPB5/1Z2EIEXwSfigudpiM4kzLkmIrKQPmS9w=";

    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : new Base64().decodeBase64(new String(base64Code).getBytes());
    }
     
    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {
 
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
         
        byte[] temp = IV.getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(temp);
         
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"), iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
         
        System.out.print(new String(decryptBytes));
        return new String(decryptBytes);
    }
 
    public static String aesDecrypt(String encryptStr) throws Exception { 
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr)); 
    }
 }
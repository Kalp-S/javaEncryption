/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jencrypdes;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64DecoderStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author kalps
 */
public class JEncrypDES {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("Enter the messege: \'No body can see me\'");
        Scanner scan = new Scanner(System.in);
        String m = scan.nextLine();
        Key key = generateKey(); 
        try {
            String encodedText = encodeText(m,key);
            decodeText(encodedText,key);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JEncrypDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(JEncrypDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(JEncrypDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(JEncrypDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(JEncrypDES.class.getName()).log(Level.SEVERE, null, ex);
        }   
   }
    
    static Key generateKey() throws NoSuchAlgorithmException{
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecureRandom secRandom = new SecureRandom();
        keyGen.init(secRandom);
        Key key = keyGen.generateKey();
        return key;
    }
        
    static String encodeText(String message,Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        Cipher ecipher = Cipher.getInstance("DES");      
        ecipher.init(ecipher.ENCRYPT_MODE, key);
        byte[] utf8 = message.getBytes("UTF8");
        byte[] enc;
        enc = ecipher.doFinal(utf8);
        //byte[] bytes = ecipher.doFinal(message.getBytes());      
        
        enc = BASE64EncoderStream.encode(enc);
        System.out.println(enc);
        return new String(enc);
    }
    
    static void decodeText(String message,Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        Cipher dcipher = Cipher.getInstance("DES");      
        dcipher.init(dcipher.DECRYPT_MODE, key);      
        byte[] dec = BASE64DecoderStream.decode(message.getBytes());
        byte[] utf8 = dcipher.doFinal(dec);
        //byte[] bytes = dcipher.doFinal(message.getBytes());      
        System.out.println(new String(utf8, "UTF8"));
    }
}
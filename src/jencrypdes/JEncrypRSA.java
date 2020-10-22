/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jencrypdes;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author kalps
 */
public class JEncrypRSA {
    public static KeyPair keyPairRSA() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
	} catch (Exception e) {
            e.printStackTrace();
        }
        if (generator != null) {
            generator.initialize(2048);
            KeyPair keyPair = generator.genKeyPair();
            return keyPair;
        }
        return null;
    }
    
    public static byte[] encrypt(String original, Key privateKey) {
		if (original != null && privateKey != null) {
			byte[] bs = original.getBytes();
			byte[] encData = convert(bs, privateKey, Cipher.ENCRYPT_MODE);
			return encData;
		}
		return null;
	}

    public static byte[] decrypt(byte[] encrypted, Key publicKey) {
        if (encrypted != null && publicKey != null) {
            byte[] decData = convert(encrypted, publicKey, Cipher.DECRYPT_MODE);
            return decData;
            }
        return null;
    }

    private static byte[] convert(byte[] data, Key key, int mode) {
	try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(mode, key);
            byte[] newData = cipher.doFinal(data);
            return newData;
	} catch (Exception e) {
            e.printStackTrace();
	}
            return null;
	}
    
    	public static void main(String[] args) {
            System.out.println("Enter the messege: \'No body can see me\'");
            Scanner scan = new Scanner(System.in);
            String m = scan.nextLine();
            String password = m;
            KeyPair keyPair = keyPairRSA();
            Key publicKey = keyPair.getPublic();
            Key privateKey = keyPair.getPrivate();
            System.out.println("Original: " + password);
            byte[] encrypted = encrypt(password, privateKey);
            System.out.println();
            System.out.println("Encrypted: " + new String(encrypted));
            byte[] decrypted = decrypt(encrypted, publicKey);
            System.out.println("Decrypted: " + new String(decrypted));
	}
}

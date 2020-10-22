/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jencrypdes;

/**
 *
 * @author kalps
 */
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;


public class client {
	
    public static void main(String[] args)
    {
        byte[] keyBytes, encryptedOutput, decryptedOutput = null, 
                encryptedInput = null;
        String id = "INITIATOR A";
        String host = "localhost";
        String km = "NETWORK SECURITY";
        int port = 5001;
        SecretKey desKey;
        Cipher desCipherObj = null;
        SecretKeyFactory fac;
        String text;

//1        
        try {
            System.out.println("client connection");
            keyBytes =  km.getBytes();
            fac = SecretKeyFactory.getInstance("DES");
            desKey = fac.generateSecret(new DESKeySpec(keyBytes));
            
            System.out.println("Connecting to " + host + " on port " + port);
            
            // connecting to socket.
            Socket client = new Socket(host, port);
            System.out.println("Connected to " + client.getRemoteSocketAddress() +" success!" + "\n" );
            
            DataOutputStream out =
                    new DataOutputStream(client.getOutputStream());
            out.writeUTF(id);
            
//2
            DataInputStream in =
            new DataInputStream(client.getInputStream());
            // Read in length of incoming bytes.
            int duration = in.readInt();
            if(duration > 0) encryptedInput = new byte[duration];
            in.read(encryptedInput, 0, duration);
            System.out.println("received cipher: ");
            System.out.println("Recieved cipher byte code: " + 
                    encryptedInput.toString());
            System.out.println("Recieved cipher string format: " + 
                    new String(encryptedInput) + "\n");
            System.out.println("Decrypting Cipher ...");
            // Create DES Cipher 
            desCipherObj = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipherObj.init(Cipher.DECRYPT_MODE, desKey);
           
            // decrypt the message
            decryptedOutput = desCipherObj.doFinal(encryptedInput);
            System.out.println("Decrypted byte code " + 
                    decryptedOutput.toString());
            System.out.println("Decrypted string format: " + 
                    new String(decryptedOutput) + "\n");
            
//3
            String decryptedText = new String(decryptedOutput);
            String[] decryptedArray = decryptedText.split("\\|");
            
            // Create key out of the string recieved session key.
            keyBytes =  (decryptedArray[0]+ " ").getBytes();
            fac = SecretKeyFactory.getInstance("DES");
            desKey = fac.generateSecret(new DESKeySpec(keyBytes));
           
            // get host ID from decrpyted text.
            encryptedOutput = decryptedArray[2].getBytes();
      
            desCipherObj.init(Cipher.ENCRYPT_MODE, desKey);
            encryptedOutput = desCipherObj.doFinal(encryptedOutput);
            
            System.out.println("Extracted Session key: " + decryptedArray[0]);
            System.out.println("Extracted host ID: " + decryptedArray[2]);
            System.out.println("Sending Cipher ...\n");
            out.writeInt(encryptedOutput.length);
            out.write(encryptedOutput);
            
            in.close();
            out.close();
            client.close();   
         } catch(IOException | InvalidKeyException | NoSuchAlgorithmException |
                InvalidKeySpecException | IllegalBlockSizeException |
               BadPaddingException | NoSuchPaddingException ex) {

        } 
    }
}
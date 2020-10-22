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
import java.net.*;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

public class server
{
    public static void main(String [] args)
    {
        int port = 5001;
        String id = "RESPONDER B";
        String km = "NETWORK SECURITY";
        String ks = "RYERSON ";
        ServerSocket servSocket;
        byte[] keyBytes, encryptedOutput, encryptedInput = null, decryptedOutput;
        SecretKey desKey;
        Cipher desCipherObj = null;
        SecretKeyFactory fac;
        String text;
       
        try
        { 
//1
            System.out.println("SERVER");

            servSocket = new ServerSocket(port);
            servSocket.setSoTimeout(100000);

            Socket server = servSocket.accept();
            System.out.println("Connected to "
                    + server.getRemoteSocketAddress()+ "\n");

            // Print input stream from socket.
            DataInputStream in =
                    new DataInputStream(server.getInputStream());
            String clientID = in.readUTF();
            System.out.println("ID recieved: " + clientID+ "\n");
            
//2
            // Create DES key
            keyBytes = km.getBytes();
            fac = SecretKeyFactory.getInstance("DES");
            desKey = fac.generateSecret(new DESKeySpec(keyBytes));

            //text version of message 
            text = ks + "|" + clientID + "|" + id;
            System.out.println("Sending: " +
                    text);
            
            encryptedOutput = text.getBytes();

            desCipherObj = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipherObj.init(Cipher.ENCRYPT_MODE, desKey);
            encryptedOutput = desCipherObj.doFinal(encryptedOutput);

            //Display the encrypted Cipher on server
            System.out.println("Sending Encrypted byte code " + 
                    encryptedOutput.toString());
            System.out.println("Sending Encrypted string format: " + 
                    new String(encryptedOutput)+ "\n");

            DataOutputStream out =
                new DataOutputStream(server.getOutputStream());
            out.writeInt(encryptedOutput.length);
            out.write(encryptedOutput);
            
//3
            // recieved the cipher text. 
            int duration = in.readInt();

            if(duration > 0) encryptedInput = new byte[duration];
            in.read(encryptedInput, 0, duration);
            System.out.println("received cipher: ");
            System.out.println("Recieved cipher byte code: " + 
                    encryptedInput.toString());
            System.out.println("Recieved cipher string format: " + 
                    new String(encryptedInput) + "\n");
            
            // use key RYERSON
            keyBytes = ks.getBytes();
            fac = SecretKeyFactory.getInstance("DES");
            desKey = fac.generateSecret(new DESKeySpec(keyBytes));
            
            desCipherObj.init(Cipher.DECRYPT_MODE, desKey);
            System.out.println("Decrypting Recieced message ...");
  
            decryptedOutput = desCipherObj.doFinal(encryptedInput);
            
            System.out.println("Decrypted byte code " + 
                    decryptedOutput.toString());
            System.out.println("Decrypted string format: " + 
                    new String(decryptedOutput) + "\n");
            
            in.close();
            out.close();
            server.close();
        }catch(SocketTimeoutException s){

        }catch (IOException | NoSuchAlgorithmException | 
                NoSuchPaddingException | InvalidKeyException | 
                InvalidKeySpecException | IllegalBlockSizeException 
                | BadPaddingException e) {  
        }
    }
}
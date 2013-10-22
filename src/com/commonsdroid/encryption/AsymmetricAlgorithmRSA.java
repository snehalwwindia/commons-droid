package com.commonsdroid.encryption;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

import android.util.Base64;


public class AsymmetricAlgorithmRSA {

	Key publicKey = null;
	Key privateKey = null;

	public AsymmetricAlgorithmRSA() {
		super();
		// Generate key pair for 1024-bit RSA encryption and decryption

		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
		} catch (Exception e) {
			
		}
	}

	public String encryptAsynchRSA(String plainText) {
		// Encode the original data with RSA private key
		byte[] encodedBytes = null;
		try {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.ENCRYPT_MODE, privateKey);
			encodedBytes = c.doFinal(plainText.getBytes());
		} catch (Exception e) {
			
		}
		return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
	}

	public String decryptAsynchRSA(String encodedText) {
		// Decode the encoded data with RSA public key
		byte[] encodedBytes = Base64.decode(encodedText, Base64.DEFAULT);
		byte[] decodedBytes = null;
		try {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, publicKey);
			decodedBytes = c.doFinal(encodedBytes);
		} catch (Exception e) {
			
		}
		return new String(decodedBytes);
	}

}

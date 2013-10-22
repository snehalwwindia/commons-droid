package com.commonsdroid.encryption;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class SymmetricAlgorithmRSA {

	public SymmetricAlgorithmRSA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String encrypt(String plainText, String Base64encodedPublicKey) {
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());

		String encrypted = "";
		try {
			PublicKey key = createPublicKey(Base64
					.decodeBase64(Base64encodedPublicKey.getBytes()));
			byte[] alpha = encrypt(plainText, key);
			encrypted = new String(Base64.encodeBase64(alpha));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return encrypted;
	}

	public String decrypt(String encodedText, String Base64encodedPrivateKey) {
		String decrypted = "";
		try {
			PrivateKey key = createPrivateKey(Base64
					.decodeBase64(Base64encodedPrivateKey.getBytes()));
			decrypted = decrypt(Base64.decodeBase64(encodedText), key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return decrypted;
	}

	public static PublicKey createPublicKey(byte[] pubKeyBytes)
			throws GeneralSecurityException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA", "SC");
		PublicKey pubKey = (PublicKey) kf.generatePublic(keySpec);

		return pubKey;
	}

	public static PrivateKey createPrivateKey(byte[] priKeyBytes)
			throws GeneralSecurityException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(priKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA", "SC");
		PrivateKey priKey = (PrivateKey) kf.generatePrivate(keySpec);

		return priKey;
	}

	public static byte[] encrypt(String text, PublicKey key) {
		byte[] cipherText = null;
		try {

			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA", "SC");
			// encrypt the plain text using the public key

			cipher.init(Cipher.ENCRYPT_MODE, key);

			cipherText = cipher.doFinal(text.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	public static String decrypt(byte[] text, PrivateKey key) {
		byte[] dectyptedText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA", "SC");

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

}

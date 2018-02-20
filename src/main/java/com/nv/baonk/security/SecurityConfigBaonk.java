package com.nv.baonk.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigBaonk {	
	static final char FILE_SEPARATOR   = File.separatorChar;
	static final int BUFFER_SIZE       = 1024;
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfigBaonk.class);
	
	@Value("${CRYPTO.prm}")
	public String prm;	
	@Value("${CRYPTO.pbm}")
	public String pbm;	
	@Value("${CRYPTO.pre}")
	public String pre;	
	@Value("${CRYPTO.apb}")
	public String apb;	
	
	public static boolean encryptFile(String source, String target) throws Exception {
		boolean result              = false;
		String sourceFile           = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile           = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile                = new File(sourceFile);
		BufferedInputStream input   = null;
		BufferedOutputStream output = null;
		
		byte[] buffer = new byte[BUFFER_SIZE];
		
		try {
			if (srcFile.exists() && srcFile.isFile()) {
				input      = new BufferedInputStream(new FileInputStream(srcFile));
				output     = new BufferedOutputStream(new FileOutputStream(targetFile));
				int length = 0;
				
				while ((length = input.read(buffer)) >= 0) {
					byte[] data = new byte[length];
					System.arraycopy(buffer, 0, data, 0, length);
					output.write(encodeBinary(data).getBytes());
					output.write(System.getProperty("line.separator").getBytes());
				}
				
				result = true;
			}
		} 
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception ignore) {
					logger.debug("IGNORE: {}" + ignore);
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					logger.debug("IGNORE: {}" + ignore);
				}
			}
		}
		return result;
	}

	public static boolean decryptFile(String source, String target) throws Exception {
		boolean result              = false;
		String sourceFile           = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile           = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile                = new File(sourceFile);
		BufferedReader input        = null;
		BufferedOutputStream output = null;
		String line                 = null;
		
		try {
			if (srcFile.exists() && srcFile.isFile()) {
				
				input  = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
				output = new BufferedOutputStream(new FileOutputStream(targetFile));
				
				while ((line = input.readLine()) != null) {
					byte[] data = line.getBytes();
					output.write(decodeBinary(new String(data)));
				}
				
				result = true;
			}
		} 
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception ignore) {
					logger.debug("IGNORE: {}" + ignore);
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					logger.debug("IGNORE: {}" + ignore);
				}
			}
		}
		return result;
	}

	public static String encodeBinary(byte[] data) throws Exception {
		if (data == null) {
			return "";
		}
		
		return new String(Base64.encodeBase64(data));
	}

	public static String encode(String data) throws Exception {
		return encodeBinary(data.getBytes());
	}

	public static byte[] decodeBinary(String data) throws Exception {
		return Base64.decodeBase64(data.getBytes());
	}

	public static String decode(String data) throws Exception {
		return new String(decodeBinary(data));
	}

	@Deprecated
	public static String encryptPassword(String data) throws Exception {
		if (data == null) {
			return "";
		}
		
		byte[] plainText = null;
		byte[] hashValue = null;
		plainText        = data.getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		hashValue        = md.digest(plainText);
		
		return new String(Base64.encodeBase64(hashValue));
	}

	public static String encryptPassword(String password, String id) throws Exception {
		if (password == null) {
			return "";
		}
		
		byte[] hashValue = null;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		md.reset();
		md.update(id.getBytes());
		hashValue = md.digest(password.getBytes());
		return new String(Base64.encodeBase64(hashValue));
	}

	public static String encryptPassword(String data, byte[] salt) throws Exception {

		if (data == null) {
			return "";
		}
		
		byte[] hashValue = null;
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(salt);
		hashValue = md.digest(data.getBytes());
		
		return new String(Base64.encodeBase64(hashValue));
	}

	public static boolean checkPassword(String data, String encoded, byte[] salt) throws Exception {
		byte[] hashValue = null;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		md.reset();
		md.update(salt);
		hashValue = md.digest(data.getBytes());
		
		return MessageDigest.isEqual(hashValue, Base64.decodeBase64(encoded.getBytes()));
	}

	public static String decryptRsa(PrivateKey privateKey, String securedValue) {
		String decryptedValue     = "";
		
		try{
			Cipher cipher         = Cipher.getInstance("RSA");
			byte[] encryptedBytes = hexToByteArray(securedValue);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue        = new String(decryptedBytes, "utf-8");// 문자 인코딩 주의.
			
		}catch(Exception e){
			
		}
		return decryptedValue;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[]{};
		}
		byte[] bytes = new byte[hex.length() / 2];
		
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

	public static PrivateKey getPrivateKey(String modulus, String privateExponent) {
		BigInteger modulus_         = new BigInteger(modulus);
		BigInteger privateExponent_ = new BigInteger(privateExponent);
		PrivateKey privateKey       = null;
		
		try {
			privateKey = KeyFactory
					.getInstance("RSA")
					.generatePrivate(new RSAPrivateKeySpec(modulus_, privateExponent_));
		}
		catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return privateKey;
	}

	public String encryptAES(String s) throws Exception {
		String iv16 = apb.substring(0, 16);
		
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(apb.getBytes(), "AES");
			Cipher cipher          = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			//logger.debug("apb: " + apb);
			//logger.debug("iv16: " + iv16);
			
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv16.getBytes("UTF-8")));
			byte[] encrypted = cipher.doFinal(s.getBytes("UTF-8"));
			String enStr     = new String(Base64.encodeBase64(encrypted));
			return enStr;
		}
		catch (Exception e) {
			logger.debug("BAONK: " + apb);
			throw e;
		}
	}

	public String decryptAES(String s) throws Exception {
		String iv16 = apb.substring(0,16);
		
		try{
			SecretKeySpec skeySpec = new SecretKeySpec(apb.getBytes(), "AES");
			Cipher cipher          = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv16.getBytes("UTF-8")));
			
			byte[] byteStr = Base64.decodeBase64(s.getBytes());
			return new String(cipher.doFinal(byteStr),"UTF-8");
		}
		catch(Exception e) {
			throw e;
		}
	}

	public String getPrm() {
		return prm;
	}

	public void setPrm(String prm) {
		this.prm = prm;
	}

	public String getPbm() {
		return pbm;
	}

	public void setPbm(String pbm) {
		this.pbm = pbm;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public String getApb() {
		return apb;
	}

	public void setApb(String apb) {
		this.apb = apb;
	}
}
package com.nsc.Amoski.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import com.nsc.Amoski.entity.TUserEntity;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtil {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static SecureRandom random = new SecureRandom();

	public static Integer getSaltSize(){
		return SALT_SIZE;
	}
	private static Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
	public static void main(String[] args){
		PasswordUtil util = new PasswordUtil();
		System.out.println(util.getPassWordBySalt("13907478559","4690"));
		String salt = "3126";
		System.out.println("salt==>"+salt);
	}
	
	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 * */
	public static void entryptPassword(TUserEntity user) {
		byte[] salt = generateSalt(SALT_SIZE);
		user.setSalt(encodeHex(salt));
		byte[] hashPassword = null;
		try {
			hashPassword = sha1(user.getPassword().getBytes(), salt,HASH_INTERATIONS);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}
		user.setPassword(encodeHex(hashPassword));
	}


	public static String setPassWordBySalt(String Password , String saltString){
		byte[] salt = decodeHex(saltString);
		byte[] hashPassword = null;
		try {
			hashPassword = sha1(Password.getBytes(), salt,HASH_INTERATIONS);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return encodeHex(hashPassword);
	}
	
	public static String getPassWordBySalt(String password,String salt){
		byte[] hashPassword = null;
		try {
			hashPassword = sha1(password.getBytes(), decodeHex(salt),HASH_INTERATIONS);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}
		return encodeHex(hashPassword);
	}
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw unchecked(e);
		}
	}

	public static RuntimeException unchecked(Exception e) {
		if ((e instanceof RuntimeException)) {
			return (RuntimeException) e;
		}
		return new RuntimeException(e);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations)
			throws NoSuchAlgorithmException {
		return digest(input, "SHA-1", salt, iterations);
	}

	private static byte[] digest(byte[] input, String algorithm, byte[] salt,
			int iterations) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);

		if (salt != null) {
			digest.update(salt);
		}

		byte[] result = digest.digest(input);

		for (int i = 1; i < iterations; i++) {
			digest.reset();
			result = digest.digest(result);
		}
		return result;
	}



	private static byte[] SpitOut(byte[] input, String algorithm, byte[] salt,
								 int iterations) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);

		if (salt != null) {
			digest.update(salt);
		}

		byte[] result = digest.digest(input);

		for (int i = 1; i < iterations; i++) {
			digest.reset();
			result = digest.digest(result);
		}
		return result;
	}

	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0,
				"numBytes argument must be a positive integer (1 or larger)",
				numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}


}

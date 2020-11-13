package com.oracle.eot;

import java.security.SecureRandom;
import java.util.Arrays;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class test {
	public static void main(String args[]) {
		String password = "1234";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode(password));
		
		

		SecureRandom sr = new SecureRandom();
		byte[] randomBytes = new byte[64];
		sr.nextBytes(randomBytes);
		System.out.println(Arrays.toString(randomBytes));
		
	}
}

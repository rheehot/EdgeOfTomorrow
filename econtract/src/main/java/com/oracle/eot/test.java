package com.oracle.eot;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class test {
	public static void main(String args[]) throws MalformedURLException {
		String password = "1234";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode(password));
		
		

		SecureRandom sr = new SecureRandom();
		byte[] randomBytes = new byte[64];
		sr.nextBytes(randomBytes);
		System.out.println(Arrays.toString(randomBytes));

		Path fileLocation = Paths.get("upload-dir");
		Path filePath = fileLocation.resolve("a.txt").normalize();
		Resource resource = new UrlResource(filePath.toUri());
		System.out.println(filePath.toString() + ":" + filePath.isAbsolute() + ":" + resource.exists());
		
	}
}

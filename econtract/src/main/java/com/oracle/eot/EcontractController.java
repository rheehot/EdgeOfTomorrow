package com.oracle.eot;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EcontractController {

	@Autowired
	UserRepository userRepository;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		// User user = new User();
		// user.setUserid("user1");
		// user.setCompanyid("company1");

		String userid = "user1";

		Optional<User> customerList = userRepository.findById(userid);
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/login")
	public boolean login(@RequestParam String userid, @RequestParam String password, @RequestParam String companyid) {

		return false;
	}
}
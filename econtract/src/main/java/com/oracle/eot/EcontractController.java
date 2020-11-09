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
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/login")
	public User login(@RequestParam String userid, @RequestParam String password, @RequestParam String companyid) {
//		 User user = new User();
//		 user.setUserid("user1");
//		 user.setCompanyid("company1");

		Optional<User> user = userRepository.findById("user1");

        if(!user.isPresent()) {
        	System.out.println(" no user1");
            throw new IllegalArgumentException();
        }
        
		return user.get();
	}
}
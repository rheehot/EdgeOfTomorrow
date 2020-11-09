package com.oracle.eot;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.eot.dao.User;
import com.oracle.eot.exception.EotException;
import com.oracle.eot.repo.UserRepository;

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
		Optional<User> userOpt = userRepository.findById(userid);

        if(!userOpt.isPresent()) {
//            throw new IllegalArgumentException();
        	throw new EotException(9001, userid + " is not exist");
        }
        
        User user = userOpt.get();

        if(!user.getPassword().equals(password)) {
        	throw new EotException(9002, "Password Incorrect");
        }
		return user;
	}
	
}
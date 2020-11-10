package com.oracle.eot;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oracle.eot.dao.User;
import com.oracle.eot.exception.EotException;
import com.oracle.eot.repo.MasterRepository;
import com.oracle.eot.repo.UserRepository;
import com.oracle.eot.storage.StorageFileNotFoundException;
import com.oracle.eot.storage.StorageService;

@RestController
public class EcontractController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MasterRepository masterRepository;
	
	@Autowired
	private StorageService storageService;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@GetMapping("/")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println(storageService);
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/login")
	public User login(Principal principal) {
		String userid = principal.getName();
		Optional<User> userOpt = userRepository.findById(userid);

		if (!userOpt.isPresent()) {
			throw new EotException(9001, userid + " is not exist");
		}
		return userOpt.get();
	}

	@PostMapping("/request")
	public String requestContract(@RequestParam("contractFile") MultipartFile contractFile, RedirectAttributes redirectAttribute) {
		storageService.store(contractFile);
		redirectAttribute.addFlashAttribute("message",
				"You successfully uploaded " + contractFile.getOriginalFilename() + "!");
		return "redirect:/";
	}

	public String approveContract() {
		return "redirect:/";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
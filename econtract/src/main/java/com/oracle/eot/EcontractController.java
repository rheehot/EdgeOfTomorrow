package com.oracle.eot;

import java.security.Principal;
import java.sql.Date;
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

import com.oracle.eot.dao.Master;
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
	public User getUser(Principal principal) {
		String userid = principal.getName();
		Optional<User> userOpt = userRepository.findById(userid);

		if (!userOpt.isPresent()) {
			throw new EotException(9001, userid + " is not exist");
		}
		return userOpt.get();
	}

	@PostMapping("/request")
	public String requestContract(
			Principal principal,
			@RequestParam("pid") String pid,
			@RequestParam("requestEmail") String requestEmail,
			@RequestParam("approveName") String approveName,
			@RequestParam("approveEmail") String approveEmail,
			@RequestParam("contractFile") MultipartFile contractFile, 
			@RequestParam("requestFile")  MultipartFile requestFile,
			RedirectAttributes redirectAttribute) {
		

		//1. 사용자 정보를 가져온다.
		User user = getUser(principal);
		
		//2. Master 레코드를 만든다.
		Master master = new Master();
		master.setPid(pid);
		master.setRequestDT(new Date(System.currentTimeMillis()));
		master.setRequestName(user.getName());
		master.setRequestEmail(requestEmail);
		master.setApproveName(approveName);
		master.setApproveEmail(approveEmail);
		master = masterRepository.save(master);
		System.out.println(master);
		
		String prefix = Integer.toString(master.getCid()) + "-";
		
		//3. contractFile을 ObjectStorage에 저장한다.
		String contractPath = storageService.store(prefix, contractFile);
		master.setContractPath(contractPath);
		System.out.println("contractPath-->" + contractPath);
		
		//4. requestFile을 ObjectStorage에 저장한다.
		prefix = prefix + master.getRequestEmail() + "-";
		String requestPath = storageService.store(prefix, requestFile);
		master.setRequestPath(requestPath);
		System.out.println("requestPath-->" + requestPath);

		//5. Blockchain에 등록한다.
		String txid = null;
		master.setTxid("txid1");
		
		//6. 해쉬코드를 등록한다.
		master.setContractHash("chash1");
		master.setRequestHash("rhash1");
		
		//7. 레코드를 업데이트 한다.
		masterRepository.save(master);
		
		
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
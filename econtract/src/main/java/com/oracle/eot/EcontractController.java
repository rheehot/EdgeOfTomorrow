package com.oracle.eot;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.DocumentException;
import com.oracle.eot.dao.Master;
import com.oracle.eot.dao.User;
import com.oracle.eot.exception.EotException;
import com.oracle.eot.repo.MasterRepository;
import com.oracle.eot.repo.UserRepository;
import com.oracle.eot.storage.PdfConvertService;
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
	
	@Autowired
	private PdfConvertService pdfConvertService;
	
	

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

	@PostMapping("/contract")
	public String requestContract(
			Principal principal,
			@RequestPart String pid,
			@RequestPart String approveName,
			@RequestPart String approveEmail,
			@RequestPart MultipartFile contractFile, 
			@RequestPart MultipartFile requestFile,
			RedirectAttributes redirectAttribute) {
		

		//1. 사용자 정보를 가져온다.
		User user = getUser(principal);
		
		//2. Master 레코드를 만든다.
		Master master = new Master();
		master.setRequestDT(new Date(System.currentTimeMillis()));
		master.setPid(pid);
		master.setRequestName(user.getName());
		master.setRequestEmail(user.getEmail());
		master.setApproveName(approveName);
		master.setApproveEmail(approveEmail);
		master = masterRepository.save(master);
		System.out.println(master);
	
		UUID uuid = UUID.randomUUID();
		String prefix = null;
		
		//3. contractFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid())  + "-" + uuid + "-contract-";
		String contractObj = storageService.store(prefix, contractFile);
		master.setContractFile(contractObj);
		System.out.println("contractFile-->" + contractObj);
		
		//4. requestFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid())  + "-" + uuid + "-" + master.getRequestEmail() + "-";
		String requestObj = storageService.store(prefix, requestFile);
		master.setRequestFile(requestObj);
		System.out.println("requestFile-->" + requestObj);

		//5. Blockchain에 등록한다.
		String txid = null;
		master.setTxid("txid1");
		
		//6. 해쉬코드를 등록한다.
		master.setContractHash(master.getContractFile().hashCode());
		master.setRequestHash(master.getRequestFile().hashCode());
		
		//7. 레코드를 업데이트 한다.
		masterRepository.save(master);
		
		
		redirectAttribute.addFlashAttribute("message","계약서 요청이 완료되었습니다.");
		
		return "redirect:/";
	}



	@PutMapping("/contract/{cid}")
	public String approveContract(
			Principal principal,
			@PathVariable("cid") int cid,
			@RequestPart MultipartFile approveFile,
			RedirectAttributes redirectAttribute) {
		

		//1. 사용자 정보를 가져온다.
		User user = getUser(principal);
		
		//2. Master 레코드를 읽는다.
		Master master = masterRepository.getOne(cid);
		System.out.println(master);
		
		//3. 사용자 이름을 비교한다.
		if(	!master.getApproveName().equals(user.getName()) || 
			!master.getApproveEmail().equals(user.getEmail())) {
			throw new EotException(9003,"사용자가 다릅니다.");
		}
		
		//4. 날짜를 등록한다.
		master.setApproveDT(new Date(System.currentTimeMillis()));
		
		UUID uuid = UUID.randomUUID();
		String prefix = null;

		//5. approveFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid())  + "-" + uuid + "-" + master.getApproveEmail() + "-";
		String approveObj = storageService.store(prefix, approveFile);
		master.setApproveFile(approveObj);
		System.out.println("approveFile-->" + approveObj);

		//6. 최종PDF를 만든다.
		String agreementFile = null;
		try {
			agreementFile = pdfConvertService.convert(master);
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EotException(9000, e);
		}
		
		
		//7. pdfFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid())  + "-" + uuid + "-agreement-";
		String agreementObj = storageService.store(prefix, agreementFile);
		master.setAgreementFile(agreementObj);
		System.out.println("setAgreementFile-->" + agreementObj);
		
		//6. Blockchain에 등록한다. ?????
//		String txid = null;
//		master.setTxid("txid1");
		
		//7. 해쉬코드를 등록한다.
		master.setApproveHash(master.getApproveFile().hashCode());
		master.setAgreementHash(master.getAgreementFile().hashCode());
		
		//8. 레코드를 업데이트 한다.
		masterRepository.save(master);
		
		
		redirectAttribute.addFlashAttribute("message","승인이 완료되었습니다.");
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
package com.oracle.eot;

import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.DocumentException;
import com.oracle.eot.dao.ContractStatus;
import com.oracle.eot.dao.History;
import com.oracle.eot.dao.Master;
import com.oracle.eot.dao.User;
import com.oracle.eot.dto.Item;
import com.oracle.eot.exception.EotException;
import com.oracle.eot.repo.ContractStatusRepository;
import com.oracle.eot.repo.HistoryRepository;
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
	private HistoryRepository historyRepository;

	@Autowired
	private ContractStatusRepository contractStatusRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private PdfConvertService pdfConvertService;

	@GetMapping("/")
	public Message login(@RequestParam(value = "message", defaultValue = "Hello") String message) {
		System.out.println(storageService);
		return new Message(message);
	}

	@PostMapping("/login")
	public User login(Principal principal) {
		System.out.println(storageService);
		return getUser(principal);
	}
	
	private User getUser(Principal principal) {
		String userid = principal.getName();
		
		Optional<User> userOpt = userRepository.findById(userid);

		if (!userOpt.isPresent()) {
			throw new EotException(userid + " is not exist");
		}
		return userOpt.get();
	}
	
	@GetMapping("/users")
	public List<User> getUserList(Principal principal) {
		if (!principal.getName().equals("admin")) {
			throw new EotException("you have no permission to do");
		}

		return userRepository.findAll();
	}

	@GetMapping("/users/{userid}")
	public User getUser(Principal principal, @PathVariable("userid") String userid) {
		if (!principal.getName().equals("admin") && !principal.getName().equals(userid)) {
			throw new EotException("you have no permission to do");
		}
		
		Optional<User> userOpt = userRepository.findById(userid);

		if (!userOpt.isPresent()) {
			throw new EotException(userid + " is not exist");
		}
		return userOpt.get();
	}



	@GetMapping("/list")
	public List<Item> getContractList(Principal principal) {
		List<Item> itemList = new ArrayList<Item>();

		// 1. 사용자 정보를 가져온다.
		User user = getUser(principal);

		// 2. Master 레코드를 읽는다.
		List<Master> masterList = masterRepository.findByRequestEmail(user.getEmail());

		for (Master master : masterList) {
			Item item = new Item();
			item.setApproveEmail(master.getApproveEmail());
			item.setApproveName(master.getApproveName());
			item.setCid(master.getCid());
			if (master.getApproveDT() != null)
				item.setDt(master.getApproveDT());
			else
				item.setDt(master.getRequestDT());
			item.setTitle(master.getTitle());
			item.setTxid(master.getTxid());

			itemList.add(item);
		}

		return itemList;

	}

	@GetMapping("/contracts")
	public List<Master> getContracts(Principal principal) {
		// 1. 사용자 정보를 가져온다.
		User user = getUser(principal);

		// 2. 신청한 건수들
		List<Master> list = masterRepository.findByRequestEmail(user.getEmail());

		return list;

	}

	@GetMapping("/contracts/{uuid}")
	public Master getContract(Principal principal, @PathVariable("uuid") String uuid) {
		// 1. 사용자 정보를 가져온다.
		User user = getUser(principal);

		// 2. Master 레코드를 읽는다.
		Master master = masterRepository.findByUuid(uuid);
		System.out.println(master);

		// 3. 신청자인지 비교한다.
		if (!master.getRequestName().equals(user.getName()) || !master.getRequestEmail().equals(user.getEmail())) {
			throw new EotException("해당 계약에 대한 요청자가 아닙니다.");
		}

		return master;

	}

	@PostMapping("/contracts")
	@ResponseBody
	public ResponseEntity requestContract(Principal principal, @RequestPart String title, @RequestPart(value="pid", required = false) String pid,
			@RequestPart String approveName, @RequestPart String approveEmail, @RequestPart MultipartFile contractFile,
			@RequestPart MultipartFile requestFile, RedirectAttributes redirectAttribute) {

		
		String uuid = UUID.randomUUID().toString();

		// 1. 사용자 정보를 가져온다.
		User user = getUser(principal);

		if(pid == null || pid.isEmpty()) {
			pid = uuid;
		}
		// 2. Master 레코드를 만든다.
		Master master = new Master();
		master.setUuid(uuid);
		master.setPid(pid);
		master.setTitle(title);
		master.setRequestName(user.getName());
		master.setRequestEmail(user.getEmail());
		master.setApproveName(approveName);
		master.setApproveEmail(approveEmail);
		master = masterRepository.save(master);
		System.out.println(master);

		String prefix = null;

		// 3. contractFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid()) + "-" + uuid + "-contract-";
		String contractObj = storageService.store(prefix, contractFile);
		master.setContractFile(contractObj);
		System.out.println("contractFile-->" + contractObj);

		// 4. requestFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid()) + "-" + uuid + "-" + master.getRequestEmail() + "-";
		String requestObj = storageService.store(prefix, requestFile);
		master.setRequestFile(requestObj);
		System.out.println("requestFile-->" + requestObj);

		// 5. 해쉬코드를 등록한다.
		master.setContractHash(master.getContractFile().hashCode());
		master.setRequestHash(master.getRequestFile().hashCode());

		// 6. Blockchain에 등록한다.
		// -------------------------
		// 블록체인을 부르자~~~ 테스트하자~~
		// setTxid 를 호출하자~~
		// -------------------------
		String txid = null;
		master.setTxid("txid1");

		// 7. 날짜를 등록한다.
		master.setRequestDT(Timestamp.valueOf(LocalDateTime.now()));

		// 8. 레코드를 업데이트 한다.
		masterRepository.save(master);

		// 9. 히스토리 업데이트
		makeHistory(master.getUuid(), ContractStatus.REQUEST);

		// 10. email 보내는 쪽 호출
		makeHistory(master.getUuid(), ContractStatus.EMAIL);

		return ResponseEntity.ok().body(new Message("계약서 요청이 완료되었습니다. uuid=" + master.getUuid()));
	}

	@PutMapping("/contracts/{uuid}")
	@ResponseBody
	public ResponseEntity approveContract(Principal principal, @PathVariable("uuid") String uuid,
			@RequestPart("approveEmail") String approveEmail, @RequestPart MultipartFile approveFile,
			RedirectAttributes redirectAttribute) {

		// 2. Master 레코드를 읽는다.
		Master master = masterRepository.findByUuid(uuid);
		System.out.println(master);

		if(master == null) {
			throw new EotException(uuid + " 에 대한 계약이 없습니다.");
		}
		
		// 3. 승인자인지 비교한다.
		if (!master.getApproveEmail().equals(approveEmail)) {
			throw new EotException(approveEmail + " 은 지정된 승인자가 아닙니다.");
		}

		String prefix = null;

		// 4. approveFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid()) + "-" + master.getUuid() + "-" + master.getApproveEmail() + "-";
		String approveObj = storageService.store(prefix, approveFile);
		master.setApproveFile(approveObj);
		System.out.println("approveFile-->" + approveObj);

		// 5. 최종PDF를 만든다.
		String agreementFile = null;
		try {
			agreementFile = pdfConvertService.convert(master);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
			throw new EotException(e);
		}

		// 6. pdfFile을 ObjectStorage에 저장한다.
		prefix = Integer.toString(master.getCid()) + "-" + master.getUuid() + "-agreement-";
		String agreementObj = storageService.store(prefix, agreementFile);
		master.setAgreementFile(agreementObj);
		System.out.println("setAgreementFile-->" + agreementObj);

		// 7. 해쉬코드를 등록한다.
		master.setApproveHash(master.getApproveFile().hashCode());
		master.setAgreementHash(master.getAgreementFile().hashCode());

		// 8. Blockchain에 등록한다. ?????
//		String txid = null;
//		master.setTxid("txid1");

		// 9. 날짜를 등록한다.
		master.setApproveDT(Timestamp.valueOf(LocalDateTime.now()));

		// 10. 레코드를 업데이트 한다.
		masterRepository.save(master);

		// 11. history 업데이트
		makeHistory(master.getUuid(), ContractStatus.APPROVE);
		makeHistory(master.getUuid(), ContractStatus.DONE);

		return ResponseEntity.ok().body(new Message("계약서 승인이 완료되었습니다."));
	}

	@GetMapping("/history/{uuid}")
	public List<History> getHistory(Principal principal, @PathVariable("uuid") String uuid) {
		List<History> historyList = historyRepository.findByUuid(uuid);
		return historyList;
	}
	

	private History makeHistory(String uuid, int status) {
		History history = new History();
		history.setUuid(uuid);
		history.setHistoryDT(Timestamp.valueOf(LocalDateTime.now()));

		Optional<ContractStatus> statusOpt = contractStatusRepository.findById(status);
		if (statusOpt.isEmpty()) {
			throw new EotException("status code " + status + " is not exist");
		}
		history.setState(statusOpt.get().getContext());
		return historyRepository.save(history);
	}

	@ExceptionHandler(EotException.class)
	public ResponseEntity<?> handleEotException(EotException e) {
		return ResponseEntity.badRequest().body(new Message(e.getMessage()));
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e) {
		return ResponseEntity.notFound().build();
	}

}
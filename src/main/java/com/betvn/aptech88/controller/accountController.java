package com.betvn.aptech88.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.betvn.aptech88.model.Ban;
import com.betvn.aptech88.model.ChangePassword;
import com.betvn.aptech88.model.account;
import com.betvn.aptech88.model.protect_time;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.accountRepository;
import com.betvn.aptech88.repository.protect_timeRepository;
import com.betvn.aptech88.repository.walletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.utility.RandomString;
import ultis.emailContent;
import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8000/")
@RestController
public class accountController {
	@Autowired
	accountRepository accounts;

	@Autowired
	walletRepository wallets;

	@Autowired
	protect_timeRepository protect_times;

	@Autowired
	JavaMailSender emailSender;

	// get account
	@RequestMapping(value = mapping.ACCOUNT_GET)
	public List<account> get() {
		List<account> account_list = accounts.findByUsernameNot("admin");
		return account_list;
	}
	//find account by id of android
			@RequestMapping(value = "/FindAccount", method = RequestMethod.POST, consumes = {"application/json"})
			public ResponseEntity<?> find(@RequestBody String id_account)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode jsonNode = mapper.readTree(id_account);
					int id=jsonNode.get("id").asInt();
					//find account
					account c = accounts.findById(id);
					//check if found account
					if( c != null)
					{
						//return found account
						return new ResponseEntity<>(c, HttpStatus.CREATED);
					}
					else {
						//if not found account return with id = 0
						return ResponseEntity.status(404).body("Account not found");
					}
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			//find account by phone
				@RequestMapping(value = "/AccountPhone", method = RequestMethod.POST, consumes = {"application/json"})
				public ResponseEntity<?> find_phone(@RequestBody String phone)
				{
					ObjectMapper mapper = new ObjectMapper();
					try {
						JsonNode jsonNode = mapper.readTree(phone);
						String phones=jsonNode.get("phonenumber").asText();
						//find account
						account c = accounts.findByPhonenumber(phones);
						//check if found account
						if( c != null)
						{
							//return found account
							return new ResponseEntity<>(c, HttpStatus.CREATED);
						}
						else {
							//if not found account return with id = 0
							return ResponseEntity.status(404).body("Phone not found");
						}
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
	// find account by id
	@RequestMapping(value = mapping.ACCOUNT_FIND, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> find(@RequestBody int id) {
		// find account
		account c = accounts.findById(id);
		// check if found account
		if (c != null) {
			// return found account
			return new ResponseEntity<>(c, HttpStatus.CREATED);
		} else {
			return ResponseEntity.status(404).body("Account not found");
		}
	}

	// create account
	@RequestMapping(value = mapping.ACCOUNT_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody account c) {
		account acc = new account();
		// check if email is taken
		if (accounts.existsByEmail(c.getEmail())) {
			return ResponseEntity.status(409).body("Email is taken");
		}
		// check if phone number is taken
		else if (accounts.existsByPhonenumber(c.getPhonenumber())) {
			return ResponseEntity.status(409).body("Phone is taken");

		}
		// check if username is taken
		else if (accounts.existsByUsername(c.getUsername())) {
			return ResponseEntity.status(409).body("Username is taken");
		} else {
			try {
				// set status true for account, false = banned
				c.setStatus(true);
				// set protect_time for account , 1 = no protect
				c.setProctectTimeId(1);
				c.setProtect_time(protect_times.findById(1));
				// hash password
				int strength = 10; // work factor of bcrypt
				BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
				String encodedPassword = bCryptPasswordEncoder.encode(c.getPassword());
				c.setPassword(encodedPassword);
				// create verified code
				String randomCode = RandomString.make(64);
				c.setVerifiedCode(randomCode);
				// set created time for verification code
				// get current time
				LocalTime local_time = LocalTime.now();
				// convert it to sql.time
				Time current_time = Time.valueOf(local_time);
				c.setVerifiedCreateDate(current_time);
				// if save successfully , save account
				acc = accounts.save(c);
				// send verification email
				sendVerificationEmail(acc);
				// return account
				return new ResponseEntity<account>(acc, HttpStatus.CREATED);
			} catch (Exception ex) {
				// if account not created
				return ResponseEntity.status(409).body("Account cannot be create");
			}
		}
	}

	// verifiy account
	@RequestMapping(value = mapping.ACCOUNT_VERIFY)
	public String verify(@RequestParam(name = "code") String code) {
		// find account by verified code
		account acc = accounts.findByVerifiedCode(code);
		// if not found return fail
		if (acc == null || acc.isVerified()) {
			return "Cannot verifiy please try again";
		} else {
			// if found, set verified = true and return success
			acc.setVerified(true);
			acc.setVerifiedCode(null);
			acc.setVerifiedCreateDate(null);
			// create wallet if verified
			// create wallet
			wallet w = new wallet();
			w.setAccount(acc);
			w.setAccountId(acc.getId());
			wallets.save(w);
			accounts.save(acc);
			return "Verified";
		}
	}

	// change password
	// input json = {"id":"account_id","new_password":"new
	// password","old_password":"old password"}
	@RequestMapping(value = mapping.ACCOUNT_PASSWORD_CHANGE, method = RequestMethod.POST, consumes = {
			"application/json" })
	public ResponseEntity<?> changePassword(@RequestBody ChangePassword cp) {
		// found account
		account c = accounts.findById(cp.getId());
		// check if account is found
		if (c != null) {

			int strength = 10;
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(strength, new SecureRandom());
			// check if old pass is match
			if (bcrypt.matches(cp.getOld_password(), c.getPassword())) {
				// if match hash new pass and return account with hashed pass
				String encodedPassword = bcrypt.encode(cp.getNew_password());
				c.setPassword(encodedPassword);
				return new ResponseEntity<account>(accounts.save(c), HttpStatus.CREATED);
			} else {
				// if old pass not match
				return ResponseEntity.status(406).body("Password not match");
			}
		} else {
			// if account not found
			return ResponseEntity.status(404).body("Account not found");

		}

	}

	// edit account
	@RequestMapping(value = mapping.ACCOUNT_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> edit(@RequestBody account c) {
		account acc = accounts.findById(c.getId());
		// check if account found
		if (acc != null) {
			if (accounts.existsByEmail(c.getPhonenumber())) {
				return ResponseEntity.status(409).body("Phone is taken");
			}
			acc.setAddress(c.getAddress());
			acc.setProvince(c.getProvince());
			acc.setCountry(c.getCountry());
			acc.setName(c.getName());
			acc.setAge(c.getAge());
			acc.setPhonenumber(c.getPhonenumber());
			c = accounts.save(acc);
			return new ResponseEntity<account>(c, HttpStatus.CREATED);

		} else {
			// if not found
			return ResponseEntity.status(404).body("Account not found");
		}
	}

	// maximum deposit, only required id and maximum_deposit value, maximum_deposit
	// = 0 mean no limit
	@RequestMapping(value = mapping.ACCOUNT_MAXIUMUM_DEPOSIT, method = RequestMethod.POST, consumes = {
			"application/json" })
	public ResponseEntity<?> maximum(@RequestBody account acc) {
		// find account
		account c = accounts.findById(acc.getId());
		if (accounts != null) {
			// return account if save sucess
			c.setMaximumDeposit(acc.getMaximumDeposit());
			acc = accounts.save(c);
			return new ResponseEntity<account>(c, HttpStatus.CREATED);
		}

		else {
			// check if not found account
			return ResponseEntity.status(404).body("Account not found");
		}
	}

	// send verified
	@RequestMapping(value = mapping.ACCOUNT_SEND_VERIFY, method = RequestMethod.POST, consumes = { "application/json" })
	public Boolean sendVerify(@RequestBody int id) {
		// find account
		account acc = accounts.findById(id);
		// check if not found
		if (acc == null || acc.isVerified()) {
			return false;
		} else {
			// get current time
			LocalTime local_time = LocalTime.now();
			// convert it to sql.time
			Time current_time = Time.valueOf(local_time);
			acc.setVerifiedCreateDate(current_time);
			String randomCode = RandomString.make(64);
			acc.setVerifiedCode(randomCode);

			try {
				accounts.save(acc);
				// if save sucess send verification email
				sendVerificationEmail(acc);
			} catch (Exception ex) {
				return false;
			}
			return true;
		}
	}

	// ban account
	@RequestMapping(value = mapping.ACCOUNT_BAN, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> ban(@RequestBody Ban ban) {
		account acc = new account();
		try {
			acc = accounts.findById(ban.getId());
			acc.setStatus(false);
			acc.setBannedReason(ban.getReason());
			// return acc if sucess
			return new ResponseEntity<account>(accounts.save(acc), HttpStatus.CREATED);
		} catch (Exception ex) {
			return ResponseEntity.status(404).body("Account not found");

		}
	}

	// unban
	@RequestMapping(value = mapping.ACCOUNT_UNBAN, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> unban(@RequestBody int id) {
		// find account if not exists return account with id = 0
		account acc = new account();
		try {
			acc = accounts.findById(id);
			if (!acc.isStatus()) {
				acc.setStatus(true);
				acc.setBannedReason(null);
				return new ResponseEntity<account>(accounts.save(acc), HttpStatus.CREATED);
			} else {
				// if account no ban return response
				return ResponseEntity.status(304).body("Cannot unabn due to error");

			}

		} catch (Exception ex) {
			return ResponseEntity.status(404).body("Account not found");
		}
	}

	// protect account
	@RequestMapping(value = mapping.ACCOUNT_PROTECT, method = RequestMethod.POST)
	public ResponseEntity<?> protect(@RequestBody String account, String protect) {

		// convert string to int
		int account_id = Integer.valueOf(account);
		int protect_id = Integer.valueOf(protect);
	
		// find protect time
		protect_time p = protect_times.findById(protect_id);
		if (p == null || p.getId() == 1) {
			// if null or id = 1(no protect) return response
			return ResponseEntity.status(404).body("Protect = 1 or not found");

		} else {
			// find account
			account a = accounts.findById(account_id);
			if (a == null) {
				// if null return
				return ResponseEntity.status(404).body("Account not found");
			} else {
				// get current date
				LocalDate local_date = LocalDate.now();
				// convert it into sql.date
				Date current_date = Date.valueOf(local_date);
				a.setProtectTimeStart(current_date);
				a.setProctectTimeId(p.getId());
				// set status = false
				a.setStatus(false);
				return new ResponseEntity<account>(accounts.save(a), HttpStatus.CREATED);
			}
		}
	}

	// send verification email
	private void sendVerificationEmail(account acc) throws MessagingException, UnsupportedEncodingException {
		// get email content
		String content = emailContent.EMAIL_CONTENT;
		// replace name = username
		content = content.replace("[[name]]", acc.getUsername());
		// get current request url
		String current_request = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		// get url by remove last occurrence
		String url = current_request.substring(0, current_request.lastIndexOf("/"));
		// generate verification url
		String verifyURL = url + "/AccountVerify?code=" + acc.getVerifiedCode();
		// replae url = verifyURL;
		content = content.replace("[[URL]]", verifyURL);
		MimeMessage message = emailSender.createMimeMessage();
		message.setSubject("Verify your registration");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);
		helper.setFrom("Aptech88");
		helper.setTo(acc.getEmail());
		helper.setText(content, true);
		// send email
		emailSender.send(message);

	}

	// login
	@RequestMapping(value = mapping.ACCOUNT_LOGIN, method = RequestMethod.POST, consumes = { "application/json" })
	private  ResponseEntity<?> login(@RequestBody account acc) {
		account a = new account();
		// check if username exist
		if (accounts.existsByUsername(acc.getUsername())) {
			// find account
			a = accounts.findByUsername(acc.getUsername());
			// check if account is verified
			if (a.isVerified()) {
				// check if status = true, false = banned or protect
				if (a.isStatus()) {
					// hash password to compare
					int strength = 10; // work factor of bcrypt
					BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength,
							new SecureRandom());
					// check if password is equal
					if (bCryptPasswordEncoder.matches(acc.getPassword(), a.getPassword())) {
						// if true return a
						return new ResponseEntity<account>(a,HttpStatus.OK);
					} else {
						// return if password not match
						return ResponseEntity.status(404).body("Password not match");		

					}
				} else {
					// if status = false and protect = 1 acccount is banned, return with status = 0
					// and banned_reason
					// if status = false = protect != 1 account is protected, return protect_time_id
					// = time left
					if (a.getProctectTimeId() != 1) {
						// convert sql.date to LocalDate
						LocalDate start_date = a.getProtectTimeStart().toLocalDate();
						// get day protect
						long day = a.getProtect_time().getValue();
						// get finish date
						start_date = start_date.plusDays(day);
						// get current date
						LocalDate current_date = LocalDate.now();
						// get date left
						long date_left = ChronoUnit.DAYS.between(current_date, start_date);
						return ResponseEntity.status(403).body("Account is locked, days left: "+date_left);		


					}
					return ResponseEntity.status(403).body("Account is banned, reason: "+a.getBannedReason());		

				}
			} else
			// if account is not verified
			{
				return ResponseEntity.status(401).body("Account not verified");		
			}

		} else {
			// if not find account return with username =0
			return ResponseEntity.status(404).body("Account not found");		


		}
	}

}

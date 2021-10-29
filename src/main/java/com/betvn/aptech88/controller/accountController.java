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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.betvn.aptech88.model.Ban;
import com.betvn.aptech88.model.account;
import com.betvn.aptech88.model.protect_time;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.accountRepository;
import com.betvn.aptech88.repository.protect_timeRepository;
import com.betvn.aptech88.repository.walletRepository;

import net.bytebuddy.utility.RandomString;
import ultis.emailContent;
import ultis.mapping;

@Controller
public class accountController {
	@Autowired
	accountRepository accounts;
	
	@Autowired
	walletRepository wallets;
	
	@Autowired
	protect_timeRepository protect_times;
	
	@Autowired
	JavaMailSender emailSender;
	
	//get account
	@RequestMapping(value = mapping.ACCOUNT_GET)
	public @ResponseBody List<account> get()
	{
		List<account> account_list = accounts.findAll();
		return account_list;
	}
    // create account
	@RequestMapping(value = mapping.ACCOUNT_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody account create(@RequestBody account c) {
		account acc = new account();
		// check if email is taken, if true return account with email = 0
		if (accounts.existsByEmail(c.getEmail())) {
			acc.setEmail("0");
			return acc;
		}
		// check if phone number is taken, if true return account with number = 0
		else if (accounts.existsByPhonenumber(c.getPhonenumber())) {
			acc.setPhonenumber("0");
			return acc;

		}
		// check if username is taken, if true return account with username = 0
		else if (accounts.existsByUsername(c.getUsername())) {
			acc.setUsername("0");
			return acc;
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
				//get current time
				LocalTime local_time = LocalTime.now();
				//convert it to sql.time
				Time current_time = Time.valueOf(local_time);
				c.setVerifiedCreateDate(current_time);
				// if save successfully , save account
				acc = accounts.save(c);
				//send verification email
				sendVerificationEmail(acc);
				// create wallet
				wallet w = new wallet();
				w.setAccount(acc);
				w.setAccountId(acc.getId());
				wallets.save(w);
				// return account
				return acc;

			} catch (Exception ex) {
				// if account not created return with id = 0
				acc.setId(0);
				return acc;
			}
		}
	}

	// verifiy account
	@RequestMapping(value = mapping.ACCOUNT_VERIFY)
	public @ResponseBody Boolean verify(@RequestParam(name = "code") String code) {
		//find account by verified code
		account acc = accounts.findByVerifiedCode(code);
		//if not found return fail
		if (acc == null || acc.isVerified()) {
			return false;
		} else {
			//if found, set verified = true and return success
			acc.setVerified(true);
			acc.setVerifiedCode(null);
			acc.setVerifiedCreateDate(null);
			accounts.save(acc);
			return true;
		}
	}
	
	// maximum deposit, only required id and maximum_deposit value
	@RequestMapping(value = mapping.ACCOUNT_MAXIUMUM_DEPOSIT, method = RequestMethod.POST, consumes = {"application/json"})
	public @ResponseBody account maximum(@RequestBody account acc)
	{
		//find account
		acc =accounts.findById(acc.getId());
		if(accounts != null)
		{
			//return account
			acc.setMaximumDeposit(acc.getMaximumDeposit());
			return accounts.save(acc);
		}
		
		else
		{
			//return with id = 0 if not found account
			acc.setId(0);
			return acc;
		}
	}
	
	//send verified
	@RequestMapping(value = mapping.ACCOUNT_SEND_VERIFY, method = RequestMethod.POST,consumes = { "application/json" })
	public @ResponseBody Boolean sendVerify(@RequestBody int id)
	{
		//find account
		account acc = accounts.findById(id);
		//check if not found 
		if(acc == null || acc.isVerified())
		{
			return false;
		}
		else
		{
			//get current time
			LocalTime local_time = LocalTime.now();
			//convert it to sql.time
			Time current_time = Time.valueOf(local_time);
			acc.setVerifiedCreateDate(current_time);
			String randomCode = RandomString.make(64);
			acc.setVerifiedCode(randomCode);
			
			try {
				accounts.save(acc);
				//if save sucess send verification email
				sendVerificationEmail(acc);
			}catch(Exception ex)
			{
				return false;
			}
			return true;
		}
	}
	
	// ban account
		@RequestMapping(value = mapping.ACCOUNT_BAN, method = RequestMethod.POST,consumes = { "application/json" })
		public @ResponseBody account ban(@RequestBody Ban ban) {
			//find account if not exists return account with id = 0
			account acc = new account();
			try {			
				acc =accounts.findById(ban.getId());
				acc.setStatus(false);
				acc.setBannedReason(ban.getReason());
				//return acc if sucess
				return accounts.save(acc);
			} catch (Exception ex)
			{
				acc.setId(0);
				return acc;
			}
		}
	//unban
		@RequestMapping(value = mapping.ACCOUNT_UNBAN, method = RequestMethod.POST,consumes = { "application/json" })
		public @ResponseBody account ban(@RequestBody int id) {
			//find account if not exists return account with id = 0
			account acc = new account();
			try {			
				acc =accounts.findById(id);
				if(!acc.isStatus())
				{
					acc.setStatus(true);
					acc.setBannedReason(null);
					return accounts.save(acc);
				}
				else
				{
					//if account no ban return with id =0 , status = 0
					acc.setId(0);
					acc.setStatus(false);
					return acc;
					
				}
				
			} catch (Exception ex)
			{
				acc.setId(0);
				return acc;
			}
		}
	
	//protect account
		@RequestMapping(value = mapping.ACCOUNT_PROTECT, method = RequestMethod.POST)
		public @ResponseBody account protect(@RequestBody String account, String protect) {
			
			//convert string to int
			int account_id = Integer.valueOf(account);
			int protect_id = Integer.valueOf(protect);
			account acc = new account();
			//find protect time
			protect_time p =protect_times.findById(protect_id);
			if(p == null || p.getId() == 1)
			{
				//if null or id = 1(no protect) return protect = 0
				acc.setProctectTimeId(0);
				return acc;
			}
			else
			{
				//find account
				account a = accounts.findById(account_id);
				if(a == null)
				{
					//if null return id = 0
					acc.setId(0);
					return acc;
				}
				else
				{
					//get current date
					LocalDate local_date = LocalDate.now();
					//convert it into sql.date
					Date current_date = Date.valueOf(local_date);
					a.setProtectTimeStart(current_date);					
					a.setProctectTimeId(p.getId());
					//set status = false 
					a.setStatus(false);
					return accounts.save(a);
				}
			}
		}
		
	//send verification email
	private void sendVerificationEmail(account acc)
	        throws MessagingException, UnsupportedEncodingException {
		//get email content 
	    String content =  emailContent.EMAIL_CONTENT;
	    //replace name = username 
	    content = content.replace("[[name]]", acc.getUsername());
	    //get current request url
	    String current_request = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
	    //get url by remove last occurrence
	    String url = current_request.substring(0, current_request.lastIndexOf("/"));
	    //generate verification url
	    String verifyURL =  url+"/AccountVerify?code=" + acc.getVerifiedCode();
	    //replae url = verifyURL;
	    content = content.replace("[[URL]]", verifyURL);
        MimeMessage message = emailSender.createMimeMessage();
        message.setSubject("Verify your registration");
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setFrom("Aptech88");
        helper.setTo(acc.getEmail());
        helper.setText(content, true);
        //send email
        emailSender.send(message);
	    	  	     
	}
	
	//login
	@RequestMapping(value = mapping.ACCOUNT_LOGIN, method=RequestMethod.POST, consumes = {"application/json"})
	private @ResponseBody account login(@RequestBody account acc)
	{
		account a = new account();
		//check if username exist
		if(accounts.existsByUsername(acc.getUsername()))
		{
			// find account
			a = accounts.findByUsername(acc.getUsername());
			//check if account is verified
			if(a.isVerified())
			{
				//check if status = true, false = banned or protect
				if(a.isStatus())
				{
					//hash password to compare
					int strength = 10; // work factor of bcrypt
					BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
					//check if password is equal
					if(bCryptPasswordEncoder.matches(acc.getPassword(), a.getPassword())) {
						//if true return a
						return a;
					}
					else
					{
						//return with password = 0
						a.setPassword("0");
						return a;
					}
				}
				else
				{
					//if status = false and protect = 1 acccount is banned, return with status = 0 and banned_reason
					//if status = false = protect != 1 account is protected, return protect_time_id = time left
					if(a.getProctectTimeId() != 1)
					{
						//convert sql.date to LocalDate
						LocalDate start_date = a.getProtectTimeStart().toLocalDate();
						//get day protect
						long day = a.getProtect_time().getValue();
						//get finish date
						start_date = start_date.plusDays(day);											
						//get current date
						LocalDate current_date = LocalDate.now();
						//get date left
						long date_left = ChronoUnit.DAYS.between( current_date , start_date );
						//set protect_time_id = time left
						a.setProctectTimeId((int) date_left);
						return a;
						
					}
					return a;
				}
			}
			else
				//if account is not verified, return with verified = false
			{
				return a;
			}
			
		
		}
		else
		{
			//if not find account return with username =0
			a.setUsername("0");
			return a;
			
		}
	}

}

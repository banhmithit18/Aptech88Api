package com.betvn.aptech88.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Time;
import java.time.LocalTime;

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
			accounts.save(acc);
			return true;
		}
	}
	
	//send verified
	@RequestMapping(value = mapping.ACCOUNT_SEND_VERIFY, method = RequestMethod.POST,consumes = { "application/json" })
	public @ResponseBody Boolean resendVerify(@RequestBody int id)
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
		@RequestMapping(value = mapping.ACCOUNT_PROTECT, method = RequestMethod.POST,consumes = { "application/json" })
		public @ResponseBody account protect(@RequestBody int account_id,int protect_id) {
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
					a.setProctectTimeId(p.getId());
					a.setProtect_time(p);
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

}

package com.betvn.aptech88.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.account;
import com.betvn.aptech88.model.payment;
import com.betvn.aptech88.model.promotion;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.accountRepository;
import com.betvn.aptech88.repository.paymentRepository;
import com.betvn.aptech88.repository.promotionRepository;
import com.betvn.aptech88.repository.walletRepository;

import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
public class paymentController {
	@Autowired
	paymentRepository payments;
	@Autowired
	walletRepository wallets;
	@Autowired
	promotionRepository promotions;
	@Autowired
	accountRepository accounts;


	// create payment
	// cal api payment first then import to database
	// paymentType = "deposit" ,"withdraw" must have paymentType
	@RequestMapping(value = mapping.PAYMENT_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity <?> create(@RequestBody payment p) {
		//find wallet
		wallet w = wallets.findById(p.getWalletId());
		// check if wallet is found
		if (w != null) {
			double current_amount = w.getAmount();
			// get promotion if have
			promotion promotion = promotions.findById(p.getPromotionId());
			// get current amount
			// check if payment type is deposit
			if (p.getPaymentType().equals("deposit")) {
				// add to walet
				if (promotion != null) {
					w.setAmount(current_amount + (p.getAmount() * promotion.getValue()));
					account a = w.getAccount();
					a.setTodayDeposit(p.getAmount());
					accounts.save(a);
				} else {
					//if promotion is null set promotion id to default = 1
					p.setPromotionId(1);
					w.setAmount(current_amount + p.getAmount());
					account a = w.getAccount();
					a.setTodayDeposit(p.getAmount());
					accounts.save(a);
				}

			} else if (p.getPaymentType().equals("withdraw")) {
				// subtract from wallet
				double amount_left = w.getAmount() - p.getAmount();
				// check if ammout in wallet is bigger than payment
				if (amount_left > 0) {
					w.setAmount(amount_left);
				} else {
					// if not enough return
					return ResponseEntity.status(404).body("insufficient balance");		

				}
			} else {
				// if not found type return 
				return ResponseEntity.status(404).body("type not found");		
			}

			try {
				wallets.save(w);
				// if wallet is saved set status payment to true
				p.setStatus(true);
				// get current time
				LocalDateTime date = LocalDateTime.now();	
				//convert to sql timestamp
				Timestamp sqlDate = Timestamp.valueOf(date);
				p.setPaymentDate(sqlDate);
				return new ResponseEntity<payment>(payments.save(p),HttpStatus.CREATED);

			} catch (Exception ex) {
				// if cannot save wallet 
				return ResponseEntity.status(304).body("Error! Try again later");		
			}

		} else {
			// if not found wallet
			return ResponseEntity.status(404).body("Wallet not found");		
		}
	}

	// find payment by wallet id
	@RequestMapping(value = mapping.PAYMENT_FIND, method = RequestMethod.POST, consumes = { "application/json" })
	public List<payment> find(@RequestBody int id) {
		List<payment> payment_list = payments.findAllByWalletId(id);
		return payment_list;
	}
	//get payment between
	
	// get all payment
	@RequestMapping( value = mapping.PAYMENT_GET)
	public List<payment> get() {
		List<payment> payment_list = payments.findAll();
		return payment_list;
	}

	//get wallet by account id
	@RequestMapping (value = "/payment/getPaymentByWallet")
	public @ResponseBody List<Object[]> findById (HttpServletRequest request)
	{
		int id = Integer.parseInt(request.getParameter("id"));
		int skip = Integer.parseInt(request.getParameter("skip"));
		int take = Integer.parseInt(request.getParameter("take"));
		List<Object[]> p = payments.findPaymentPagination(id, skip, take);
		return p;
	}

	@RequestMapping (value = "/payment/countPaymentByWalletId")
	public @ResponseBody int countPaymentByWalletId (HttpServletRequest request)
	{
		int id = Integer.parseInt(request.getParameter("id"));
		List<payment> p = payments.findAllByWalletId(id);
		return p.size();
	}

	@RequestMapping (value = "/payment/filterDate")
	public @ResponseBody List<Object[]> filterDate (HttpServletRequest request)
	{
		List<Object[]> p = null;
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		if(fromDate == "" || toDate == ""){
			return p;
		}
		int skip = Integer.parseInt(request.getParameter("skip"));
		int take = Integer.parseInt(request.getParameter("take"));
		int wallet_id = Integer.parseInt(request.getParameter("wallet_id"));

		p = payments.filterDate(fromDate, toDate, skip, take,wallet_id);
		return p;
	}

	@RequestMapping (value = "/payment/countOfFilter")
	public @ResponseBody int countOfFilter (HttpServletRequest request)
	{
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		if(fromDate == "" || toDate == ""){
			return 0;
		}
		int wallet_id = Integer.parseInt(request.getParameter("wallet_id"));

		List<Object[]> p = payments.countOfFilter(fromDate, toDate,wallet_id);
		return p.size();
	}
}

package com.betvn.aptech88.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betvn.aptech88.model.payment;
import com.betvn.aptech88.model.promotion;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.paymentRepository;
import com.betvn.aptech88.repository.promotionRepository;
import com.betvn.aptech88.repository.walletRepository;

import ultis.mapping;

@Controller
public class paymentController {
	@Autowired
	paymentRepository payments;
	@Autowired
	walletRepository wallets;
	@Autowired
	promotionRepository promotions;


	// create payment
	// cal api payment first then import to database
	// paymentType = "deposit" ,"withdraw" must have paymentType
	@RequestMapping(value = mapping.PAYMENT_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody payment create(@RequestBody payment p) {
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
				} else {
					//if promotion is null set promotion id to default = 1
					p.setPromotionId(1);
					w.setAmount(current_amount + p.getAmount());

				}

			} else if (p.getPaymentType().equals("withdraw")) {
				// subtract from wallet
				double amount_left = w.getAmount() - p.getAmount();
				// check if ammout in wallet is bigger than payment
				if (amount_left > 0) {
					w.setAmount(amount_left);
				} else {
					// if not enough return paymnent with amount = 0
					p.setAmount(0);
					return p;
				}
			} else {
				// if not found type return payment with paymentType = not found
				p.setPaymentType("not found type");
				return p;

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
				return payments.save(p);

			} catch (Exception ex) {
				// if cannot save wallet return payment with status = 0
				return p;
			}

		} else {
			// if not found wallet return with wallet id = 0
			p.setWalletId(0);
			return p;
		}
	}

	// find payment by wallet id
	@RequestMapping(value = mapping.PAYMENT_FIND, method = RequestMethod.POST, consumes = { "application/json" })
	public @ResponseBody List<payment> find(@RequestBody int id) {
		List<payment> payment_list = payments.findAllByWalletId(id);
		return payment_list;
	}
	//get payment between
	
	// get all payment
	@RequestMapping( value = mapping.PAYMENT_GET)
	public @ResponseBody List<payment> get() {
		List<payment> payment_list = payments.findAll();
		return payment_list;
	}

}
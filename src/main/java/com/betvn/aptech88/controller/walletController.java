package com.betvn.aptech88.controller;

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

import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.walletRepository;

import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
public class walletController {
	@Autowired walletRepository wallets;
	
	//get all wallet
	@RequestMapping ( value = mapping.WALLET_GET)
	public  List<wallet> get() {
		List<wallet> wallet_list = wallets.findAll();
		return wallet_list;
	}
	
	//get wallet by account id
	@RequestMapping ( value = mapping.WALLET_GET, method = RequestMethod.POST, consumes = {"application/json"})
	public ResponseEntity<?> find (@RequestBody int id)
	{
		wallet w = wallets.findByAccountId(id);
		if(w != null)
		{
			return new ResponseEntity<wallet>(w,HttpStatus.ACCEPTED);
		}
		else
		{
			return ResponseEntity.status(404).body("Wallet not found");		
		}
	}
	//get wallet by account id
	@RequestMapping ( value = "/wallet/getAmount")
	public @ResponseBody double find (HttpServletRequest request)
	{
		int id = Integer.parseInt(request.getParameter("id"));
		wallet w = wallets.findByAccountId(id);
		return w.getAmount();
	}
}

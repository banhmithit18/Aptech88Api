package com.betvn.aptech88.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.walletRepository;

import ultis.mapping;

@Controller
public class walletController {
	@Autowired walletRepository wallets;
	
	//get all wallet
	@RequestMapping ( value = mapping.WALLET_GET)
	public @ResponseBody List<wallet> get() {
		List<wallet> wallet_list = wallets.findAll();
		return wallet_list;
	}
	
	//get wallet by account id
	@RequestMapping ( value = mapping.WALLET_GET, method = RequestMethod.POST, consumes = {"application/json"})
	public @ResponseBody wallet find (@RequestBody int id)
	{
		wallet w = wallets.findByAccountId(id);
		return w;
	}
}

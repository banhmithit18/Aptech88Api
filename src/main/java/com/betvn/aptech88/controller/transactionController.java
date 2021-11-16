package com.betvn.aptech88.controller;

import java.sql.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.transaction;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.transactionRepository;

import ultis.mapping;

@RestController
public class transactionController {
	
	@Autowired transactionRepository transactions;
	
	//get all transaction
	@RequestMapping(value = mapping.TRANSACTION_GET, method = RequestMethod.GET)
	public List<transaction> get(){
		List<transaction> list_transaction = transactions.findAll();
		return list_transaction;
	}
	
	//get all transactions by reason 
	@RequestMapping(value = mapping.TRANSACTION_FINDBYREASON, method = RequestMethod.POST)
	public List<transaction> getByReason (@RequestBody transaction t)
	{
		List<transaction> list_transaction = transactions.findByReason(t.getReason());
		return list_transaction;
	}
	
	//get transaction by id 
	@RequestMapping(value = mapping.TRANSACTION_FINDBYID, method = RequestMethod.POST)
	public ResponseEntity<?> getById(@RequestBody transaction t)
	{
		transaction tr = transactions.findById(t.getId());
		if(tr != null)
		{
			return new  ResponseEntity<transaction>(tr,HttpStatus.OK);
		}
		else
		{
		 return ResponseEntity.status(404).body("Not found");	
		}
	}
	
	//get tranasction by status
	@RequestMapping(value = mapping.TRANSACTION_FINDBYSTATUS, method = RequestMethod.POST)
	public List<transaction> getByStatus (@RequestBody transaction t )
	{
		List<transaction> list_transaction = transactions.findByStatus(t.getStatus());
		return list_transaction;
	}
	
	//get transaction by walletID by
	@RequestMapping(value = mapping.TRANSACTION_FINDBYWALLET, method = RequestMethod.POST)
	public List<transaction> getByWallet (@RequestBody wallet w)
	{
		List<transaction> list_transaction_send = transactions.findByFromWallet(w.getId());
		List<transaction> list_transaction_receive = transactions.findByToWallet(w.getId());
		list_transaction_send.addAll(list_transaction_receive);
		return list_transaction_send;
	}
	
	
	// json {"fromDate" :"yyyy-MM-dd", "toDate" :"yyyy-MM-dd"} must not null
	//get transaction between date
	@RequestMapping(value = mapping.TRANSACTION_FINDBYDATE, method = RequestMethod.POST)
	public List<transaction> getByDate (@RequestBody String inputJson)
	{
		//convert string to object
		JSONObject ob = new JSONObject(inputJson);
		//get date string
		String from_date_string = ob.get("fromDate").toString();
		String to_date_string = ob.get("toDate").toString();
		//convert to sql
		Date fromDate = Date.valueOf(from_date_string);
		Date toDate = Date.valueOf(to_date_string);
		//get list
		List<transaction> list_transaction =transactions.findByTransactionDateBetween(fromDate, toDate);
		return list_transaction;
		
	}
}

package com.betvn.aptech88.controller;

import java.sql.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.bet_history;
import com.betvn.aptech88.repository.bet_historyRepository;

import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
public class bet_historyController {

	@Autowired
	bet_historyRepository bet_histories;

	@RequestMapping(value = mapping.HISTORY_GET)
	public List<bet_history> get() {
		List<bet_history> list_bet_history = bet_histories.findAll();
		return list_bet_history;
	}

	// get all
	@RequestMapping(value = mapping.HISTORY_FINDBYID, method = RequestMethod.POST)
	public ResponseEntity<?> findById(@RequestBody bet_history bh) {
		bet_history bet = bet_histories.findById(bh.getId());
		if (bet != null) {
			return new ResponseEntity<bet_history>(bet, HttpStatus.OK);
		} else {
			return ResponseEntity.status(404).body("Not found");
		}
	}

	// get by account
	@RequestMapping(value = mapping.HISTORY_FINDBYACCOUNT, method = RequestMethod.POST)
	public List<bet_history> findByAccount(@RequestBody bet_history bh) {
		List<bet_history> list_bet_history = bet_histories.findByAccountId(bh.getAccountId());
		return list_bet_history;
	}

	// json {"fromDate" :"yyyy-MM-dd", "toDate" :"yyyy-MM-dd"} must not null
	// get transaction between date
	@RequestMapping(value = mapping.HISTORY_FINDBYDATE, method = RequestMethod.POST)
	public List<bet_history> findByFixture(@RequestBody String inputJson) {
		// convert string to object
		JSONObject ob = new JSONObject(inputJson);
		// get date string
		String from_date_string = ob.get("fromDate").toString();
		String to_date_string = ob.get("toDate").toString();
		// convert to sql
		Date fromDate = Date.valueOf(from_date_string);
		Date toDate = Date.valueOf(to_date_string);
		// get list
		List<bet_history> list_transaction = bet_histories.findByDateBetween(fromDate, toDate);
		return list_transaction;
	}
}

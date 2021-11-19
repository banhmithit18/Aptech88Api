package com.betvn.aptech88.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.odd;
import com.betvn.aptech88.repository.oddRepository;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
public class oddController {
	@Autowired
	oddRepository odds;

//edit odd rate //
	@RequestMapping()
	public ResponseEntity<?> edit(odd o) {
		odd odd = odds.findById(o.getId());
		// check null
		if (odd != null) {
			try {
				odd.setValue(o.getValue());
				return new ResponseEntity<odd>(odds.save(odd), HttpStatus.OK);
			} catch (Exception ex) {
				return ResponseEntity.status(404).body("Error, try again later");
			}
		} else {
			return ResponseEntity.status(404).body("not found");
		}
	}
	//get with id fixture
	@RequestMapping(value = "/GetOddById", method = RequestMethod.POST, consumes = { "application/json" })
	public odd getOddById(@RequestBody odd f) {
		odd odd = odds.findById(f.getId());
		return odd;

	}
}

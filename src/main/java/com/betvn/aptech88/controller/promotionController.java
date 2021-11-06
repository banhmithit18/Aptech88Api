package com.betvn.aptech88.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.promotion;
import com.betvn.aptech88.repository.promotionRepository;

import ultis.mapping;

@RestController
public class promotionController {
	@Autowired
	promotionRepository promotions;

	// get all promotions
	@RequestMapping(value = mapping.PROMOTION_GET)
	public @ResponseBody List<promotion> get() {
		List<promotion> promotion_list = promotions.findAll();
		return promotion_list;

	}

	// create promotion
	@RequestMapping(value = mapping.PROMOTION_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody promotion p) {
		try {
			// return promotion if saved
			return new ResponseEntity<promotion>(promotions.save(p), HttpStatus.CREATED);
		} catch (Exception ex) {
			return ResponseEntity.status(400).body("Error! Try again later");
		}

	}

	// edit promotion
	@RequestMapping(value = mapping.PROMOTION_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> edit(@RequestBody promotion p) {
		promotion promotion = promotions.findById(p.getId());

		// check if promotion is found
		if (promotion != null) {
			promotion.setValue(p.getValue());
			promotion.setName(p.getName());
			promotion.setStatus(p.getStatus());
			return new ResponseEntity<promotion>(promotions.save(promotion), HttpStatus.CREATED);

		} else {
			// if not found 
			return ResponseEntity.status(404).body("Promotion not found");		
		}
	}

	@RequestMapping(value = mapping.PROMOTION_DELETE, method = RequestMethod.POST, consumes = { "application/json" })
	public  ResponseEntity<?> delete(@RequestBody int id) {
		promotion p = promotions.findById(id);
		// check if found promotion
		if (p != null)
			try {
				promotions.deleteById(id);
				// return promotion if delete success
				return new ResponseEntity<promotion>( p,HttpStatus.CREATED);
			} catch (Exception ex) {
				// if cannot delete 
				return ResponseEntity.status(409).body("Cannot deleted beacuse it is in used");		
			}
		else {
			// if not found r
			return ResponseEntity.status(404).body("Promotion not found ");		
		}
	}

}

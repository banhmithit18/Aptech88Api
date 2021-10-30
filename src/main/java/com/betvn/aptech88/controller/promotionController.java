package com.betvn.aptech88.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betvn.aptech88.model.promotion;
import com.betvn.aptech88.repository.promotionRepository;

import ultis.mapping;

@Controller
public class promotionController {
	@Autowired promotionRepository promotions;
	
	
	//get all promotions
	@RequestMapping ( value = mapping.PROMOTION_GET)
	public @ResponseBody List<promotion> get ()
	{
		List<promotion> promotion_list  = promotions.findAll();
		return promotion_list;
		
	}
	
	//create promotion
	@RequestMapping( value = mapping.PROMOTION_CREATE, method = RequestMethod.POST, consumes= {"application/json"})
	public @ResponseBody promotion create (@RequestBody promotion p)
	{
		try {
			//return promotion if saved
			return promotions.save(p);
		}catch(Exception ex)
		{
			//if failed to save return promotion with id = 0
			return p;
		}
		
	}
	
	//edit promotion
	@RequestMapping ( value = mapping.PROMOTION_EDIT, method = RequestMethod.POST, consumes = {"application/json"})
	public @ResponseBody promotion edit (@RequestBody promotion p)
	{
		promotion promotion = promotions.findById(p.getId());
		
		//check if promotion is found
		if( promotion != null)
		{
			promotion.setValue(p.getValue());
			promotion.setName(p.getName());
			promotion.setStatus(p.getStatus());
			return promotions.save(promotion);
			
		}
		else
		{
			//if not found retun with id = 0
			p.setId(0);
			return p;
		}
	}
	
	@RequestMapping ( value = mapping.PROMOTION_DELETE, method = RequestMethod.POST, consumes = {"application/json"})
	public @ResponseBody promotion delete (@RequestBody int id)
	{
		promotion p = promotions.findById(id);
		//check if found promotion
		if(p!= null)
		try {
			promotions.deleteById(id);
			//return promotion if delete success
			return p;
		}catch (Exception ex)
		{
			//if cannot delete return promotion with id = -1
			p.setId(-1);
			return p;
		}
		else {
			//if not found return promotion with id = 0
			return p;
		}
	}

	
}

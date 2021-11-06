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

import com.betvn.aptech88.model.protect_time;
import com.betvn.aptech88.repository.protect_timeRepository;

import ultis.mapping;

@RestController
public class protect_timeController {
	@Autowired protect_timeRepository protect_times;
	
	//get all protect_time
	@RequestMapping(value= mapping.PROTECT_TIME_GET)
	public  List<protect_time> get(){
		List<protect_time> list_protect = protect_times.findAllByIdNot(1);
		return list_protect;
	}
	
	//create protect_time
	@RequestMapping(value = mapping.PROTECT_TIME_CREATE, method = RequestMethod.POST, consumes = {"application/json"})
	public ResponseEntity<?> create(@RequestBody protect_time p)
	{
	
		//check if name is already exists
		if(!protect_times.existsByName(p.getName()))
		{
			try {
				//if save sucessfully return save
				return new ResponseEntity<protect_time>( protect_times.save(p),HttpStatus.CREATED);
				
			}catch( Exception ex)
			{
				//if create false
				return ResponseEntity.status(400).body("Error! Please try again later");		
			}
		}
		else
		{
			//if exists return
			return ResponseEntity.status(404).body("Protect name already exists");		
			
		}
	}
	
	//edit protect_time
	@RequestMapping(value = mapping.PROTECT_TIME_EDIT, method = RequestMethod.POST, consumes = {"application/json"})
	public ResponseEntity<?> edit(@RequestBody protect_time p)
	{
		//find protect_time
		protect_time edit = protect_times.findById(p.getId());
		//check if name is same
		if((edit.getName()).equals(p.getName())) {
			//return if save sucess;
			return new ResponseEntity<protect_time>(protect_times.save(p),HttpStatus.CREATED);
		}
		else
		{
			//check if name is exists
			if(!protect_times.existsByName(p.getName())) {
				return new ResponseEntity<protect_time>(protect_times.save(p),HttpStatus.CREATED);
			}
			else
			{
				//if name is taken return with name = 0
				return ResponseEntity.status(304).body("Protect name is taken");
			}
		}
	}
	
	//delete protect_time
	@RequestMapping(value = mapping.PROTECT_TIME_DELETE, method = RequestMethod.POST)
	public @ResponseBody protect_time delete (@RequestBody int id)
	{
		//find protect_time
		protect_time delete = protect_times.findById(id);
		if(delete != null)
		{
			try {
				protect_times.deleteById(id);
				//if delete sucessfully return delete
				return delete;
			}catch (Exception ex)
			{
				//if cannot delete return with value = 0 
				delete.setValue(0);
				return delete;
			}
		}
		else
		{
			//return with id = 0 if not found
			return delete;
		}
	}
}

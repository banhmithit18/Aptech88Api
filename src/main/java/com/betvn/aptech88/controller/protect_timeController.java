package com.betvn.aptech88.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betvn.aptech88.model.protect_time;
import com.betvn.aptech88.repository.protect_timeRepository;

import ultis.mapping;

@Controller
public class protect_timeController {
	@Autowired protect_timeRepository protect_times;
	
	//create protect_time
	@RequestMapping(value = mapping.PROTECT_TIME_CREATE, method = RequestMethod.POST, consumes = {"application/json"})
	public @ResponseBody protect_time create(@RequestBody protect_time p)
	{
		protect_time pro = new protect_time();
		//check if name is already exists
		if(!protect_times.existsByName(p.getName()))
		{
			try {
				//if save sucessfully return save
				return protect_times.save(p);
				
			}catch( Exception ex)
			{
				//if create false return with id = 0 
				pro.setId(0);
				return pro;
			}
		}
		else
		{
			//if exists return with name = 0
			pro.setName("0");
			return pro;
			
		}
	}
	
	//edit protect_time
	public @ResponseBody protect_time edit(@RequestBody protect_time p)
	{
		//find protect_time
		protect_time edit = protect_times.findById(p.getId());
		//check if name is same
		if(edit.getName() == p.getName()) {
			//return if save sucess;
			return protect_times.save(p);
		}
		else
		{
			//check if name is exists
			if(!protect_times.existsByName(p.getName())) {
				return protect_times.save(p);
			}
			else
			{
				//if name is taken return with name = 0
				p.setName("0");
				return p;
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
				protect_times.delete(delete);
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

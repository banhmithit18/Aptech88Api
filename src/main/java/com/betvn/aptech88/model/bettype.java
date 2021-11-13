package com.betvn.aptech88.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity
@Table(name = "bettype")
public class bettype {
	@Id
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "status")
	private Boolean status;
	
	@OneToMany(mappedBy = "bettype")
	private List<odd> odd;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<odd> getOdd() {
		return odd;
	}


	public void setOdd(List<odd> odd) {
		this.odd = odd;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	
	
	
	
}

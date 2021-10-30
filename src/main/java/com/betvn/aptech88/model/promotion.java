package com.betvn.aptech88.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
@Table(name = "promotion")
public class promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	
	@Column(name = "value")
	private double value;
	
	@Column(name = "status")
	private Boolean status;
	
	
	@OneToMany(mappedBy = "promotion")
	private List<payment> payment;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public List<payment> getPayment() {
		return payment;
	}


	public void setPayment(List<payment> payment) {
		this.payment = payment;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	
}


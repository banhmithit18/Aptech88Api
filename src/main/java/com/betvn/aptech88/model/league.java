package com.betvn.aptech88.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
@Table(name = "league")
public class league {
	@Id
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "logo")
	private String logo;
	
	@Column(name = "status")
	private boolean status;
	
	@OneToMany(mappedBy = "league")
	private List<fixture> fixture;


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


	public List<fixture> getFixture() {
		return fixture;
	}


	public void setFixture(List<fixture> fixture) {
		this.fixture = fixture;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getLogo() {
		return logo;
	}


	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	
	
}


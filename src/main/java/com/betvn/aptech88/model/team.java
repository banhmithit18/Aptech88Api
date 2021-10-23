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
@Table(name = "team")
public class team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	
	@OneToMany(mappedBy = "homeTeam")
	private List<fixture> home;
	
	
	@OneToMany(mappedBy = "awayTeam")
	private List<fixture> away;


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


	public List<fixture> getHome() {
		return home;
	}


	public void setHome(List<fixture> home) {
		this.home = home;
	}


	public List<fixture> getAway() {
		return away;
	}


	public void setAway(List<fixture> away) {
		this.away = away;
	}
	
	
	
}

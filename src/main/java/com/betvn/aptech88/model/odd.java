package com.betvn.aptech88.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
@Table(name = "odd")
public class odd {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "bettype_id")
	private int bettypeId;
	
	@Column(name = "value")
	private double value;
	
	@Column(name = "fixture_id")
	private int fixtureId;
	
	
	@OneToMany(mappedBy = "odd")
	private List<betdetail> betdetail;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fixture_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private fixture fixture;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bettype_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private bettype bettype;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getBettypeId() {
		return bettypeId;
	}


	public void setBettypeId(int bettypeId) {
		this.bettypeId = bettypeId;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public int getFixtureId() {
		return fixtureId;
	}


	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}


	public List<betdetail> getBetdetail() {
		return betdetail;
	}


	public void setBetdetail(List<betdetail> betdetail) {
		this.betdetail = betdetail;
	}


	public fixture getFixture() {
		return fixture;
	}


	public void setFixture(fixture fixture) {
		this.fixture = fixture;
	}


	public bettype getBettype() {
		return bettype;
	}


	public void setBettype(bettype bettype) {
		this.bettype = bettype;
	}
	
	
	
}

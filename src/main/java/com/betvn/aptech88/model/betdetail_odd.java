package com.betvn.aptech88.model;

import java.sql.Date;


public class betdetail_odd {

	private Date date;
	private Boolean status;
	
	private int oddId;
	private int bettypeId;	
	private String value;
	private double oddValue;
	private int fixtureId;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public int getBettypeId() {
		return bettypeId;
	}
	public void setBettypeId(int bettypeId) {
		this.bettypeId = bettypeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public double getOddValue() {
		return oddValue;
	}
	public void setOddValue(double oddValue) {
		this.oddValue = oddValue;
	}
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}
	public int getOddId() {
		return oddId;
	}
	public void setOddId(int oddId) {
		this.oddId = oddId;
	}

	
	
	
}

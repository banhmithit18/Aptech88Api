package com.betvn.aptech88.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.Id;
;


@Entity
@Table(name = "bet_history")
public class bet_history {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "account_id")
	private int accountId;
	
	@Column(name = "fixture_id")
	private int fixtureId;
	
	@Column(name = "bet_id")
	private int bet;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getFixtureId() {
		return fixtureId;
	}

	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}
	
	
}


package com.betvn.aptech88.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
@Table(name = "wallet")
public class wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "account_id")
	private int accountId;
	
	@Column(name = "amount")
	private double amount;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private account account;
	
	
	@OneToMany(mappedBy = "wallet")
	private List<payment> payment;
	
	
	@OneToMany(mappedBy = "wallet")
	private List<bet> bet;
	
	
	@OneToMany(mappedBy = "from_wallet")
	private List<transaction> from_wallet;
	
	
	@OneToMany(mappedBy = "to_wallet")
	private List<transaction> to_wallet;


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


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public account getAccount() {
		return account;
	}


	public void setAccount(account account) {
		this.account = account;
	}


	public List<payment> getPayment() {
		return payment;
	}


	public void setPayment(List<payment> payment) {
		this.payment = payment;
	}


	public List<bet> getBet() {
		return bet;
	}


	public void setBet(List<bet> bet) {
		this.bet = bet;
	}


	public List<transaction> getFrom_wallet() {
		return from_wallet;
	}


	public void setFrom_wallet(List<transaction> from_wallet) {
		this.from_wallet = from_wallet;
	}


	public List<transaction> getTo_wallet() {
		return to_wallet;
	}


	public void setTo_wallet(List<transaction> to_wallet) {
		this.to_wallet = to_wallet;
	}
	
	
	
	
	
}


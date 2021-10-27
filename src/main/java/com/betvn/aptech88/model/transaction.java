package com.betvn.aptech88.model;

import java.sql.Date;

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


@Entity
@Table(name = "transaction")
public class transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "from_wallet")
	private int fromWallet;
	
	@Column(name = "to_wallet")
	private int toWallet;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "from_wallet", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private wallet from_wallet;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "to_wallet", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private wallet to_wallet;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getFromWallet() {
		return fromWallet;
	}


	public void setFromWallet(int fromWallet) {
		this.fromWallet = fromWallet;
	}


	public int getToWallet() {
		return toWallet;
	}


	public void setToWallet(int toWallet) {
		this.toWallet = toWallet;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Date getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}


	public wallet getFrom_wallet() {
		return from_wallet;
	}


	public void setFrom_wallet(wallet from_wallet) {
		this.from_wallet = from_wallet;
	}


	public wallet getTo_wallet() {
		return to_wallet;
	}


	public void setTo_wallet(wallet to_wallet) {
		this.to_wallet = to_wallet;
	}
	
	
	
}


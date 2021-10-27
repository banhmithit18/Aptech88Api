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
@Table(name = "bet")
public class bet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "wallet_id")
	private int walletId;
	
	@Column(name = "odd")
	private double odd;
	
	@Column(name = "bet_amount")
	private double betAmount;
	
	@Column(name = "win")
	private double win;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "returnable")
	private Boolean returnable;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wallet_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private wallet wallet;
	
	
	@OneToMany(mappedBy = "bet")
	private List<betdetail> betdetail;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getWalletId() {
		return walletId;
	}


	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}


	public double getOdd() {
		return odd;
	}


	public void setOdd(double odd) {
		this.odd = odd;
	}


	public double getBetAmount() {
		return betAmount;
	}


	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}


	public double getWin() {
		return win;
	}


	public void setWin(double win) {
		this.win = win;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Boolean getReturnable() {
		return returnable;
	}


	public void setReturnable(Boolean returnable) {
		this.returnable = returnable;
	}


	public wallet getWallet() {
		return wallet;
	}


	public void setWallet(wallet wallet) {
		this.wallet = wallet;
	}


	public List<betdetail> getBetdetail() {
		return betdetail;
	}


	public void setBetdetail(List<betdetail> betdetail) {
		this.betdetail = betdetail;
	}
	
	
	
	
}


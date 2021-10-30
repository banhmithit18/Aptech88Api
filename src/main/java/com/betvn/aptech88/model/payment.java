package com.betvn.aptech88.model;

import java.sql.Timestamp;

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
@Table(name="payment")
public class payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "payment_type")
	private String paymentType;
	
	@Column(name = "promotion_id")
	private int promotionId;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "wallet_id")
	private int walletId;
	
	@Column(name = "from_bank_account")
	private String formBankAccount;
	
	@Column(name = "to_bank_account")
	private String toBankAccount;
	
	@Column(name = "from_bank_name")
	private String fromBankName;
	
	@Column(name = "to_bank_name")
	private String toBankName;
	
	@Column(name = "payment_date")
	private Timestamp paymentDate;
	
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wallet_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private wallet wallet;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "promotion_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private promotion promotion;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public int getPromotionId() {
		return promotionId;
	}


	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public int getWalletId() {
		return walletId;
	}


	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}


	public String getFormBankAccount() {
		return formBankAccount;
	}


	public void setFormBankAccount(String formBankAccount) {
		this.formBankAccount = formBankAccount;
	}


	public String getToBankAccount() {
		return toBankAccount;
	}


	public void setToBankAccount(String toBankAccount) {
		this.toBankAccount = toBankAccount;
	}


	public String getFromBankName() {
		return fromBankName;
	}


	public void setFromBankName(String fromBankName) {
		this.fromBankName = fromBankName;
	}


	public String getToBankName() {
		return toBankName;
	}


	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}


	public wallet getWallet() {
		return wallet;
	}


	public void setWallet(wallet wallet) {
		this.wallet = wallet;
	}


	public promotion getPromotion() {
		return promotion;
	}


	public void setPromotion(promotion promotion) {
		this.promotion = promotion;
	}


	public Timestamp getPaymentDate() {
		return paymentDate;
	}


	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	
	
}



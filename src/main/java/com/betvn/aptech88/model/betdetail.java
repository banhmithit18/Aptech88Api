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
@Table(name = "betdetail")
public class betdetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "odd_id")
	private int oddId;
	
	@Column(name = "bet_id")
	private int betId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "status")
	private Boolean status;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bet_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private bet bet;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "odd_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private odd odd;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getOddId() {
		return oddId;
	}


	public void setOddId(int oddId) {
		this.oddId = oddId;
	}


	public int getBetId() {
		return betId;
	}


	public void setBetId(int betId) {
		this.betId = betId;
	}


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


	public bet getBet() {
		return bet;
	}


	public void setBet(bet bet) {
		this.bet = bet;
	}


	public odd getOdd() {
		return odd;
	}


	public void setOdd(odd odd) {
		this.odd = odd;
	}
	
	
	
}


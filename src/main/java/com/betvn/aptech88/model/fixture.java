package com.betvn.aptech88.model;

import java.sql.Date;
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
@Table(name = "fixture")
public class fixture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "leauge_id")
	private int leaugeId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "away")
	private int away;
	
	@Column(name = "home")
	private int home;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "result")
	private String result;
	
	@Column(name = "in_match")
	private Boolean inMatch;
	
	@Column(name = "red_card")
	private int redCard;
	
	@Column(name = "yellow_card")
	private int yellowCard;
	
	@Column(name = "foul")
	private int foul;
	
	@Column(name = "cornner")
	private int cornner;
	
	
	@OneToMany(mappedBy = "fixture")
	private List<odd> odd;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private leauge leauge;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private team homeTeam;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private team awayTeam;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getLeaugeId() {
		return leaugeId;
	}


	public void setLeaugeId(int leaugeId) {
		this.leaugeId = leaugeId;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getAway() {
		return away;
	}


	public void setAway(int away) {
		this.away = away;
	}


	public int getHome() {
		return home;
	}


	public void setHome(int home) {
		this.home = home;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public Boolean getInMatch() {
		return inMatch;
	}


	public void setInMatch(Boolean inMatch) {
		this.inMatch = inMatch;
	}


	public int getRedCard() {
		return redCard;
	}


	public void setRedCard(int redCard) {
		this.redCard = redCard;
	}


	public int getYellowCard() {
		return yellowCard;
	}


	public void setYellowCard(int yellowCard) {
		this.yellowCard = yellowCard;
	}


	public int getFoul() {
		return foul;
	}


	public void setFoul(int foul) {
		this.foul = foul;
	}


	public int getCornner() {
		return cornner;
	}


	public void setCornner(int cornner) {
		this.cornner = cornner;
	}


	public List<odd> getOdd() {
		return odd;
	}


	public void setOdd(List<odd> odd) {
		this.odd = odd;
	}


	public leauge getLeauge() {
		return leauge;
	}


	public void setLeauge(leauge leauge) {
		this.leauge = leauge;
	}


	public team getHomeTeam() {
		return homeTeam;
	}


	public void setHomeTeam(team homeTeam) {
		this.homeTeam = homeTeam;
	}


	public team getAwayTeam() {
		return awayTeam;
	}


	public void setAwayTeam(team awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	
	
	
	
	
	
}


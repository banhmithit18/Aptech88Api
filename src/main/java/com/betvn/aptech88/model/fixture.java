package com.betvn.aptech88.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

	private int id;
	
	@Column(name = "league_id")
	private int leagueId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "time")
	private Time time;
	
	@Column(name = "away")
	private int away;
	
	@Column(name = "home")
	private int home;
	
	@Column(name = "status")
	private Boolean status;
	
	
	@Column(name = "in_match")
	private Boolean inMatch;	
	
	
	@OneToMany(mappedBy = "fixture")
	private List<odd> odd;
	
	@OneToMany(mappedBy = "fixture")
	private List<fixture_detail> fixture_detail;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "league_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private league league;
	
	

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "home", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private team homeTeam;
	
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "away", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private team awayTeam;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getLeagueId() {
		return leagueId;
	}


	public void setLeagueId(int leaugeId) {
		this.leagueId = leaugeId;
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

	public Boolean getInMatch() {
		return inMatch;
	}


	public void setInMatch(Boolean inMatch) {
		this.inMatch = inMatch;
	}


	public List<odd> getOdd() {
		return odd;
	}


	public void setOdd(List<odd> odd) {
		this.odd = odd;
	}


	public league getLeauge() {
		return league;
	}


	public void setLeauge(league league) {
		this.league = league;
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


	public Time getTime() {
		return time;
	}


	public void setTime(Time time) {
		this.time = time;
	}



	
	
}


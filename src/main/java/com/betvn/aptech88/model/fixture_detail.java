package com.betvn.aptech88.model;




import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "fixture_detail")
public class fixture_detail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "fixture_id")
	private int fixtureId;
	
	@Column(name = "match_winner")
	private String matchWinner;
	
	@Column(name = "second_half_winner")
	private String secondHalfWinner ;
	
	@Column(name = "first_half_winner")
	private String firstHalfWinner ;
	
	@Column(name = "goal")
	private int goal ;
	
	@Column(name = "goal_first_half")
	private int goalFirstHalf ;
	
	@Column(name = "goal_second_half")
	private int goalSecondHalf;
	
	@Column(name = "clean_sheet_home")
	private Boolean cleanSheetHome ;
	
	@Column(name = "clean_sheet_away")
	private Boolean cleanSheetAway ;
	
	@Column(name = "both_team_score")
	private Boolean bothTeamScore ;
	
	@Column(name = "both_team_score_first_half")
	private Boolean bothTeamScoreFirstHalf ;
	
	@Column(name = "both_team_score_second_half")
	private Boolean bothTeamScoreSecondHalf ;
	
	@Column(name = "exact_score")
	private String exactScore ;
	
	@Column(name = "correct_score_first_half")
	private String correctScoreFirstHalf ;
	
	@Column(name = "correct_score_second_half")
	private String correctScoreSecondHalf ;
	
	@Column(name = "team_score_first")
	private String teamScoreFirst;
	
	@Column(name = "team_score_last")
	private String teamScoreLast ;
	
	@Column(name = "ten_min_winner")
	private String tenMinWinner ;
	
	@Column(name = "home_goal")
	private int homeGoal;
	
	@Column(name = "away_goal")
	private int awayGoal ;
	
	@Column(name = "total_corners")
	private int corners ;
	
	@Column(name = "away_corners")
	private int awayCorner ;
	
	@Column(name = "home_corners")
	private int homeCorner ;
	
	@Column(name = "first_corner")
	private String firstCorner ;
	
	@Column(name = "last_corner")
	private String lastCorner ;
	
	@Column(name = "total_corner_first_half")
	private int totalCornerFirstHalf;
	
	@Column(name = "total_corner_second_half")
	private int totalCornerSecondHalf ;
	
	@Column(name = "card")
	private int card ;
	
	@Column(name = "red_card")
	private int redCard ;
	
	@Column(name = "home_card")
	private int homeCard ;
	
	@Column(name = "away_card")
	private int awayCard ;
	
	@Column(name = "total_shot_on_goal")
	private int totalShotOnGoal ;
	
	@Column(name = "home_total_shot_on_goal")
	private int homeShotOnGoal ;
	
	@Column(name = "away_total_shot_on_goal")
	private int awayShotOnGoal ;
	
	@Column(name = "first_goal_method")
	private String firstGoalMethod ;
	
	@Column(name = "soccers")
	private String soccers ;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fixture_id", referencedColumnName = "id", insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private fixture fixture;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFixtureId() {
		return fixtureId;
	}

	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}

	public String getMatchWinner() {
		return matchWinner;
	}

	public void setMatchWinner(String matchWinner) {
		this.matchWinner = matchWinner;
	}

	public String getSecondHalfWinner() {
		return secondHalfWinner;
	}

	public void setSecondHalfWinner(String secondHalfWinner) {
		this.secondHalfWinner = secondHalfWinner;
	}

	public String getFirstHalfWinner() {
		return firstHalfWinner;
	}

	public void setFirstHalfWinner(String firstHalfWinner) {
		this.firstHalfWinner = firstHalfWinner;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public int getGoalFirstHalf() {
		return goalFirstHalf;
	}

	public void setGoalFirstHalf(int goalFirstHalf) {
		this.goalFirstHalf = goalFirstHalf;
	}

	public int getGoalSecondHalf() {
		return goalSecondHalf;
	}

	public void setGoalSecondHalf(int goalSecondHalf) {
		this.goalSecondHalf = goalSecondHalf;
	}

	public Boolean getCleanSheetHome() {
		return cleanSheetHome;
	}

	public void setCleanSheetHome(Boolean cleanSheetHome) {
		this.cleanSheetHome = cleanSheetHome;
	}

	public Boolean getCleanSheetAway() {
		return cleanSheetAway;
	}

	public void setCleanSheetAway(Boolean cleanSheetAway) {
		this.cleanSheetAway = cleanSheetAway;
	}

	public Boolean getBothTeamScore() {
		return bothTeamScore;
	}

	public void setBothTeamScore(Boolean bothTeamScore) {
		this.bothTeamScore = bothTeamScore;
	}

	public Boolean getBothTeamScoreFirstHalf() {
		return bothTeamScoreFirstHalf;
	}

	public void setBothTeamScoreFirstHalf(Boolean bothTeamScoreFirstHalf) {
		this.bothTeamScoreFirstHalf = bothTeamScoreFirstHalf;
	}

	public Boolean getBothTeamScoreSecondHalf() {
		return bothTeamScoreSecondHalf;
	}

	public void setBothTeamScoreSecondHalf(Boolean bothTeamScoreSecondHalf) {
		this.bothTeamScoreSecondHalf = bothTeamScoreSecondHalf;
	}

	public String getExactScore() {
		return exactScore;
	}

	public void setExactScore(String exactScore) {
		this.exactScore = exactScore;
	}

	public String getCorrectScoreFirstHalf() {
		return correctScoreFirstHalf;
	}

	public void setCorrectScoreFirstHalf(String correctScoreFirstHalf) {
		this.correctScoreFirstHalf = correctScoreFirstHalf;
	}

	public String getCorrectScoreSecondHalf() {
		return correctScoreSecondHalf;
	}

	public void setCorrectScoreSecondHalf(String correctScoreSecondHalf) {
		this.correctScoreSecondHalf = correctScoreSecondHalf;
	}

	public String getTeamScoreFirst() {
		return teamScoreFirst;
	}

	public void setTeamScoreFirst(String teamScoreFirst) {
		this.teamScoreFirst = teamScoreFirst;
	}

	public String getTeamScoreLast() {
		return teamScoreLast;
	}

	public void setTeamScoreLast(String teamScoreLast) {
		this.teamScoreLast = teamScoreLast;
	}

	public String getTenMinWinner() {
		return tenMinWinner;
	}

	public void setTenMinWinner(String tenMinWinner) {
		this.tenMinWinner = tenMinWinner;
	}

	public int getHomeGoal() {
		return homeGoal;
	}

	public void setHomeGoal(int homeGoal) {
		this.homeGoal = homeGoal;
	}

	public int getAwayGoal() {
		return awayGoal;
	}

	public void setAwayGoal(int awayGoal) {
		this.awayGoal = awayGoal;
	}

	public int getCorners() {
		return corners;
	}

	public void setCorners(int corners) {
		this.corners = corners;
	}

	public int getAwayCorner() {
		return awayCorner;
	}

	public void setAwayCorner(int awayCorner) {
		this.awayCorner = awayCorner;
	}

	public int getHomeCorner() {
		return homeCorner;
	}

	public void setHomeCorner(int homeCorner) {
		this.homeCorner = homeCorner;
	}

	public String getFirstCorner() {
		return firstCorner;
	}

	public void setFirstCorner(String firstCorner) {
		this.firstCorner = firstCorner;
	}

	public String getLastCorner() {
		return lastCorner;
	}

	public void setLastCorner(String lastCorner) {
		this.lastCorner = lastCorner;
	}

	public int getTotalCornerFirstHalf() {
		return totalCornerFirstHalf;
	}

	public void setTotalCornerFirstHalf(int totalCornerFirstHalf) {
		this.totalCornerFirstHalf = totalCornerFirstHalf;
	}

	public int getTotalCornerSecondHalf() {
		return totalCornerSecondHalf;
	}

	public void setTotalCornerSecondHalf(int totalCornerSecondHalf) {
		this.totalCornerSecondHalf = totalCornerSecondHalf;
	}

	public int getCard() {
		return card;
	}

	public void setCard(int card) {
		this.card = card;
	}

	public int getRedCard() {
		return redCard;
	}

	public void setRedCard(int readCard) {
		this.redCard = readCard;
	}

	public int getHomeCard() {
		return homeCard;
	}

	public void setHomeCard(int homeCrad) {
		this.homeCard = homeCrad;
	}

	public int getAwayCard() {
		return awayCard;
	}

	public void setAwayCard(int awayCard) {
		this.awayCard = awayCard;
	}

	public int getTotalShotOnGoal() {
		return totalShotOnGoal;
	}

	public void setTotalShotOnGoal(int totalShotOnGoal) {
		this.totalShotOnGoal = totalShotOnGoal;
	}

	public int getHomeShotOnGoal() {
		return homeShotOnGoal;
	}

	public void setHomeShotOnGoal(int homeShotOnGoal) {
		this.homeShotOnGoal = homeShotOnGoal;
	}

	public int getAwayShotOnGoal() {
		return awayShotOnGoal;
	}

	public void setAwayShotOnGoal(int awayShotOnGoal) {
		this.awayShotOnGoal = awayShotOnGoal;
	}

	public String getFirstGoalMethod() {
		return firstGoalMethod;
	}

	public void setFirstGoalMethod(String firstGoalMethod) {
		this.firstGoalMethod = firstGoalMethod;
	}

	public String getSoccers() {
		return soccers;
	}

	public void setSoccers(String soccers) {
		this.soccers = soccers;
	}

	public fixture getFixture() {
		return fixture;
	}

	public void setFixture(fixture fixture) {
		this.fixture = fixture;
	}

	
	
	
	
}

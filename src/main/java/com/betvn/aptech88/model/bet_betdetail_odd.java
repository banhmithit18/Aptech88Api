package com.betvn.aptech88.model;

import java.util.List;

public class bet_betdetail_odd {
   private int  walletId;
   private double betAmount;
   private List<betdetail_odd> betdetail_odds;
  
   
public int getWalletId() {
	return walletId;
}
public void setWalletId(int walletId) {
	this.walletId = walletId;
}
public double getBetAmount() {
	return betAmount;
}
public void setBetAmount(double betAmount) {
	this.betAmount = betAmount;
}
public List<betdetail_odd> getBetdetail_odds() {
	return betdetail_odds;
}
public void setBetdetail_odds(List<betdetail_odd> betdetail_odds) {
	this.betdetail_odds = betdetail_odds;
}
   
   
}

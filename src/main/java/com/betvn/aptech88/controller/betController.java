package com.betvn.aptech88.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.bet;
import com.betvn.aptech88.model.bet_betdetail_odd;
import com.betvn.aptech88.model.bet_history;
import com.betvn.aptech88.model.betdetail;
import com.betvn.aptech88.model.betdetail_odd;
import com.betvn.aptech88.model.bettype;
import com.betvn.aptech88.model.fixture;
import com.betvn.aptech88.model.fixture_detail;
import com.betvn.aptech88.model.odd;
import com.betvn.aptech88.model.transaction;
import com.betvn.aptech88.model.wallet;
import com.betvn.aptech88.repository.betRepository;
import com.betvn.aptech88.repository.bet_historyRepository;
import com.betvn.aptech88.repository.betdetailRepository;
import com.betvn.aptech88.repository.bettypeRepository;
import com.betvn.aptech88.repository.fixtureRepository;
import com.betvn.aptech88.repository.fixture_detailRepository;
import com.betvn.aptech88.repository.oddRepository;
import com.betvn.aptech88.repository.transactionRepository;
import com.betvn.aptech88.repository.walletRepository;

import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8000/")
@RestController
public class betController {

	@Autowired
	betRepository bets;
	@Autowired
	oddRepository odds;
	@Autowired
	bettypeRepository bettypes;
	@Autowired
	walletRepository wallets;
	@Autowired
	betdetailRepository betdetails;
	@Autowired
	fixtureRepository fixtures;
	@Autowired
	fixture_detailRepository fixture_details;
	@Autowired
	bet_historyRepository bet_histories;
	@Autowired
	transactionRepository transactions;

	// create
	// note satatus in betdetail mean returnable or not, if one of betdetail satatus
	// = false then bet will not returnable
	@RequestMapping(value = mapping.BET_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody bet_betdetail_odd bbo) {

		// check if bettype created
		if (checkBettype(bbo.getBetdetail_odds())) {

			// check if odd created
			List<betdetail_odd> bd = checkOdd(bbo.getBetdetail_odds());
			if (bd != null) {
				// find wallet
				wallet w = wallets.findById(bbo.getWalletId());
				if (w != null) {
					double amount_left = w.getAmount();
					double amount_bet = bbo.getBetAmount();
					if (amount_bet > amount_left) {
						return ResponseEntity.status(404).body("Balance insufficient");
					}					
					double odd_value = 1;
					// create bet (only need bet ammount, wallet with bet constructor)
					bet bet = new bet();
					bet.setBetAmount(bbo.getBetAmount());
					// set wallet
					bet.setWalletId(bbo.getWalletId());
					// false = not return yet
					bet.setReturnable(true);
					// set status = false mean not check win or lost yet
					bet.setStatus(false);
					bet = bets.save(bet);
					// creatte bettype
					for (betdetail_odd betdetail_odd : bd) {
						// plus odd_value
						odd_value = odd_value * betdetail_odd.getOddValue();
						// create bet detail
						betdetail betdetail = new betdetail();
						betdetail.setBetId(bet.getId());
						betdetail.setDate(betdetail_odd.getDate());
						betdetail.setOddId(betdetail_odd.getOddId());
						betdetail.setStatus(betdetail_odd.getStatus());
						// win = null mean not calculate yet
						betdetail.setWin(null);
						if (betdetail.getStatus() == false) {
							bet.setReturnable(false);
						}
						// set bet value
						betdetails.save(betdetail);
					}
					bet.setOdd(odd_value);
					bet = bets.save(bet);
					//change amount bet
					w.setAmount(amount_left - amount_bet);
					wallets.save(w);
					// create history bet
					bet_history bh = new bet_history();
					bh.setAccountId(w.getAccountId());
					bh.setBetId(bet.getId());
					// get now date
					LocalDate date = LocalDate.now();
					Date sqlDate = Date.valueOf(date);
					bh.setDate(sqlDate);
					bet_histories.save(bh);

					return ResponseEntity.status(200).body("Betted");

				} else {
					return ResponseEntity.status(404).body("Wallet not found");
				}

			}
			{
				return ResponseEntity.status(404).body("Error ! try again later");
			}
		} else {
			return ResponseEntity.status(404).body("Bet type not found");
		}
	}
	/// check bet

	@RequestMapping("BetResult")
	public String result() throws IOException, InterruptedException {
		// get all bet where status = false
		List<bet> bet_list = bets.findByStatusFalse();
		for (int i = 0; i < bet_list.size(); i++) {
			// get bet detail list
			List<betdetail> betdetail_list = bet_list.get(i).getBetdetail();
			for (int j = 0; j < betdetail_list.size(); j++) {
				int fixture_id = betdetail_list.get(j).getOdd().getFixtureId();
				if (checkFixtureDetail(fixture_id)) {
					betdetail_list.get(j).setWin(calculateResultBet(betdetail_list.get(j), fixture_id));
					betdetails.save(betdetail_list.get(j));
				} else {
					continue;
				}

			}

		}
		return "Calculated done!";

	}

	// return if win or not
	@RequestMapping("/BetReturn")
	public String returnBet() throws IOException, InterruptedException {
		result();
		List<bet> bet_list = bets.findByWin(0);
		for (int i = 0; i < bet_list.size(); i++) {
			// get bet detail list
			Boolean flag = true;
			List<betdetail> betdetail_list = bet_list.get(i).getBetdetail();

			for (int j = 0; j < betdetail_list.size(); j++) {
				if (betdetail_list.get(j).getWin() == null) {
					flag = false;
					break;

				}

				if (betdetail_list.get(j).getWin() == false) {
					double lossAmount = bet_list.get(i).getBetAmount();
					bet_list.get(i).setWin(-lossAmount);
				} else {
					continue;
				}

			}
			if (flag == false) {
				continue;
			}
			if (bet_list.get(i).getWin() == 0) {
				double winAmount = bet_list.get(i).getBetAmount() * bet_list.get(i).getOdd();
				bet_list.get(i).setWin(winAmount);
				// get wallet
				wallet w = bet_list.get(i).getWallet();
				// get admin wallet id
				wallet w_a = wallets.findById(1);
				w_a.setAmount(w_a.getAmount() - winAmount);
				w.setAmount(w.getAmount() + winAmount);
				wallets.save(w);
				wallets.save(w_a);
				// create transaction
				transaction t = new transaction();
				t.setAmount(winAmount);
				t.setFromWallet(1);
				t.setToWallet(w.getId());
				t.setReason("Return Win");
				t.setStatus(true);
				LocalDate d = LocalDate.now();
				Date date = Date.valueOf(d);
				t.setTransactionDate(date);
				transactions.save(t);
				bet_list.get(i).setStatus(true);
				bets.save(bet_list.get(i));

			}
			if (bet_list.get(i).getWin() < 0) {
				double lossAmount = bet_list.get(i).getBetAmount();
				bet_list.get(i).setWin(-lossAmount);
				// get wallet
				wallet w = bet_list.get(i).getWallet();
				// get admin wallet id
				wallet w_a = wallets.findById(1);
			
//				w.setAmount(w.getAmount() - Math.abs(lossAmount));
				w_a.setAmount(w_a.getAmount() + Math.abs(lossAmount));
				wallets.save(w);
				wallets.save(w_a);
				// create transaction
				transaction t = new transaction();
				t.setAmount(Math.abs(lossAmount));
				t.setFromWallet(w.getId());
				t.setToWallet(1);
				t.setReason("Return Loss");
				t.setStatus(true);
				LocalDate d = LocalDate.now();
				Date date = Date.valueOf(d);
				t.setTransactionDate(date);
				transactions.save(t);
				bet_list.get(i).setStatus(true);
				bets.save(bet_list.get(i));
			}

		}
		return "Returned";
	}

	public Boolean calculateResultBet(betdetail bt, int fixture_id) {

		int bettype_id = bt.getOdd().getBettypeId();
		String value = bt.getOdd().getValue();
		fixture_detail fd = fixture_details.findByFixtureId(fixture_id);

		switch (bettype_id) {

		// match winner
		case 1: {
			if (fd.getMatchWinner().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// second half winner
		case 3: {
			if (fd.getSecondHalfWinner().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// home/away
		case 2: {

			if (value.equals("Home")) {
				if (fd.getFirstHalfWinner().equals("Home") && fd.getSecondHalfWinner().equals("Away"))
					return true;

			} else {
				return false;
			}
			if (value.equals("Away")) {
				if (fd.getFirstHalfWinner().equals("Away") && fd.getSecondHalfWinner().equals(""))
					return true;
				else {
					return false;
				}
			}
		}

		// asian handicap
		case 4: {
			// get home or away
			String team = value.substring(0, 4);
			double handicap = Double.valueOf(value.substring(5));
			if (team.equals("Home")) {
				int away_goal = fd.getAwayGoal();
				double home_goal = fd.getHomeGoal() + handicap;
				if (home_goal > away_goal) {
					return true;
				} else {
					return false;
				}
			}
			if (team.equals("Away")) {
				double away_goal = fd.getAwayGoal() + handicap;
				int home_goal = fd.getHomeGoal();
				if (home_goal < away_goal) {
					return true;
				} else {
					return false;
				}
			}
		}

		// goal upder/over
		case 5: {
			double handicap = 0;
			if (value.contains("Over")) {
				handicap = Double.valueOf(value.substring(5));
				if (fd.getGoal() < handicap) {
					return true;
				} else {
					return false;
				}
			}
			if (value.contains("Under")) {
				handicap = Double.valueOf(value.substring(6));
				if (fd.getGoal() > handicap) {
					return true;
				} else {
					return false;
				}
			}
		}

		// goal over/under first half
		case 6: {
			double handicap = 0;
			if (value.contains("Over")) {
				handicap = Double.valueOf(value.substring(5));
				if (fd.getGoalFirstHalf() < handicap) {
					return true;
				} else {
					return false;
				}
			}
			if (value.contains("Under")) {
				handicap = Double.valueOf(value.substring(6));
				if (fd.getGoalFirstHalf() > handicap) {
					return true;
				} else {
					return false;
				}
			}
		}

		// HT/FT Double
		case 7: {
			// get result
			String result = fd.getFirstHalfWinner() + "/" + fd.getMatchWinner();
			if (result.equals(value)) {
				return true;
			} else {
				return false;
			}

		}

		// Both teams score
		case 8: {
			if (value.toLowerCase().equals(fd.getBothTeamScore().toString())) {
				return true;
			} else {
				return false;
			}
		}

		// handicap result
		case 9: {
			String team = value.substring(0, 4);
			double handicap = Double.valueOf(value.substring(5));
			if (team.equals("Home")) {
				int away_goal = fd.getAwayGoal();
				double home_goal = fd.getHomeGoal() + handicap;
				if (home_goal > away_goal) {
					return true;
				} else {
					return false;
				}
			}
			if (team.equals("Away")) {
				double away_goal = fd.getAwayGoal() + handicap;
				int home_goal = fd.getHomeGoal();
				if (home_goal < away_goal) {
					return true;
				} else {
					return false;
				}
			}
		}

		// exact score
		case 10: {
			if (fd.getExactScore().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// highest scroing half
		case 11: {
			int first_half_goal = fd.getGoalFirstHalf();
			int second_half_goal = fd.getGoalSecondHalf();
			String result = "";
			if (first_half_goal > second_half_goal) {
				result = "1st Half";
			} else if (first_half_goal < second_half_goal) {
				result = "2nd Half";
			} else {
				result = "Draw";
			}
			if (value.equals(result)) {
				return true;
			} else {
				return false;
			}
		}

		// Double Chance
		case 12: {
			// get chance
			String chance_first = value.substring(value.lastIndexOf("/") + 1);
			String chance_second = value.substring(0, value.lastIndexOf("/") - 1);

			if (fd.getMatchWinner().equals(chance_second) || fd.getMatchWinner().equals(chance_first)) {
				return true;
			}
			if (!fd.getMatchWinner().equals(chance_second) || !fd.getMatchWinner().equals(chance_first)) {
				return false;
			}
		}

		// First Half Winner
		case 13: {
			if (fd.getFirstHalfWinner().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// Team to Score First
		case 14: {
			if (fd.getTeamScoreFirst().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// Team to Score Last
		case 15: {
			if (fd.getTeamScoreLast().equals(value)) {
				return true;
			} else {
				return false;
			}
		}

		// Total - Home
		case 16: {
			double handicap = 0;
			if (value.contains("Over")) {
				handicap = Double.valueOf(value.substring(5));
				if (fd.getHomeGoal() < handicap) {
					return true;
				} else {
					return false;
				}
			}
			if (value.contains("Under")) {
				handicap = Double.valueOf(value.substring(6));
				if (fd.getHomeGoal() > handicap) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Total - Away
		case 17: {
			double handicap = 0;
			if (value.contains("Over")) {
				handicap = Double.valueOf(value.substring(5));
				if (fd.getAwayGoal() < handicap) {
					return true;
				} else {
					return false;
				}
			}
			if (value.contains("Under")) {
				handicap = Double.valueOf(value.substring(6));
				if (fd.getAwayGoal() > handicap) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Handicap Result - First Half
		case 18: {
			String team = value.substring(0, 4);
			double handicap = Double.valueOf(value.substring(5));
			if (team.equals("Home")) {
				int away_goal = fd.getAwayGoalFirstHalf();
				double home_goal = fd.getHomeGoalFirstHalf() + handicap;
				if (home_goal > away_goal) {
					return true;
				} else {
					return false;
				}
			}
			if (team.equals("Away")) {
				double away_goal = fd.getAwayGoalFirstHalf() + handicap;
				int home_goal = fd.getHomeGoalFirstHalf();
				if (home_goal < away_goal) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Asian Handciap First Half
		case 19: {
			String team = value.substring(0, 4);
			double handicap = Double.valueOf(value.substring(5));
			if (team.equals("Home")) {
				int away_goal = fd.getAwayGoalFirstHalf();
				double home_goal = fd.getHomeGoalFirstHalf() + handicap;
				if (home_goal > away_goal) {
					return true;
				} else {
					return false;
				}
			}
			if (team.equals("Away")) {
				double away_goal = fd.getAwayGoalFirstHalf() + handicap;
				int home_goal = fd.getHomeGoalFirstHalf();
				if (home_goal < away_goal) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Double Chance - First Half
		case 20: {
			// get chance
			String chance_first = value.substring(value.lastIndexOf("/") + 1);
			String chance_second = value.substring(0, value.lastIndexOf("/") - 1);

			if (fd.getFirstHalfWinner().equals(chance_second) || fd.getFirstHalfWinner().equals(chance_first)) {
				return true;
			}
			if (!fd.getFirstHalfWinner().equals(chance_second) || !fd.getFirstHalfWinner().equals(chance_first)) {
				return false;
			}
		}

		// Odd/Even
		case 21: {
			if (value.equals("Even")) {
				if (fd.getGoal() % 2 == 0) {
					return true;
				} else {
					return false;
				}
			} else if (value.equals("Odd")) {
				if (fd.getGoal() % 2 != 0) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Odd/Even - First Half
		case 22: {
			if (value.equals("Even")) {
				if (fd.getGoalFirstHalf() % 2 == 0) {
					return true;
				} else {
					return false;
				}
			} else if (value.equals("Odd")) {
				if (fd.getGoalFirstHalf() % 2 != 0) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Home Odd/Even
		case 23: {
			if (value.equals("Even")) {
				if (fd.getHomeGoal() % 2 == 0) {
					return true;
				} else {
					return false;
				}
			} else if (value.equals("Odd")) {
				if (fd.getHomeGoal() % 2 != 0) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Results/Both Teams Score
		case 24: {
			String result = "";
			if (fd.getBothTeamScore()) {
				result = fd.getMatchWinner() + "/" + "Yes";
			} else {
				result = fd.getMatchWinner() + "/" + "No";
			}
			if (value.equals(result)) {
				return true;
			} else {
				return false;
			}

		}

		// Results/Total Goals
		case 25: {
			double handicap = 0;
			if (value.contains("Over")) {
				handicap = Double.valueOf(value.substring(10));
				if (fd.getGoal() < handicap && value.substring(0, value.indexOf("/") - 1).equals(fd.getMatchWinner())) {
					return true;
				} else {
					return false;
				}
			}
			if (value.contains("Under")) {
				handicap = Double.valueOf(value.substring(11));
				if (fd.getGoal() > handicap && value.substring(0, value.indexOf("/") - 1).equals(fd.getMatchWinner())) {
					return true;
				} else {
					return false;
				}
			}
		}

		// Goals Over/Under - Second Half
		case 26: {

		}
		}
		return false;
	}

	// cannot get method goal, first , second half corner, first/last corner
	public boolean checkFixtureDetail(int fixtureId) throws IOException, InterruptedException {
		//check fixture inMatch
		checkInMatch(fixtureId);
		// check if fixture is start
		fixture fixture = fixtures.findById(fixtureId);
		if (fixture.getInMatch() == true) {

			fixture_detail fde = fixture_details.findByFixtureId(fixtureId);
			if (fde != null) {
				return true;
			} else {
				// init variable
				String matchWinner = "Draw";
				String secondHalfWinner = "Draw";
				String firstHalfWinner = "Draw";
				int goal = 0;
				int goalFirstHalf = 0;
				int goalSecondHalf = 0;
				Boolean cleanSheetHome = true;
				Boolean cleanSheetAway = true;
				Boolean bothTeamScore = false;
				Boolean bothTeamScoreFirstHalf = false;
				Boolean bothTeamScoreSecondHalf = false;
				String exactScore = "0:0";
				String correctScoreFirstHalf = "0:0";
				String correctScoreSecondHalf = "0:0";
				String teamScoreFirst = "Draw";
				String teamScoreLast = "No goal";
				String tenMinWinner = "Draw";
				int homeGoal = 0;
				int awayGoal = 0;
				int corners = 0;
				int awayCorner = 0;
				int homeCorner = 0;
				String firstCorner = "None";
				String lastCorner = "None";
				int totalCornerFirstHalf = 0;
				int totalCornerSecondHalf = 0;
				int card = 0;
				int redCard = 0;
				int homeCard = 0;
				int awayCard = 0;
				int totalShotOnGoal = 0;
				int homeShotOnGoal = 0;
				int awayShotOnGoal = 0;
				String firstGoalMethod = "None";
				List<String> soccers = new ArrayList<String>();
				List<String> team_soccers = new ArrayList<String>();
				int home_goal_first_half = 0;
				int away_goal_first_half = 0;
				int home_goal_second_half = 0;
				int away_goal_second_half = 0;
				int away_ten_min_goal = 0;
				int home_ten_min_goal = 0;

				// find fixure
				fixture f = fixtures.findById(fixtureId);
				// get away id
				int away_id = f.getAway();
				// get home id
				int home_id = f.getHome();
				// call api
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(
								"https://api-football-v1.p.rapidapi.com/v3/fixtures/events?fixture=" + fixtureId))
						.header("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
						.header("x-rapidapi-key", mapping.API_KEY).method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpResponse<String> response = HttpClient.newHttpClient().send(request,
						HttpResponse.BodyHandlers.ofString());

				// get json from api
				String jsonString = response.body();
				// convert to json object
				JSONObject obj = new JSONObject(jsonString);
				// get array child of response
				JSONArray arr = obj.getJSONArray("response");

				for (int i = 0; i < arr.length(); i++) {

					int time = arr.getJSONObject(i).getJSONObject("time").getInt("elapsed");
					int team_id = arr.getJSONObject(i).getJSONObject("team").getInt("id");
					String type = arr.getJSONObject(i).getString("type");
					String detail = arr.getJSONObject(i).getString("detail");
					String player = arr.getJSONObject(i).getJSONObject("player").getString("name");

					switch (type) {
					case "Card": {

						if (detail.equals("Red Card") || detail.equals("Second Yellow card")) {
							redCard++;
						}
						if (team_id == home_id) {
							card++;
							homeCard++;

						}
						if (team_id == away_id) {
							card++;
							awayCard++;
						}

					}
						break;
					case "Goal": {
						if (detail.equals("Normal Goal") || detail.equals("Penalty")) {
							if (time <= 45) {
								if (team_id == home_id) {
									cleanSheetAway = false;
									goal++;
									goalFirstHalf++;
									home_goal_first_half++;
									if (time < 10) {
										home_ten_min_goal++;
									}
								}
								if (team_id == away_id) {
									cleanSheetHome = false;
									goal++;
									goalFirstHalf++;
									away_goal_first_half++;
									if (time < 10) {
										away_ten_min_goal++;
									}
								}
							}
							if (time > 45) {
								if (team_id == home_id) {
									cleanSheetAway = false;
									goal++;
									goalSecondHalf++;
									home_goal_second_half++;
								}
								if (team_id == away_id) {
									cleanSheetHome = false;
									goal++;
									goalSecondHalf++;
									away_goal_second_half++;
								}
							}
						}
						if (detail.equals("Own Goal")) {
							if (time <= 45) {
								if (team_id == home_id) {
									cleanSheetHome = false;
									goal++;
									goalFirstHalf++;
									away_goal_first_half++;
									if (time < 10) {
										away_ten_min_goal++;
									}
								}
								if (team_id == away_id) {
									cleanSheetAway = false;
									goal++;
									goalFirstHalf++;
									home_goal_first_half++;
									if (time < 10) {
										home_ten_min_goal++;
									}
								}

							}
							if (time > 45) {
								if (team_id == home_id) {
									cleanSheetHome = false;
									goal++;
									goalSecondHalf++;
									away_goal_second_half++;
								}
								if (team_id == away_id) {
									cleanSheetAway = false;
									goal++;
									goalSecondHalf++;
									home_goal_second_half++;
								}
							}

						}
						if (!detail.equals("Missed Penalty")) {
							soccers.add(player);

							if (team_id == away_id) {
								team_soccers.add("Away");
							}

							if (team_id == home_id) {
								team_soccers.add("Home");
							}
						}
					}
					}

				}

				// calculate first half winner
				if (away_goal_first_half > home_goal_first_half) {
					firstHalfWinner = "Away";
				} else if (away_goal_first_half < home_goal_first_half) {
					firstHalfWinner = "Home";
				}

				// calculate second half winner
				if (away_goal_second_half > home_goal_second_half) {
					secondHalfWinner = "Away";
				} else if (away_goal_second_half < home_goal_second_half) {
					secondHalfWinner = "Home";
				}

				// calculate if both team scores first half
				if (home_goal_first_half != 0 && away_goal_first_half != 0) {
					bothTeamScoreFirstHalf = true;
				}

				// calculate if both team scores second half
				if (home_goal_second_half != 0 && away_goal_second_half != 0) {
					bothTeamScoreSecondHalf = true;
				}
				// calculate home goal
				homeGoal = home_goal_first_half + home_goal_second_half;
				// calculate away goal
				awayGoal = away_goal_first_half + away_goal_second_half;
				// calculate if both team socore
				if (homeGoal != 0 && awayGoal != 0) {
					bothTeamScore = true;
				}
				// calculate matchWinner
				if (homeGoal > awayGoal) {
					matchWinner = "Home";
				} else if (homeGoal < awayGoal) {
					matchWinner = "Away";
				}
				// calculate exactScore
				exactScore = homeGoal + ":" + awayGoal;
				// calculate ScoreFirstHalf
				correctScoreFirstHalf = home_goal_first_half + ":" + away_goal_first_half;
				// calculate ScoreSecondHalf
				correctScoreSecondHalf = home_goal_second_half + ":" + away_goal_second_half;
				// calculate team score last
				try {
					teamScoreLast = team_soccers.get(team_soccers.size() - 1);
				} catch (Exception ex) {
					teamScoreLast = "No goal";
				}
				// calculate team score first
				try {
					teamScoreFirst = team_soccers.get(0);
				} catch (Exception ex) {
					teamScoreFirst = "Draw";
				}
				// calculate ten minute winner
				if (away_ten_min_goal > home_ten_min_goal) {
					tenMinWinner = "Away";
				}

				// calculate if both team scores second half
				else if (away_ten_min_goal < home_ten_min_goal) {
					tenMinWinner = "Home";
				}
				// get stat
				HttpRequest request_event = HttpRequest.newBuilder()
						.uri(URI.create(
								"https://api-football-v1.p.rapidapi.com/v3/fixtures/statistics?fixture=" + fixtureId))
						.header("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
						.header("x-rapidapi-key", mapping.API_KEY).method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpResponse<String> response_event = HttpClient.newHttpClient().send(request_event,
						HttpResponse.BodyHandlers.ofString());
				// get json from api
				String json_string_stat = response_event.body();
				// convert to json object
				JSONObject obj_stat = new JSONObject(json_string_stat);
				// get array child of response
				JSONArray arr_stat = obj_stat.getJSONArray("response");

				for (int j = 0; j < arr_stat.length(); j++) {
					{
						int team_stat_id = arr_stat.getJSONObject(j).getJSONObject("team").getInt("id");
						JSONArray arr_stat_detail = arr_stat.getJSONObject(j).getJSONArray("statistics");
						for (int k = 0; k < arr_stat_detail.length(); k++) {
							String type_stat = arr_stat_detail.getJSONObject(k).getString("type");

							if (type_stat.equals("Shots on Goal")) {
								String string_shot_on_goal = arr_stat_detail.getJSONObject(k).get("value").toString();
								if (team_stat_id == home_id) {

									homeShotOnGoal = string_shot_on_goal.equals("null") ? 0
											: Integer.parseInt(string_shot_on_goal);
								}
								if (team_stat_id == away_id) {
									awayShotOnGoal = string_shot_on_goal.equals("null") ? 0
											: Integer.parseInt(string_shot_on_goal);
								}
							}
							if (type_stat.equals("Corner Kicks")) {
								String string_corner = arr_stat_detail.getJSONObject(k).get("value").toString();
								if (team_stat_id == home_id) {

									homeCorner = string_corner.equals("null") ? 0 : Integer.parseInt(string_corner);
								}
								if (team_stat_id == away_id) {
									awayCorner = string_corner.equals("null") ? 0 : Integer.parseInt(string_corner);
								}
							}
						}

					}

				}
				// calculate corner
				corners = homeCorner + awayCorner;
				totalShotOnGoal = homeShotOnGoal + awayShotOnGoal;
				// set value for constructor
				fixture_detail fd = new fixture_detail();
				fd.setFixtureId(fixtureId);
				fd.setMatchWinner(matchWinner);
				fd.setSecondHalfWinner(secondHalfWinner);
				fd.setFirstHalfWinner(firstHalfWinner);
				fd.setGoal(goal);
				fd.setGoalFirstHalf(goalFirstHalf);
				fd.setGoalSecondHalf(goalSecondHalf);
				fd.setCleanSheetHome(cleanSheetHome);
				fd.setCleanSheetAway(cleanSheetAway);
				fd.setBothTeamScore(bothTeamScore);
				fd.setBothTeamScoreFirstHalf(bothTeamScoreFirstHalf);
				fd.setBothTeamScoreSecondHalf(bothTeamScoreSecondHalf);
				fd.setExactScore(exactScore);
				fd.setCorrectScoreFirstHalf(correctScoreFirstHalf);
				fd.setCorrectScoreSecondHalf(correctScoreSecondHalf);
				fd.setTeamScoreFirst(teamScoreFirst);
				fd.setTeamScoreLast(teamScoreLast);
				fd.setTenMinWinner(tenMinWinner);
				fd.setHomeGoal(homeGoal);
				fd.setAwayGoal(awayGoal);
				fd.setCorners(corners);
				fd.setAwayCorner(awayCorner);
				fd.setHomeCorner(homeCorner);
				fd.setCard(card);
				fd.setRedCard(redCard);
				fd.setHomeCard(homeCard);
				fd.setAwayCard(awayCard);
				fd.setTotalShotOnGoal(totalShotOnGoal);
				fd.setHomeShotOnGoal(homeShotOnGoal);
				fd.setAwayShotOnGoal(awayShotOnGoal);
				fd.setHomeGoalFirstHalf(home_goal_first_half);
				fd.setHomeGoalSecondHalf(home_goal_second_half);
				fd.setAwayGoalFirstHalf(away_goal_first_half);
				fd.setAwayGoalSecondHalf(away_goal_second_half);
				fd.setSoccers(soccers.toString());
				// save
				fixture_details.save(fd);
				return true;
			}
		} else {
			return false;
		}
	}

	public void checkInMatch(int fixtureId) {
		fixture fix = fixtures.findById(fixtureId);

			// get date, time match start
			LocalDate date_match =  fix.getDate().toLocalDate();
			LocalTime time_match =  fix.getTime().toLocalTime();
			// convert into date time
			LocalDateTime date_time_match = date_match.atTime(time_match);
			// setting UTC as the timezone
			ZoneId zonedUTC = ZoneId.of("UTC");
			// date time now
			LocalDateTime date_time_now = LocalDateTime.now(zonedUTC);
			// date match finish
			long hourBetween = ChronoUnit.HOURS.between(date_time_match.plusMinutes(120), date_time_now);
			if(hourBetween > 0)
			{
				fix.setInMatch(true);
				fixtures.save(fix);
			}

		
	}

	public boolean checkBettype(List<betdetail_odd> bo) {
		for (betdetail_odd betdetail_odd : bo) {
			bettype bt = bettypes.findById(betdetail_odd.getBettypeId());
			if (bt != null) {

				continue;
			} else {
				return false;

			}
		}
		return true;

	}

	public List<betdetail_odd> checkOdd(List<betdetail_odd> bo) {
		for (betdetail_odd betdetail_odd : bo) {
			fixture f = fixtures.findById(betdetail_odd.getFixtureId());
			if (f == null) {
				return null;
			}
			odd o = odds.findByValueAndBettypeIdAndOddValueAndFixtureId(betdetail_odd.getValue(), betdetail_odd.getBettypeId(),
					betdetail_odd.getOddValue(),f.getId());

			if (o != null) {
				betdetail_odd.setOddId(o.getId());
				continue;
			} else {
				try {
					odd created = new odd();
					created.setBettypeId(betdetail_odd.getBettypeId());
					created.setFixtureId(betdetail_odd.getFixtureId());
					
					created.setOddValue(betdetail_odd.getOddValue());
					created.setValue(betdetail_odd.getValue());
					created = odds.save(created);
					betdetail_odd.setOddId(created.getId());
				} catch (Exception ex) {
					return null;
				}
			}
		}
		return bo;
	}
}

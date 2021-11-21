package com.betvn.aptech88.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.fixture;
import com.betvn.aptech88.model.league;
import com.betvn.aptech88.model.team;
import com.betvn.aptech88.repository.fixtureRepository;
import com.betvn.aptech88.repository.leagueRepository;
import com.betvn.aptech88.repository.teamRepository;


import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8000/")
@RestController
public class fixtureController {

	@Autowired
	fixtureRepository fixtures;
	@Autowired
	teamRepository teams;
	@Autowired
	leagueRepository leagues;

	// create fixture

	// get
	@RequestMapping(value = mapping.FIXTURE_GET, method = RequestMethod.GET, consumes = { "application/json" })
	public ResponseEntity<?> get() {
		List<fixture> list_fixture =fixtures.findAll();
		return new ResponseEntity<List<fixture>>(list_fixture, HttpStatus.OK);

	}
	//get with id league
	@RequestMapping(value = mapping.FIXTURE_GET, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> get(@RequestBody league l) {
		List<fixture> list_fixture =fixtures.findAllByLeagueId(l.getId());
		return new ResponseEntity<List<fixture>>(list_fixture, HttpStatus.OK);

	}
	//get with id league android
	@RequestMapping(value = "getfixture")
	public List<fixture> get_fixture(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		List<fixture> list_fixture =fixtures.findAllByLeagueId(id);
		return list_fixture;
	}

	// not recommend use because of api
	@RequestMapping(value = mapping.FIXTURE_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody fixture f) {
		int id = fixtures.getLastId() + 1;
		f.setId(id);
		return new ResponseEntity<fixture>(fixtures.save(f), HttpStatus.OK);

	}

	// edit fixture
	@RequestMapping(value = mapping.FIXTURE_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> edit(@RequestBody fixture f) {
		// find fixture
		fixture fix = fixtures.findById(f.getId());
		if (fix != null) {
			return new ResponseEntity<fixture>(fixtures.save(f), HttpStatus.OK);
		} else {
			return ResponseEntity.status(499).body("Not found");
		}
	}

	// delete
	@RequestMapping(value = mapping.FIXTURE_DELETE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> delete(@RequestBody fixture f) {
		// find fixture
		fixture fix = fixtures.findById(f.getId());
		if (fix != null) {
			try {
				fixtures.delete(fix);
				return new ResponseEntity<fixture>(fix, HttpStatus.OK);
			} catch (Exception ex) {
				return ResponseEntity.status(499).body("Cannot delete because it is currently used");
			}

		} else {
			return ResponseEntity.status(499).body("Not found");
		}
	}

	// check league is exists

	public Boolean checkLeague(int league_id, JSONObject ob) {
		// check if league exists
		if (leagues.existsById(league_id)) {
			return true;
		} else {
			// if league not exists then create
			// get league name
			String league_name = ob.get("name").toString();
			// get logo
			String logo = ob.get("logo").toString();
			// check null for leauge name
			if (!league_name.equals("null")) {
				league l = new league();
				l.setName(league_name);
				l.setLogo(logo);
				l.setId(league_id);
				l.setStatus(true);
				// try to save league if fail pass
				try {
					leagues.save(l);
					return true;
				} catch (Exception ex) {
					return false;

				}
			} else {
				// pass if null
				return false;
			}
		}
	}

	// check team is exists
	public Boolean checkTeam(int home_id, int away_id, JSONObject ob) {
		// check if team is exists
		if (teams.existsById(away_id) && teams.existsById(home_id)) {
			return true;
		} else {
			// find out which team not exists
			if (!teams.existsById(home_id)) {
				// if away team is exists , create home team
				String home_name = ob.getJSONObject("home").get("name").toString();
				String home_logo = ob.getJSONObject("home").get("logo").toString();
				// check null for home name
				if (!home_name.equals("null")) {
					team t = new team();
					t.setLogo(home_logo);
					t.setId(home_id);
					t.setName(home_name);
					// try to save team if fail then pass
					try {
						teams.save(t);

					} catch (Exception ex) {
						return false;
					}
				} else {
					// if null pass
					return false;
				}
			}
			if (!teams.existsById(away_id)) {
				// if home team is exists , create away team
				String away_name = ob.getJSONObject("away").get("name").toString();
				// get logo
				String away_logo = ob.getJSONObject("away").get("logo").toString();
				// check null for home name
				if (!away_name.equals("null")) {
					team t = new team();
					t.setLogo(away_logo);
					t.setId(away_id);
					t.setName(away_name);
					// try to save team if fail then pass
					try {
						teams.save(t);
					} catch (Exception ex) {
						return false;
					}
				} else {
					// if null pass
					return false;
				}
			}
		}
		return true;
	}

	// update fixture from date to date
	// json {"fromDate" :"yyyy-MM-dd", "toDate" :"yyyy-MM-dd" , "status"
	// :"True/False"} must not null
	// status if you want to set all match status, null mean false
	// time is UTC + 0 // note
	// note only update if match has not begun
	@RequestMapping(value = mapping.FIXTURE_UPDATE, method = RequestMethod.POST, consumes = { "application/json" })
	public String update(@RequestBody String inputJson) {
		JSONObject ob = new JSONObject(inputJson);
		String from_date_string = ob.get("fromDate").toString();
		String to_date_string = ob.get("toDate").toString();
		Boolean status = false;
		try {
			status = (Boolean) ob.get("status");
		} catch (Exception ex) {
			status = false;
		}
		if (from_date_string.equals("null") || to_date_string.equals("null")) {
			return "Wrong date format";
		}
		// String to LocalDate
		LocalDate from_date = LocalDate.parse(from_date_string);
		LocalDate to_date = LocalDate.parse(to_date_string);
		// get day between
		long day_between = ChronoUnit.DAYS.between(from_date, to_date);
		if (day_between < 0) {
			return "End date must be greater than start date!";
		} else {
			for (int i = 0; i < day_between+1; i++) {
				String date = (from_date.plusDays(i)).toString();
				String url = "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + date;
				try {
					// call api get leauge
					HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
							.header("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
							.header("x-rapidapi-key", mapping.API_KEY)
							.method("GET", HttpRequest.BodyPublishers.noBody()).build();
					HttpResponse<String> response = HttpClient.newHttpClient().send(request,
							HttpResponse.BodyHandlers.ofString());

					// get json from api
					String jsonString = response.body();
					// convert to json object
					JSONObject obj = new JSONObject(jsonString);
					// get array child of response
					JSONArray arr = obj.getJSONArray("response");
					for (int j = 0; j < arr.length(); j++) {
						// get league id
						int league_id = arr.getJSONObject(j).getJSONObject("league").getInt("id");
						// get fixture id
						int fixture_id = arr.getJSONObject(j).getJSONObject("fixture").getInt("id");
						// get away id team
						int away_id = arr.getJSONObject(j).getJSONObject("teams").getJSONObject("away").getInt("id");
						// get home id team
						int home_id = arr.getJSONObject(j).getJSONObject("teams").getJSONObject("home").getInt("id");

						// check if fixture exists
						if (!fixtures.existsById(fixture_id)) {
							// get json object of teams
							JSONObject obTeam = arr.getJSONObject(j).getJSONObject("teams");
							// get json object of league
							JSONObject obLeague = arr.getJSONObject(j).getJSONObject("league");
							// check if league is exists
							if (checkLeague(league_id, obLeague) && checkTeam(home_id, away_id, obTeam)) {
								// get date
								LocalDate dt = from_date.plusDays(i);

								// convert to sql
								Date d = Date.valueOf(dt);
								// get date time string
								String date_time_string = arr.getJSONObject(j).getJSONObject("fixture")
										.getString("date");
								// split time
								String time_string = date_time_string.substring(date_time_string.lastIndexOf("T") + 1,
										date_time_string.lastIndexOf("+") - 1);
								// convert to sql time
								Time time = Time.valueOf(time_string);					
								fixture t = new fixture();

								
									t.setId(fixture_id);
									t.setInMatch(false);
									t.setHome(0);
									t.setTime(time);
									t.setStatus(status);
									t.setStatus(true);
									t.setDate(d);
									t.setAway(away_id);
									t.setHome(home_id);
									t.setLeagueId(league_id);
									t.setTime(time);
									t.setDate(d);
									fixtures.save(t);
								
							} else {
								// if 1 or both function above return fail then pass
								continue;
							}
						} else {
							continue;
						}
					}
					// return
				} catch (IOException ioe) {
					return ioe.getMessage();
				} catch (InterruptedException iex) {
					return iex.getMessage();
				}
			}
		}
		return "updated";
	}
	//get with id fixture
	@RequestMapping(value = "/GetFixtureById", method = RequestMethod.POST, consumes = { "application/json" })
	public fixture getFixtureById(@RequestBody fixture f) {
		fixture fixture =fixtures.findById(f.getId());
		return fixture;

	}
}

package com.betvn.aptech88.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.betvn.aptech88.model.league;
import com.betvn.aptech88.model.team;
import com.betvn.aptech88.repository.leagueRepository;
import com.betvn.aptech88.repository.teamRepository;

import ultis.mapping;

@RestController
public class teamController {
@Autowired teamRepository teams;
@Autowired leagueRepository leagues;
//create leauge
	@RequestMapping(value = mapping.TEAM_CREATE, method = RequestMethod.POST, consumes = { "application/json" })

	public ResponseEntity<?> Create(@RequestBody team t) {
		// check if name is not duplicated
		if (!teams.existsByName(t.getName())) {
			//get last id
			int id = teams.getLastId()+1;
			t.setId(id);
			team tea = teams.save(t);
			return new ResponseEntity<>(tea, HttpStatus.CREATED);

		} else {

			return ResponseEntity.status(499).body("Team name already exists");

		}
	}

	// get team
	@RequestMapping(value = mapping.TEAM_GET)
	public List<team> get() {
		List<team> leauge_list = teams.findAll();
		return leauge_list;
	}

	// edit leauge
	@RequestMapping(value = mapping.TEAM_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> Edit(@RequestBody team t) {
		// find team
		team tea = teams.findById(t.getId());
		// if not found return status 404
		if (tea == null) {
			return ResponseEntity.status(404).body("Team not found");
		}
		// check if team has been renamed
		if (!t.getName().equals(tea.getName())) {
			// check if name not duplicatated
			if (!teams.existsByName(t.getName())) {
				tea.setName(t.getName());
				return new ResponseEntity<team>(teams.save(tea), HttpStatus.CREATED);

			} else {
				// if duplicated return
				return ResponseEntity.status(304).body("Team name is duplicated");

			}
		} else {
			// return team with
			return ResponseEntity.status(304).body("Nothing change");


		}
	}

	// delete
	@RequestMapping(value = mapping.TEAM_DELETE, method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody team t) {
		// find leauge
		team tea = teams.findById(t.getId());
		if (tea == null) {
			// return with id =
			return ResponseEntity.status(404).body("Team not found");
		}
		try {
			teams.delete(tea);
			return new ResponseEntity<team>(tea, HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.status(409).body("Cannot delete this team beacause it is used");

		}
	}

	// update all team
	// call api to get current leauge, call once week
	@RequestMapping(value = mapping.TEAM_UPDATE, method = RequestMethod.GET)
	public String update() {
		//get all leauge
		List<league> league_list = leagues.findAll();
		//get current season
		int year = Calendar.getInstance().get(Calendar.YEAR);
		//call api to get team of each league
		for (league league : league_list) {
			//get league id 
			int league_id = league.getId();
			
			try {
				// call api get team
				String url = "https://api-football-v1.p.rapidapi.com/v3/teams?league="+league_id+"&season="+year;
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(url))
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
					// get id
					int team_id = arr.getJSONObject(i).getJSONObject("team").getInt("id");
					if (!teams.existsById(league_id)) {
						// create constructor team
						team t = new team();
						// get league name
						String team_name = arr.getJSONObject(i).getJSONObject("team").getString("name");
						//get logo
						String logo =  arr.getJSONObject(i).getJSONObject("team").getString("logo");
						// set value
						t.setLogo(logo);
						t.setId(team_id);
						t.setName(team_name);
						// save to database
						teams.save(t);

					} else {
						continue;
					}

				}
				// return
				
			} catch (IOException ioe) {			
				return ioe.getMessage();
			} catch (InterruptedException iex) {
				return iex.getMessage();
			}catch(org.json.JSONException ex)
			{
				return ex.getMessage();
			}
		}
		return "updated";

	}
	// find team by name
	@RequestMapping(value= mapping.TEAM_FIND, method = RequestMethod.POST, consumes= ("application/json"))
	public ResponseEntity<?> find (@RequestBody team tea)
	{
		List<team> t = teams.findAllByNameContaining(tea.getName());
		if(t != null)
		{
			return new ResponseEntity<List<team>>(t,HttpStatus.OK);
		}
		else
		{
			return ResponseEntity.status(404).body("league not found");
		}
		
	}	

}

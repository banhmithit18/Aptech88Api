package com.betvn.aptech88.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
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

import com.betvn.aptech88.model.league;
import com.betvn.aptech88.repository.leagueRepository;

import ultis.mapping;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
public class leagueController {
	@Autowired
	leagueRepository leauges;

	// create league
	@RequestMapping(value = mapping.LEAGUE_CREATE, method = RequestMethod.POST, consumes = { "application/json" })

	public ResponseEntity<?> Create(@RequestBody league l) {
		// check if name is not duplicated
		if (!leauges.existsByName(l.getName())) {
			// get last id
			int id = leauges.getLastId() + 1;
			l.setId(id);
			league lea = leauges.save(l);
			return new ResponseEntity<>(lea, HttpStatus.CREATED);

		} else {

			return ResponseEntity.status(499).body("league name already exists");

		}
	}

	// get league
	@RequestMapping(value = mapping.LEAGUE_GET)
	public List<league> get() {
		List<league> leauge_list = leauges.findAll();
		return leauge_list;
	}

	// get league by status true
	@RequestMapping(value = mapping.LEAGUE_GET, method = RequestMethod.POST)
	public List<league> getStatusTrue(Boolean Status) {
		List<league> leauge_list = leauges.findAllByStatusTrue();
		return leauge_list;
	}
	
	
	// get league by status true
		@RequestMapping("/LeagueTop")
		public List<league> getTopLeague() {
			List<league> league_list = new ArrayList<league>();
			int array_id [] = { 39, 140, 135, 61, 88, 253,17,18,2,12,16,3,848,5 };
			for (int i : array_id) {
				league l = leauges.findById(i);
				league_list.add(l);
			}
			return league_list;
		}

	// edit league
	@RequestMapping(value = mapping.LEAGUE_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> Edit(@RequestBody league l) {
		// find leauge
		league lea = leauges.findById(l.getId());
		// if not found return status 404
		if (lea == null) {
			return ResponseEntity.status(404).body("League not found");
		}
		// check if leauge has been renamed
		if (!l.getName().equals(lea.getName())) {
			// check if name not duplicatated
			if (!leauges.existsByName(l.getName())) {
				lea.setName(l.getName());
				lea.setStatus(l.isStatus());
				return new ResponseEntity<league>(leauges.save(lea), HttpStatus.CREATED);

			} else {
				// if duplicated return
				return ResponseEntity.status(304).body("league name is duplicated");

			}
		} else {
			// return leauge with changed status
			lea.setStatus(l.isStatus());
			return new ResponseEntity<league>(leauges.save(lea), HttpStatus.CREATED);

		}
	}

	// delete
	@RequestMapping(value = mapping.LEAGUE_DELETE, method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody league lea) {
		// find leauge
		league l = leauges.findById(lea.getId());
		if (l == null) {
			// return with id =
			return ResponseEntity.status(404).body("Leauge not found");
		}
		try {
			leauges.delete(l);
			return new ResponseEntity<league>(l, HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.status(409).body("Cannot delete this league beacause it is used");

		}
	}

	// update all league
	// call api to get current leauge
	@RequestMapping(value = mapping.LEAGUE_UPDATE, method = RequestMethod.GET)
	public String update() {
		try {
			// call api get leauge
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/leagues?current=true"))
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
				int league_id = arr.getJSONObject(i).getJSONObject("league").getInt("id");
				if (!leauges.existsById(league_id)) {
					// create constructor leauge
					league l = new league();
					// get league name
					String league_name = arr.getJSONObject(i).getJSONObject("league").getString("name");
					//get logo
					String logo = arr.getJSONObject(i).getJSONObject("league").getString("logo");
					// set value
					l.setId(league_id);
					l.setName(league_name);
					l.setLogo(logo);
					l.setStatus(true);
					// save to database
					leauges.save(l);

				} else {
					continue;
				}

			}
			// return
			return "updated";
		} catch (IOException ioe) {
			return ioe.getMessage();
		} catch (InterruptedException iex) {
			return iex.getMessage();
		}

	}

	// find league by name and status
	@RequestMapping(value = mapping.LEAGUE_FIND, method = RequestMethod.POST, consumes = ("application/json"))
	public ResponseEntity<?> find(@RequestBody league lea) {
		if (!lea.isStatus()) {
			List<league> l = leauges.findAllByNameContaining(lea.getName());
			if (l != null) {
				return new ResponseEntity<List<league>>(l, HttpStatus.OK);
			} else {
				return ResponseEntity.status(404).body("league not found");
			}
		} else {
			List<league> l = leauges.findAllByNameContainingAndStatusTrue(lea.getName());
			if (l != null) {
				return new ResponseEntity<List<league>>(l, HttpStatus.OK);
			} else {
				return ResponseEntity.status(404).body("league not found");
			}
		}
	}

}

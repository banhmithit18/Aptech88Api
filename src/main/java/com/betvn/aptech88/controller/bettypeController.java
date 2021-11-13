package com.betvn.aptech88.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

import com.betvn.aptech88.model.bettype;
import com.betvn.aptech88.repository.bettypeRepository;

import ultis.mapping;

@RestController
public class bettypeController {
	@Autowired
	bettypeRepository bettypes;

	// create
	@RequestMapping(value = mapping.BETTYPE_CREATE, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody bettype bt) {
		// check if name exists
		if (!bettypes.existsByName(bt.getName())) {
			int id = bettypes.getLastId() + 1;
			// save
			bt.setId(id);
			return new ResponseEntity<bettype>(bettypes.save(bt), HttpStatus.OK);
		} else {
			return ResponseEntity.status(499).body("Bettype name already exists");
		}
	}

	// get
	@RequestMapping(value = mapping.BETTYPE_GET, method = RequestMethod.GET)
	public List<bettype> get() {
		List<bettype> list_bettype = bettypes.findAll();
		return list_bettype;
	}

	// get by status true
	@RequestMapping(value = mapping.BETTYPE_GET, method = RequestMethod.POST)
	public List<bettype> get(Boolean status) {
		List<bettype> list_bettype = bettypes.findAllByStatusTrue();
		return list_bettype;
	}

	// edit
	@RequestMapping(value = mapping.BETTYPE_EDIT, method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> edit(@RequestBody bettype bt) {
		// find
		bettype t = bettypes.findById(bt.getId());
		if (t != null) {
			// check if reaname
			if (!t.getName().equals(bt.getName())) {
				if (!bettypes.existsByName(bt.getName())) {
					// set name
					t.setStatus(bt.getStatus());
					t.setName(bt.getName());
					// return
					return new ResponseEntity<bettype>(bettypes.save(t), HttpStatus.OK);
				} else {
					return ResponseEntity.status(499).body("Bettype name already exists");
				}
			} else {
				t.setStatus(bt.getStatus());
				return new ResponseEntity<bettype>(bettypes.save(t), HttpStatus.OK);
			}

		} else {
			return ResponseEntity.status(499).body("Not found");
		}
	}

	// delete
	@RequestMapping(value = mapping.BETTYPE_DELETE, method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody bettype t) {
		// find leauge
		bettype bt = bettypes.findById(t.getId());
		if (bt == null) {
			// return with id =
			return ResponseEntity.status(404).body("Not found");
		}
		try {
			bettypes.delete(bt);
			return new ResponseEntity<bettype>(bt, HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.status(409).body("Cannot delete this bet beacause it is used");

		}
	}

	// find team by name
	@RequestMapping(value = mapping.BETTYPE_FIND, method = RequestMethod.POST, consumes = ("application/json"))
	public ResponseEntity<?> find(@RequestBody bettype t) {
		// find

		if (!t.getStatus()) {
			List<bettype> bt = bettypes.findAllByNameContaining(t.getName());

			return new ResponseEntity<List<bettype>>(bt, HttpStatus.OK);

		} else {
			List<bettype> bt = bettypes.findAllByNameContainingAndStatusTrue(t.getName());

			return new ResponseEntity<List<bettype>>(bt, HttpStatus.OK);

		}
	}

	@RequestMapping(value = mapping.BETTYPE_UPDATE)
	public String update() {
		try {
			// call api get leauge
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api-football-v1.p.rapidapi.com/v3/odds/bets"))
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
				int bettype_id = arr.getJSONObject(i).getInt("id");
				if (!bettypes.existsById(bettype_id)) {
					// create constructor leauge
					bettype bt = new bettype();			
					if (!arr.getJSONObject(i).get("name").toString().equals("null")) {
						String bettype_name = arr.getJSONObject(i).getString("name");
						// set value
						bt.setId(bettype_id);
						bt.setName(bettype_name);
						bt.setStatus(true);
						// save to database
						bettypes.save(bt);
					}

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
}

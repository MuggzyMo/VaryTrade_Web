package com.VaryTrade.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VaryTrade.Dao.Misc.AppMiscRepository;
import com.VaryTrade.Model.Company;

@RestController
public class MiscApiController {
	@Autowired
	private AppMiscRepository appMiscRepository;
	
	@GetMapping("/api/company")
	public ResponseEntity<Company> retrieveCompany() {
		return new ResponseEntity<>(appMiscRepository.retrieveCompany(), HttpStatus.OK);
	}
	
	@GetMapping("/api/states")
	public ResponseEntity<ArrayList<String>> retrieveStates() {
		return new ResponseEntity<>(appMiscRepository.retrieveStates(), HttpStatus.OK);
	}
}

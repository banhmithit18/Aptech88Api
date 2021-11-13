package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.fixture;


@Repository
public interface fixtureRepository extends JpaRepository<fixture, Integer> {
	
	@Query(value ="SELECT MAX(id) FROM aptech88.fixture", nativeQuery = true)
	int getLastId();
	
	fixture findById(int id);
	
	List<fixture> findAllByLeagueId(int leagueId);
	
}

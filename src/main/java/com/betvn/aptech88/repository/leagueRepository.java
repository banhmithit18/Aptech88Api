package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.league;


@Repository
public interface leagueRepository extends JpaRepository<league, Integer> {
	Boolean existsByName ( String name);
	league findById(int id);
	Boolean existsById ( int id);
	List<league> findAllByNameContaining ( String name);
	List<league> findAllByNameContainingAndStatusTrue(String name);
	List<league> findAllByStatusTrue();
	@Query(value = "SELECT MAX(id) FROM aptech88.league",nativeQuery = true)
	int getLastId();
	
}

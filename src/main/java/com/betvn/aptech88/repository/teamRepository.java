package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.team;


@Repository
public interface teamRepository extends JpaRepository<team, Integer> {
	team findById(int id);
	team findByName(String name);
	Boolean existsById(int id);
	Boolean existsByName(String name);
	
	@Query(value ="SELECT MAX(id) FROM aptech88.team", nativeQuery = true)
	int getLastId();	
	List<team> findAllByNameContaining(String name);
	
}

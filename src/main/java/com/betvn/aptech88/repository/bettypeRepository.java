package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.bettype;


@Repository
public interface bettypeRepository extends JpaRepository<bettype, Integer> {
	bettype findById(int id);
	bettype findByName(String name);
	List<bettype> findAllByStatusTrue();
	Boolean existsById(int id);
	Boolean existsByName(String name);
	@Query(value = "SELECT MAX(id) FROM aptech88.bettype",nativeQuery = true)
	int getLastId();
	List<bettype> findAllByNameContaining(String name);
	List<bettype> findAllByNameContainingAndStatusTrue(String name);
}

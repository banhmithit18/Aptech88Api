package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.protect_time;


@Repository
public interface protect_timeRepository extends JpaRepository<protect_time, Integer> {
	
}

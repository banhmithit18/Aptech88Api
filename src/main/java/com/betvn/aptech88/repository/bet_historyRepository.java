package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.bet_history;


@Repository
public interface bet_historyRepository extends JpaRepository<bet_history, Integer> {
	
}

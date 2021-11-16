package com.betvn.aptech88.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.bet_history;



@Repository
public interface bet_historyRepository extends JpaRepository<bet_history, Integer> {

	bet_history findById(int id);
	List<bet_history> findByAccountId(int accountId);
	
	List<bet_history> findByDateBetween(Date start, Date end);
}

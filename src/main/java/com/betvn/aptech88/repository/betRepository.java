package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.bet;


@Repository
public interface betRepository extends JpaRepository<bet, Integer> {
	List<bet> findByStatusFalse();
	List<bet> findByWinIsNull();
	List<bet> findByWin(double win);
}

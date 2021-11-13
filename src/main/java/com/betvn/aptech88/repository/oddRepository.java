package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.odd;


@Repository
public interface oddRepository extends JpaRepository<odd, Integer> {
	odd findById(int id);
	odd findByValueAndBettypeIdAndOddValue(String value, int bettypeId, double oddValue);
}

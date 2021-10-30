package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.promotion;


@Repository
public interface promotionRepository extends JpaRepository<promotion, Integer> {
	promotion findById(int id);
	
}

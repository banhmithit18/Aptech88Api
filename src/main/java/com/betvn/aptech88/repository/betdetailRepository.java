package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.betdetail;


@Repository
public interface betdetailRepository extends JpaRepository<betdetail, Integer> {
	
}

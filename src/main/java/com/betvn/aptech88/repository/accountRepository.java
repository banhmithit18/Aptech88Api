package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.account;



@Repository
public interface accountRepository extends JpaRepository<account, Integer> {
	
}

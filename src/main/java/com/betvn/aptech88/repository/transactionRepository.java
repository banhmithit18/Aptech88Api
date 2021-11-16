package com.betvn.aptech88.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.transaction;


@Repository
public interface transactionRepository extends JpaRepository<transaction, Integer> {
	
	transaction findById(int id);
	List<transaction> findByFromWallet(int fromWallet);
	List<transaction> findByToWallet(int toWallet);
	List<transaction> findByReason(String reason);
	List<transaction> findByStatus(boolean status);
	List<transaction> findByTransactionDateBetween(Date start, Date end);

	
}

package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.wallet;


@Repository
public interface walletRepository extends JpaRepository<wallet, Integer> {
	wallet findById(int id);
	wallet findByAccountId( int accountId);

}

package com.betvn.aptech88.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.account;



@Repository
public interface accountRepository extends JpaRepository<account, Integer> {	
	Boolean existsByEmail(String email);
	Boolean existsByPhonenumber(String phonenumber);
	Boolean existsByUsername(String username);
    account findByVerifiedCode(String verifiedCode);
    account findById(int id);
    account findByUsername(String username);
    account findByWalletId(int walletId);
    account findByPhonenumber(String phonenumber);
    account findByEmail(String email);
    List<account> findByUsernameNot(String username );
   
}

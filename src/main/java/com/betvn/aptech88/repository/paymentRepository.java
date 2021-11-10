package com.betvn.aptech88.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.betvn.aptech88.model.payment;


public interface paymentRepository extends JpaRepository<payment, Integer> {
	List<payment> findAllByWalletId( int walletId);

	@Query(value="select * from payment where wallet_id = ?1 order by(id) desc limit ?2, ?3",nativeQuery = true)
	List<Object[]> findPaymentPagination(int walletId, int skip, int take);

	@Query(value="select * from payment where payment_date >= ?1 AND payment_date <= ?2 AND wallet_id = ?5 order by(id) desc limit ?3, ?4",nativeQuery = true)
	List<Object[]> filterDate(String fromDate, String toDate, int skip, int take, int wallet_id);

	@Query(value="select * from payment where payment_date >= ?1 AND payment_date <= ?2 AND wallet_id = ?3 order by(id) desc",nativeQuery = true)
	List<Object[]> countOfFilter(String fromDate, String toDate, int wallet_id);
}

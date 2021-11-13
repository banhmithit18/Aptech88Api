package com.betvn.aptech88.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvn.aptech88.model.fixture_detail;


@Repository
public interface fixture_detailRepository extends JpaRepository<fixture_detail, Integer> {
	fixture_detail findByFixtureId(int fixtureId);
}

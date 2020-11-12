package com.oracle.eot.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.eot.dao.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
	public List<History> findByCid(int cid);
}
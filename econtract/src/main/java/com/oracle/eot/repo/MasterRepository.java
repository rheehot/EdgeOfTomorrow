package com.oracle.eot.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.eot.dao.History;
import com.oracle.eot.dao.Master;

@Repository
public interface MasterRepository extends JpaRepository<Master, Integer> {
	public List<Master> findByRequestEmail(String requestEmail);
	public List<Master> findByApproveEmail(String approveEmail);
	public Master findByUuid(String uuid);
}
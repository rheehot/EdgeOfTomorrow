package com.oracle.eot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.eot.dao.ContractStatus;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus, Integer> {
}
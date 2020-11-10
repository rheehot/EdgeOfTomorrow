package com.oracle.eot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.eot.dao.Master;

@Repository
public interface MasterRepository extends JpaRepository<Master, Integer> {

}
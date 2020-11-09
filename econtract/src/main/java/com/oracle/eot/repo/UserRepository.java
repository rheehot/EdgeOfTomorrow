package com.oracle.eot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.eot.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
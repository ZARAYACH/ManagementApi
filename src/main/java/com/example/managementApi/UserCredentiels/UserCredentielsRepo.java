package com.example.managementApi.UserCredentiels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentielsRepo extends JpaRepository<UserCredentials,Integer> {
}

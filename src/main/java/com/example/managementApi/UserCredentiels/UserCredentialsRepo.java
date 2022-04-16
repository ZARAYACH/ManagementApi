package com.example.managementApi.UserCredentiels;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface
UserCredentialsRepo extends JpaRepository<UserCredentials,Integer> {

    UserCredentials findByEmail(String email);

    UserCredentials getByEmail(String email);
}

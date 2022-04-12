package com.example.managementApi.signInLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LogsRepo extends JpaRepository<Logs , Integer> {
    List<Logs> findAllByUserId(int userId);
}

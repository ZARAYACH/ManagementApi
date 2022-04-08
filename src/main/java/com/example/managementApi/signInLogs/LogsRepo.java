package com.example.managementApi.signInLogs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepo extends JpaRepository<Logs,Integer> {
}

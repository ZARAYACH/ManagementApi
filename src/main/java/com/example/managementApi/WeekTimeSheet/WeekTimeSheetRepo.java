package com.example.managementApi.WeekTimeSheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WeekTimeSheetRepo extends JpaRepository<WeekTimeSheet, Integer> {
    @Query(value = "SELECT * FROM week_time_sheet where user_id =:userId",nativeQuery = true)
    List<WeekTimeSheet> getAllByUserId(Integer userId);

    boolean existsByWeekIdAndAndUserId(Integer weekId,Integer userID);
}

package com.example.managementApi.WeekTimeSheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeekTimeSheetRepo extends JpaRepository<WeekTimeSheet, Integer> {
    @Query(value = "SELECT * FROM week_time_sheet where user_id =:userId", nativeQuery = true)
    List<WeekTimeSheet> getAllByUserId(Integer userId);

    boolean existsByWeekIdAndAndUserId(Integer weekId, Integer userID);

    @Query(value = "select weeks.weekId from (select week_id as weekId,(select distinct day.full_date from day,week_time_sheet where(select beginning_of_week from week_time_sheet where week_id = weekId) = day.id ) as beginingOfWeek ,(select distinct day.full_date from day,week_time_sheet where (select end_of_week from week_time_sheet where week_id = weekId) = day.id ) as endOfWeek from week_time_sheet where user_id=:userId)as weeks where beginingOfWeek <=:dayDate and endOfWeek>=:daydate ;", nativeQuery = true)
    WeekTimeSheet getWeekTimeSheetByDayAndUserId(LocalDate dayDate, int userId);

//    @Query(value = "SELECT * FROM week_time_sheet  ",nativeQuery = true)
//    ResponseEntity<?> getWeekTimeSheetByUserIdLimetd(int id, int page,int number);
}

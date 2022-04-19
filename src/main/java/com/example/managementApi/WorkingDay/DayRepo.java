package com.example.managementApi.WorkingDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
@Repository
public interface DayRepo extends JpaRepository<Day, Integer> {

    @Query(value = "select * from Day where user_id =:userId and is_worked= true and is_approved = true "  , nativeQuery = true)
    List<Day> getAllApprovedWorkedDaysWithId(int userId);

    @Query(value = "SELECT * FROM day WHERE user_id = :userId and full_date >=:beginningOfWeek and full_date<=:endOfWeek ",nativeQuery = true)
    List<Day> getDaysOfWeekByUserId(Integer userId, LocalDate beginningOfWeek,LocalDate endOfWeek);

    @Query(value = "SELECT * FROM day WHERE full_date = :date and user_id = :userId",nativeQuery = true)
    Day getDayByUserIdAndDate(LocalDate date, Integer userId);

    @Query(value = "SELECT * FROM day WHERE is_worked = true AND user_id = :userId",nativeQuery = true)
    List<Day> getAllWorkedDaysWithId(int userId);

    boolean existsByFullDateAndUserId(LocalDate fullDate, int id);

    boolean existsByIdAndUserId(int id,int userId);

    List<Day> getAllByUserId(int employeeId);
}
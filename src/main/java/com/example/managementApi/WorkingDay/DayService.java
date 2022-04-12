package com.example.managementApi.WorkingDay;

import com.example.managementApi.User.UserRepo;

import com.example.managementApi.WeekTimeSheet.WeekTimeSheetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DayService {

    private final DayRepo dayRepo;
    private final UserRepo userRepo;
    private final WeekTimeSheetService weekTimeSheetService;

    public Day addDay(Day day) {
        if (userRepo.existsById(day.getUser().getId())) {
            Day result = null;
            if (!dayRepo.existsByFullDateAndUserId(day.getFullDate(), day.getUser().getId())) {
                day.setApproved(false);
                if (day.getNumberHours() >= 1) {
                    day.setWorked(true);
                    if (day.getNumberHours() > 8) {
                        day.setOverTimed(true);
                        day.setUnWorkedNHours(0);
                        day.setUnWorkedHoursReason(null);
                        result = dayRepo.save(day);
                    } else if (day.getNumberHours() < 8) {
                        day.setOverTimed(false);
                        if (day.getNumberHours() + day.getUnWorkedNHours() == 8) {
                            result = dayRepo.save(day);
                        } else {
                            throw new IllegalStateException("it is required to have 8hours per day even if its not worked");
                        }
                    } else if (day.getNumberHours() == 8) {
                        day.setOverTimed(false);
                        day.setUnWorkedHoursReason(null);
                        day.setUnWorkedNHours(0);
                        result = dayRepo.save(day);
                    }
                }
                if (day.getFullDate().getDayOfWeek().getValue() == 5) {
                    weekTimeSheetService.AddWeekTimeSheetInFriday(day);
                }
                return result;
            } else throw new IllegalStateException("this day already declared");
        } else throw new IllegalStateException("this user doesn't exist");

    }

    public List<Day> getAllApprovedWorkedDays(int userId) {
        if (userRepo.existsById(userId)) {
            return dayRepo.getAllApprovedWorkedDaysWithId(userId);
        } else {
            throw new IllegalStateException("this user doesn't exit");
        }

    }

    public List<Day> getAllWorkedDays(int userId) {

        return dayRepo.getAllWorkedDaysWithId(userId);
    }



}



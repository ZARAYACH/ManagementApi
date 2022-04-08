package com.example.managementApi.WorkingDay;
import com.example.managementApi.User.UserRepo;

import com.example.managementApi.WeekTimeSheet.WeekTimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import java.util.List;

@Service
public class DayService {

    private final DayRepo dayRepo;
    private final UserRepo userRepo ;
    private final WeekTimeSheetService weekTimeSheetService;
    @Autowired
    public DayService(DayRepo dayRepo, UserRepo userRepo, WeekTimeSheetService weekTimeSheetService) {
        this.dayRepo = dayRepo;
        this.userRepo = userRepo;
        this.weekTimeSheetService = weekTimeSheetService;
    }


    public Day addDay(Day day) {
        if (userRepo.existsById(day.getUser().getId())) {
            Day result ;
            if (!dayRepo.existsByFullDateAndUserId(day.getFullDate(),day.getUser().getId())){
                int hours = (int)ChronoUnit.HOURS.between(day.getBeginTime(),day.getEndTime());
                if (hours == day.getNumberHours()){
                    day.setApproved(false);
                    if (day.isOverTimed()) {
                        if (hours > 8) {
                            result = dayRepo.save(day);
                        } else {
                            throw new IllegalStateException("if it overtime must be more than 8 hours");
                        }
                    } else {
                        result = dayRepo.save(day);
                    }
                    if(day.getFullDate().getDayOfWeek().getValue() == 5){
                        weekTimeSheetService.AddWeekTimeSheetInFriday(day);
                    }
                    return result;
                }else throw new IllegalStateException("the difference between the two times should be identical to number Of hours");
            }else throw new IllegalStateException("this day already declared");

        } else throw new IllegalStateException("this user doesn't exist");

        }

    private int diffBetweenTwoTime(Day day) {
        LocalTime begin = day.getBeginTime();
        LocalTime end = day.getEndTime();
        int dif = (int) ChronoUnit.HOURS.between(begin,end);
        System.out.println(dif);
        return dif;
    }

    public List<Day> getAllApprovedWorkedDays(int userId) {
        if (userRepo.existsById(userId)) {
            return dayRepo.getAllApprovedWorkedDaysWithId(userId);
        }else{
            throw new IllegalStateException("this user doesn't exit");
        }

    }

    public List<Day> getAllWorkedDays(int userId) {
        return dayRepo.getAllWorkedDaysWithId(userId);
    }
}



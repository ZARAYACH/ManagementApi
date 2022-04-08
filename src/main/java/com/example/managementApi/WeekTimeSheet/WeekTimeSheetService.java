package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.User.User;
import com.example.managementApi.WorkingDay.Day;
import com.example.managementApi.WorkingDay.DayRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;

@Service
public class WeekTimeSheetService {

    private final WeekTimeSheetRepo weekTimeSheetRepo;
    private final DayRepo dayRepo;

    public WeekTimeSheetService(WeekTimeSheetRepo weekTimeSheetRepo, DayRepo dayRepo) {
        this.weekTimeSheetRepo = weekTimeSheetRepo;
        this.dayRepo = dayRepo;
    }


    public List<WeekTimeSheet> getAllWeeklyTimeSheets(Integer userId) {
        return weekTimeSheetRepo.getAllByUserId(userId);
    }

    public WeekTimeSheet AddWeekTimeSheetInFriday(Day day){
        if(dayRepo.existsById(day.getId())){
            LocalDate fullDate = day.getFullDate();
            int dayInWeekNumber = fullDate.getDayOfWeek().getValue();
            if (dayInWeekNumber == 5){
                LocalDate BeginningOfWeek = fullDate.minusDays(4);
                LocalDate endOfWeek = fullDate.plusDays(2);
                List<Day> daysOfweek= dayRepo.getDaysOfWeekByUserId(day.getUser().getId(),BeginningOfWeek,endOfWeek);

                var weekTimeSheet = new WeekTimeSheet();

                weekTimeSheet.setBeginningOfWeek(dayRepo.getDayByUserIdAndDate(BeginningOfWeek,day.getUser().getId()));
                weekTimeSheet.setEndOfWeek(dayRepo.getDayByUserIdAndDate(endOfWeek,day.getUser().getId()));
                weekTimeSheet.setTotalNhours(calculateTotalHoursWorked(daysOfweek));
                weekTimeSheet.setUser(day.getUser());

                return weekTimeSheetRepo.save(weekTimeSheet);

            }else{
                throw new IllegalStateException("We can't add a week time sheet at this time,until end of week");
            }
        }else{
            throw new IllegalStateException("there is no declare worked day with this id ");
        }


    }

    private int calculateTotalHoursWorked(List<Day> daysOfweek) {
        int total=0;
        for (Day day : daysOfweek) {
            if (day.isWorked()) {
                total = total + day.getNumberHours();
            }
        }
      return total;

    }

    public WeekTimeSheet addWeekTimeSheet(WeekTimeSheet weekTimeSheet) {

        Day beginnigOfweek = weekTimeSheet.getBeginningOfWeek();
        Day endingOfweek = weekTimeSheet.getEndOfWeek();
        if (ChronoUnit.DAYS.between(beginnigOfweek.getFullDate(),endingOfweek.getFullDate())==7){
            User user = weekTimeSheet.getUser();
            List<Day> weekDays = dayRepo.getDaysOfWeekByUserId(user.getId(), beginnigOfweek.getFullDate(),endingOfweek.getFullDate());
            int total = calculateTotalHoursWorked(weekDays);

            WeekTimeSheet weekTimeSheet1 = new WeekTimeSheet();

            weekTimeSheet1.setBeginningOfWeek(beginnigOfweek);
            weekTimeSheet1.setEndOfWeek(endingOfweek);
            weekTimeSheet1.setUser(user);
            weekTimeSheet1.setTotalNhours(total);

            return weekTimeSheetRepo.save(weekTimeSheet1);
        }else{
            throw new IllegalStateException("you must have 7 days in your week");
        }



    }
}

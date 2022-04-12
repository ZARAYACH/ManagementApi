package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;
import com.example.managementApi.WorkingDay.Day;
import com.example.managementApi.WorkingDay.DayRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;

@AllArgsConstructor
@Service
public class WeekTimeSheetService {


    private final WeekTimeSheetRepo weekTimeSheetRepo;
    private final DayRepo dayRepo;
    private final UserRepo userRepo;

    public List<WeekTimeSheet> getAllWeeklyTimeSheets(Integer userId) {
        return weekTimeSheetRepo.getAllByUserId(userId);
    }

    public WeekTimeSheet AddWeekTimeSheetInFriday(Day day) {
        if (dayRepo.existsById(day.getId())) {
            LocalDate fullDate = day.getFullDate();
            int dayInWeekNumber = fullDate.getDayOfWeek().getValue();
            if (dayInWeekNumber == 5) {
                LocalDate BeginningOfWeek = fullDate.minusDays(4);
                LocalDate endOfWeek = fullDate.plusDays(2);
                for (int i = 1; i < 3; i++) {
                    if (fullDate.plusDays(i).getDayOfWeek().getValue() == 6 ||
                            fullDate.plusDays(i).getDayOfWeek().getValue() == 7) {
                        dayRepo.save(new Day(fullDate.plusDays(i),
                                0,
                                false,
                                false,
                                false,
                                day.getUser()));
                    }
                }
                List<Day> daysOfweek = dayRepo.getDaysOfWeekByUserId(day.getUser().getId(), BeginningOfWeek, endOfWeek);
                var weekTimeSheet = new WeekTimeSheet();
                weekTimeSheet.setBeginningOfWeek(dayRepo.getDayByUserIdAndDate(BeginningOfWeek, day.getUser().getId()));
                weekTimeSheet.setEndOfWeek(dayRepo.getDayByUserIdAndDate(endOfWeek, day.getUser().getId()));
                weekTimeSheet.setTotalNhours(calculateTotalHoursWorked(daysOfweek));
                weekTimeSheet.setUser(day.getUser());
                return weekTimeSheetRepo.save(weekTimeSheet);
            } else throw new IllegalStateException("We can't add a week time sheet at this time,until end of week");
        } else throw new IllegalStateException("there is no declare worked day with this id ");
    }

    private int calculateTotalHoursWorked(List<Day> daysOfweek) {
        int total = 0;
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
        if (ChronoUnit.DAYS.between(beginnigOfweek.getFullDate(), endingOfweek.getFullDate()) == 7) {
            User user = weekTimeSheet.getUser();
            List<Day> weekDays = dayRepo.getDaysOfWeekByUserId(user.getId(), beginnigOfweek.getFullDate(), endingOfweek.getFullDate());
            int total = calculateTotalHoursWorked(weekDays);

            WeekTimeSheet weekTimeSheet1 = new WeekTimeSheet();

            weekTimeSheet1.setBeginningOfWeek(beginnigOfweek);
            weekTimeSheet1.setEndOfWeek(endingOfweek);
            weekTimeSheet1.setUser(user);
            weekTimeSheet1.setTotalNhours(total);
            return weekTimeSheetRepo.save(weekTimeSheet1);
        } else {
            throw new IllegalStateException("you must have 7 days in your week");
        }
    }

    //TODO:test this method
    public List<WeekTimeSheet> getAllWeekTimeSheetByUser(Integer userId, Integer superVisorId) {
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.findById(userId).get().getSuperVisorId();
            if (userSuperVisorId == superVisorId) {
                return weekTimeSheetRepo.getAllByUserId(userId);
            } else throw new IllegalStateException("you are note the superVisor of this employee");
        } else throw new IllegalStateException("this user doesn't exists");
    }

    //TODO:test it
    public List<Day> getAllDaysOfAWeekByUserId(Integer weekId, Integer userId, Integer superVisorId) {
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.findById(userId).get().getSuperVisorId();
            if (userSuperVisorId == superVisorId) {
                if (weekTimeSheetRepo.existsByWeekIdAndAndUserId(weekId, userId)) {
                    WeekTimeSheet weekTimeSheet = weekTimeSheetRepo.findById(weekId).get();
                    LocalDate beginningOfWeek = weekTimeSheet.getBeginningOfWeek().getFullDate();
                    LocalDate endingOfWeek = weekTimeSheet.getEndOfWeek().getFullDate();
                    return dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endingOfWeek);
                } else throw new IllegalStateException("this week isn't exist by this user");
            } else throw new IllegalStateException("you are not the superVisor of this employee");
        } else throw new IllegalStateException("this user doesn't exists");
    }

    //TODO: test it
    public ResponseEntity<HttpStatus> approvedAWorkedWeek(int weekId, int superVisorId, int userId) {
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.findById(userId).get().getSuperVisorId();
            if (userSuperVisorId == superVisorId) {
                List<Day> days = approvedAllDaysOfAWeek(weekId, userId);
                for (Day day : days) {
                    Day approvedDay = dayRepo.save(day);
                }
                WeekTimeSheet weekTimeSheet = weekTimeSheetRepo.findById(weekId).get();
                weekTimeSheet.setApproved(true);
                WeekTimeSheet result = weekTimeSheetRepo.save(weekTimeSheet);
                if (result.isApproved()) {
                    return new ResponseEntity<HttpStatus>(HttpStatus.OK);
                } else {
                    List<Day> dayss = unapprovedAllDaysOfAWeek(weekId, userId);
                    for (Day day : dayss) {
                        dayRepo.save(day);
                    }
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else throw new IllegalStateException("you are note the superVisor of this employee");
        } else throw new IllegalStateException("this user unExist");
    }
    //TODO:need test

    public ResponseEntity<HttpStatus> aprrovedOneDay(int dayId, int userId, int supervisorId) {
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.findById(userId).get().getSuperVisorId();
            if (userSuperVisorId == supervisorId) {
                if (dayRepo.existsByIdAndUserId(dayId, userId)) {
                    Day day = dayRepo.findById(dayId).get();
                    day.setApproved(true);
                     dayRepo.save(day);
                     return new ResponseEntity<>(HttpStatus.OK);
                } else throw new IllegalStateException("this day is doesn't exist");
            } else throw new IllegalStateException("you are not the superVisor of this employee");
        } else throw new IllegalStateException("this user doesn't exists");

    }

    //TODO: test it
    private List<Day> approvedAllDaysOfAWeek(int weekId, int userId) {
        if (weekTimeSheetRepo.existsByWeekIdAndAndUserId(weekId, userId)) {
            LocalDate beginningOfWeek = weekTimeSheetRepo.findById(weekId).get().getBeginningOfWeek().getFullDate();
            LocalDate endOfWeek = weekTimeSheetRepo.findById(weekId).get().getEndOfWeek().getFullDate();
            List<Day> days = dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endOfWeek);
            for (Day day : days) {
                day.setApproved(true);
            }
            return days;
        } else throw new IllegalStateException("this week isn't exist by this user");
    }

    //TODO: test it
    private List<Day> unapprovedAllDaysOfAWeek(int weekId, int userId) {
        if (weekTimeSheetRepo.existsByWeekIdAndAndUserId(weekId, userId)) {
            LocalDate beginningOfWeek = weekTimeSheetRepo.findById(weekId).get().getBeginningOfWeek().getFullDate();
            LocalDate endOfWeek = weekTimeSheetRepo.findById(weekId).get().getEndOfWeek().getFullDate();
            List<Day> days = dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endOfWeek);
            for (Day day : days) {
                day.setApproved(false);
            }
            return days;
        } else throw new IllegalStateException("this week isn't exist by this user");
    }

}


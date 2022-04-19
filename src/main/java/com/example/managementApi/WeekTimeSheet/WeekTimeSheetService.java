package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.MailSender.JavaEmailSenderService;
import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;
import com.example.managementApi.WorkingDay.Day;
import com.example.managementApi.WorkingDay.DayRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final JavaEmailSenderService emailSender;

    public List<WeekTimeSheet> getAllWeeklyTimeSheets(Authentication authentication) {
        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        return weekTimeSheetRepo.getAllByUserId(user.getId());
    }

    public ResponseEntity<?> AddWeekTimeSheetInFriday(Day day) {
        User user = day.getUser();
        if (dayRepo.existsById(day.getId())) {
            LocalDate fullDate = day.getFullDate();
            int dayInWeekNumber = fullDate.getDayOfWeek().getValue();
            if (dayInWeekNumber == 5) {
                LocalDate BeginningOfWeek = fullDate.minusDays(4);
                LocalDate endOfWeek = fullDate.plusDays(2);
                for (int i = 1; i < 3; i++) {
                    if (fullDate.plusDays(i).getDayOfWeek().getValue() == 6 ||
                            fullDate.plusDays(i).getDayOfWeek().getValue() == 7 &&
                                    !dayRepo.existsByFullDateAndUserId(fullDate.plusDays(i), user.getId())) {
                        dayRepo.save(new Day(fullDate.plusDays(i),
                                0,
                                false,
                                false,
                                false,
                                8,
                                "WeekEnd",
                                day.getUser()));
                    }
                }
                List<Day> daysOfweek = dayRepo.getDaysOfWeekByUserId(day.getUser().getId(), BeginningOfWeek, endOfWeek);
                var weekTimeSheet = new WeekTimeSheet();
                weekTimeSheet.setBeginningOfWeek(dayRepo.getDayByUserIdAndDate(BeginningOfWeek, day.getUser().getId()));
                weekTimeSheet.setEndOfWeek(dayRepo.getDayByUserIdAndDate(endOfWeek, day.getUser().getId()));
                weekTimeSheet.setTotalNhours(calculateTotalHoursWorked(daysOfweek));
                weekTimeSheet.setUser(day.getUser());
                WeekTimeSheet save = weekTimeSheetRepo.save(weekTimeSheet);
                if (save !=null){
                    User supervisor = userRepo.getById(user.getId());
                    emailSender.SendHtmlEmail(supervisor.getEmail(),
                            "mohamedachbani5@gmail.com",
                            "A week Time Sheet was Added",
                            "A week time sheet with id"+save.getWeekId()+"was added for"+user.getFirstName()+" "+user.getLastName()+"please check it As soon As Possible");
                    return ResponseEntity.ok().body("A week time Sheet was added");
                }else return ResponseEntity.badRequest().body("please repeat later");
            } else return ResponseEntity.badRequest().body("We can't add a week time sheet at this time,until end of week");
        } else return ResponseEntity.badRequest().body("there is no declare worked day with this id ");
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

    public ResponseEntity<?> addWeekTimeSheet(WeekTimeSheet weekTimeSheet, Authentication authentication) {

        Day beginingOfweek = weekTimeSheet.getBeginningOfWeek();
        Day endingOfweek = weekTimeSheet.getEndOfWeek();
        if (ChronoUnit.DAYS.between(beginingOfweek.getFullDate(), endingOfweek.getFullDate()) == 7) {
            User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
            List<Day> weekDays = dayRepo.getDaysOfWeekByUserId(user.getId(), beginingOfweek.getFullDate(), endingOfweek.getFullDate());
            int total = calculateTotalHoursWorked(weekDays);
            WeekTimeSheet weekTimeSheet1 = new WeekTimeSheet();
            weekTimeSheet1.setBeginningOfWeek(beginingOfweek);
            weekTimeSheet1.setEndOfWeek(endingOfweek);
            weekTimeSheet1.setUser(user);
            weekTimeSheet1.setTotalNhours(total);
            WeekTimeSheet result = weekTimeSheetRepo.save(weekTimeSheet1);
            if (result != null) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body("please repeat later");
            }
        } else {
            return ResponseEntity.badRequest().body("you must have 7 days in your week");
        }
    }

//    public ResponseEntity<?> getWeekTimeSheets(Authentication authentication,int page){
//        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
//        return weekTimeSheetRepo.getWeekTimeSheetByDayAndUserIdLimeted(user.getId(),page);
//    }


    public ResponseEntity<?> getAllWeekTimeSheetByUser(Integer userId, Authentication authentication) {
        User superVisor = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.getById(userId).getSuperVisorId();
            if (userSuperVisorId == superVisor.getId()) {
                return ResponseEntity.ok().body(weekTimeSheetRepo.getAllByUserId(userId));
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are note the superVisor of this employee");
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getAllDaysOfAWeekByUserId(Integer weekId, Integer userId, Authentication authentication) {
        User superVisor = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.getById(userId).getSuperVisorId();
            if (userSuperVisorId == superVisor.getId()) {
                if (weekTimeSheetRepo.existsByWeekIdAndAndUserId(weekId, userId)) {
                    WeekTimeSheet weekTimeSheet = weekTimeSheetRepo.getById(weekId);
                    LocalDate beginningOfWeek = weekTimeSheet.getBeginningOfWeek().getFullDate();
                    LocalDate endingOfWeek = weekTimeSheet.getEndOfWeek().getFullDate();
                    return ResponseEntity.ok().body(dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endingOfWeek));
                } else return ResponseEntity.notFound().build();
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not the superVisor of this employee");
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> approvedAWorkedWeek(int weekId, Authentication authentication, int userId) {
        User superVisor = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.getById(userId).getSuperVisorId();
            if (userSuperVisorId == superVisor.getId()) {
                if (weekTimeSheetRepo.existsByWeekIdAndAndUserId(weekId, userId)) {
                    List<Day> days = approvedAllDaysOfAWeek(weekId, userId);
                    dayRepo.saveAll(days);
                    WeekTimeSheet weekTimeSheet = weekTimeSheetRepo.getById(weekId);
                    weekTimeSheet.setApproved(true);
                    WeekTimeSheet result = weekTimeSheetRepo.save(weekTimeSheet);
                    if (result.isApproved()) {
                        return ResponseEntity.ok().body("this week was approved successfully");
                    } else {
                        List<Day> dayss = unapprovedAllDaysOfAWeek(weekId, userId);
                        dayRepo.saveAll(dayss);
                        return ResponseEntity.badRequest().body("please repeat later");
                    }
                }
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are note the superVisor of this employee");
        } else return ResponseEntity.notFound().build();
        return null;
    }

    public ResponseEntity<?> approvedOneDay(int dayId, int userId, Authentication authentication) {
        User superVisor = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (userRepo.existsById(userId)) {
            int userSuperVisorId = userRepo.getById(userId).getSuperVisorId();
            if (userSuperVisorId == superVisor.getId()) {
                if (dayRepo.existsByIdAndUserId(dayId, userId)) {
                    Day day = dayRepo.getById(userId);
                    day.setApproved(true);
                    dayRepo.save(day);
                    return ResponseEntity.ok().body("this day was approved successfully");
                } else return ResponseEntity.notFound().build();
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not the superVisor orf this employee");
        } else return ResponseEntity.notFound().build();

    }

    private List<Day> approvedAllDaysOfAWeek(int weekId, int userId) {
        LocalDate beginningOfWeek = weekTimeSheetRepo.getById(weekId).getBeginningOfWeek().getFullDate();
        LocalDate endOfWeek = weekTimeSheetRepo.getById(weekId).getEndOfWeek().getFullDate();
        List<Day> days = dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endOfWeek);
        for (Day day : days) {
            day.setApproved(true);
        }
        return days;
    }

    private List<Day> unapprovedAllDaysOfAWeek(int weekId, int userId) {
        LocalDate beginningOfWeek = weekTimeSheetRepo.getById(weekId).getBeginningOfWeek().getFullDate();
        LocalDate endOfWeek = weekTimeSheetRepo.getById(weekId).getEndOfWeek().getFullDate();
        List<Day> days = dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endOfWeek);
        for (Day day : days) {
            day.setApproved(false);
        }
        return days;
    }

    public void refreshAWeekTimeSheet(int weekId, int userId) {
        WeekTimeSheet weekTimeSheet = weekTimeSheetRepo.getById(weekId);
        LocalDate beginningOfWeek = weekTimeSheet.getBeginningOfWeek().getFullDate();
        LocalDate endOfWeek = weekTimeSheet.getEndOfWeek().getFullDate();
        List<Day> days = dayRepo.getDaysOfWeekByUserId(userId, beginningOfWeek, endOfWeek);
        int total = calculateTotalHoursWorked(days);
        weekTimeSheet.setTotalNhours(total);
        weekTimeSheetRepo.save(weekTimeSheet);
    }

    public WeekTimeSheet getWeekTimeSheetByDayAndByUserId(LocalDate fullDate, int id) {
        return weekTimeSheetRepo.getWeekTimeSheetByDayAndUserId(fullDate, id);
    }
}


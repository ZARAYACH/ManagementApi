package com.example.managementApi.WorkingDay;

import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;

import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class DayService {

    private final DayRepo dayRepo;
    private final UserRepo userRepo;
    private final WeekTimeSheetService weekTimeSheetService;

    public Day addDay(Day day, Authentication authentication) {
        Day result = null;
        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (!dayRepo.existsByFullDateAndUserId(day.getFullDate(), user.getId())) {
            day.setApproved(false);
            if (day.getNumberHours() >= 1) {
                day.setWorked(true);
                day.setUser(user);
                if (day.getNumberHours() > 8) {
                    day.setOverTimed(true);
                    day.setUnWorkedNHours(0);
                    day.setUnWorkedHoursReason(null);
                    result = dayRepo.save(day);
                } else if (day.getNumberHours() < 8) {
                    day.setOverTimed(false);
                    if (day.getNumberHours() + day.getUnWorkedNHours() == 8) {
                        result = dayRepo.save(day);
                    } else
                        throw new IllegalStateException("it is required to have 8hours per day even if its not worked");
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

    }

    public List<Day> getAllApprovedWorkedDays(Authentication authentication) {
        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        return dayRepo.getAllApprovedWorkedDaysWithId(user.getId());

    }

    public List<Day> getAllWorkedDays(Authentication authentication) {
        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        return dayRepo.getAllWorkedDaysWithId(user.getId());
    }
    //TODO : add a end point to it and test it
    public ResponseEntity<?> deleteUnapprovedDay(int dayId, Authentication authentication) {
        User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (dayRepo.existsByIdAndUserId(dayId, user.getId())) {
            Day day = dayRepo.getById(dayId);
            if (!day.isApproved()) {
                LocalDate fullDate = day.getFullDate();
                WeekTimeSheet weekTimeSheet = weekTimeSheetService.getWeekTimeSheetByDayAndByUserId(fullDate,user.getId());
                dayRepo.deleteById(day.getId());
                if (weekTimeSheet==null) if (!dayRepo.existsById(day.getId())) {
                    return ResponseEntity.ok().body("this day was deleted successfully");
                } else return ResponseEntity.internalServerError().body("please try later");
                else if (!dayRepo.existsById(day.getId())) {
                    weekTimeSheetService.refreshAWeekTimeSheet(weekTimeSheet.getWeekId(), user.getId());
                    return ResponseEntity.ok().body("this day was deleted successfully and the week Time Sheet of it was updated");
                } else return ResponseEntity.internalServerError().body("please try later");
            }else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you can't remove an approved day please Ask you superVisor");
        }else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> modifyUnApprovedDay(Day newDay,Authentication authentication){
     User user = userRepo.getUserByEmail(authentication.getPrincipal().toString());
     if (dayRepo.existsById(newDay.getId())){
         Day oldDay = dayRepo.getById(newDay.getId());
         WeekTimeSheet weekTimeSheet = weekTimeSheetService.getWeekTimeSheetByDayAndByUserId(newDay.getFullDate(),user.getId());
         if (!oldDay.isApproved()){
             oldDay.setNumberHours(newDay.getNumberHours());
             oldDay.setWorked(newDay.isWorked());
             oldDay.setOverTimed(newDay.isOverTimed());
             oldDay.setUnWorkedNHours(newDay.getUnWorkedNHours());
             oldDay.setFullDate(newDay.getFullDate());
             oldDay.setUnWorkedHoursReason(newDay.getUnWorkedHoursReason());
             oldDay.setUser(user);
             Day updatedDay = dayRepo.save(oldDay);
             if (weekTimeSheet != null){
                 weekTimeSheetService.refreshAWeekTimeSheet(weekTimeSheet.getWeekId(), user.getId());
             }
             return ResponseEntity.ok().body(updatedDay);

         }else {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you can't update a approved day please contact your supervisor");
         }
     }else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getWorkingDaysBySuperVisorEmployee(int employeeId, Authentication authentication) {
        User superVisor = userRepo.getUserByEmail(authentication.getPrincipal().toString());
        if (userRepo.existsById(employeeId)){
            User employee = userRepo.getById(employeeId);
            if (employee.getSuperVisorId() == superVisor.getId()){
               return ResponseEntity.ok().body(dayRepo.getAllByUserId(employeeId));
            }else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this user is not under your supervision");
        }else return ResponseEntity.notFound().build();
    }
}

package com.example.managementApi.WeekTimeSheet;


import com.example.managementApi.WorkingDay.Day;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1")
public class WeekTimeSheetController {

    private final WeekTimeSheetService weekTimeSheetService ;

    //SuperVisor methods
    //TODO:test this method
    @GetMapping(path = "/supervisor/weeklyTimeSheets/employee/{userId}/all")
    public List<WeekTimeSheet> getAllWeekTimeSheetByUser(@PathVariable Integer userId,Authentication authentication){
        return weekTimeSheetService.getAllWeekTimeSheetByUser(userId,authentication);
    }

    @GetMapping(path = "/supervisor/weeklyTimeSheets/employee/{userId}/Week/{weekId}")
    public List<Day> getAllDaysOfAWeekByUserId(@PathVariable int weekId,@PathVariable int userId,Authentication authentication){
        return weekTimeSheetService.getAllDaysOfAWeekByUserId(weekId,userId,authentication);
    }

    @PutMapping(path = "/supervisor/weeklyTimeSheets/employee/{userId}/approvedWeek/{weekId}")
    public ResponseEntity<HttpStatus> approvedAWorkedWeek(@PathVariable int userId, @PathVariable int weekId,Authentication authentication){
        return weekTimeSheetService.approvedAWorkedWeek(weekId,authentication,userId);
    }

    @PutMapping(path = "/supervisor/weeklyTimeSheets/employee/{userId}/approvedDay/{dayId}")
    public ResponseEntity<HttpStatus> approvedoneDay(@PathVariable int userid, @PathVariable int dayId, Authentication authentication){
        return weekTimeSheetService.approvedOneDay(dayId,userid,authentication);
    }

    //employee methods
    @GetMapping(path = "/employee/weeklyTimeSheets/all")
    public List<WeekTimeSheet> getAllWeeklyTimeSheets(Authentication authentication){
           return  weekTimeSheetService.getAllWeeklyTimeSheets(authentication);
    }

    @PostMapping(path = "/weeklyTimeSheets/employee/add")
    public WeekTimeSheet addWeekTimeSheet(@RequestBody WeekTimeSheet weekTimeSheet,Authentication authentication){
        return weekTimeSheetService.addWeekTimeSheet(weekTimeSheet,authentication);
    }


}

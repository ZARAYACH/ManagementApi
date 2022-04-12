package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.WorkingDay.Day;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/weeklyTimeSheets/")
public class WeekTimeSheetController {

    private final WeekTimeSheetService weekTimeSheetService ;
    public WeekTimeSheetController(WeekTimeSheetService weekTimeSheetService) {
        this.weekTimeSheetService = weekTimeSheetService;
    }
    //SuperVisor methods
    //TODO:test this method
    @GetMapping(path = "/supervisor/employee/{userId}/all")
    public List<WeekTimeSheet> getAllWeekTimeSheetByUser(@PathVariable Integer userId,@SessionAttribute int superVisorId){
        return weekTimeSheetService.getAllWeekTimeSheetByUser(userId,superVisorId);
    }

    @GetMapping(path = "/supervisor/employee/{userId}/daysOfWeek/{weekId}")
    public List<Day> getAllDaysOfAWeekByUserId(@PathVariable int weekId,@PathVariable int userId,@SessionAttribute int superVisorId){
        return weekTimeSheetService.getAllDaysOfAWeekByUserId(weekId,userId,superVisorId);
    }

    @PutMapping(path = "/supervisor/employee/{userId}/approvedWeek/{weekId}")
    public ResponseEntity<HttpStatus> approvedAWorkedWeek(@PathVariable int userId, @PathVariable int weekId, @SessionAttribute int superVisorId){
        return weekTimeSheetService.approvedAWorkedWeek(weekId,superVisorId,userId);
    }

    @PutMapping(path = "/supervisor/employee/{userId}/approvedDay/{dayId}")
    public ResponseEntity<HttpStatus> approvedoneDay(@PathVariable int userid,@PathVariable int dayId,@SessionAttribute int superVisorId){
        return weekTimeSheetService.aprrovedOneDay(dayId,userid,superVisorId);
    }

    //employee methods
    @GetMapping(path = "employee/{userId}/all")
    public List<WeekTimeSheet> getAllWeeklyTimeSheets(@PathVariable Integer userId){
           return  weekTimeSheetService.getAllWeeklyTimeSheets(userId);
    }

    @PostMapping(path = "employee/add")
    public WeekTimeSheet addWeekTimeSheet(@RequestBody WeekTimeSheet weekTimeSheet){
        return weekTimeSheetService.addWeekTimeSheet(weekTimeSheet);
    }


}

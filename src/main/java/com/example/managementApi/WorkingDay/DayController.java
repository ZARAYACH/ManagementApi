package com.example.managementApi.WorkingDay;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1")
public class DayController {

    private final DayService dayService;

    //employee's methods
    @GetMapping(path = "/employee/WorkingDay/all")
    public List<Day> getAllWorkedDays(Authentication authentication){
        return dayService.getAllWorkedDays(authentication);
    }

    @GetMapping(path = "/employee/WorkingDay/all/approved")
    public List<Day> getAllApprovedWorkedDays(Authentication authentication){
        return dayService.getAllApprovedWorkedDays(authentication);
    }

    @PostMapping(path = "/employee/WorkingDay/add")
    public Day addDayForEmployee(@RequestBody Day day,Authentication authentication) {
        return dayService.addDay(day,authentication);

    }
//    @DeleteMapping(path = "/employee/WorkingDay/{dayId}/delete")
//    public Day addDayForEmployee(@PathVariable int dayId,Authentication authentication) {
//        return dayService.deleteUnapprovedDay(dayId,authentication);
//
//    }

    //TODO:when it's not approved he can change but if its submitted friday you have to change the week time sheet also it may be complicated

//    @PutMapping(path = "/employee/{userId}/modify/{dayId}" )
//    public Day modifyUnApprovedDay(@PathVariable int userId, @PathVariable int dayId,@RequestBody Day day){
//        return dayService.modifyUnApprovedDay(userId,dayId,day);
//    }

//    supervisors method
    @PostMapping(path = "/supervisor/add")
    public Day addDayForSupervisor(@RequestBody Day day,Authentication authentication){
        return dayService.addDay(day,authentication);
    }


}

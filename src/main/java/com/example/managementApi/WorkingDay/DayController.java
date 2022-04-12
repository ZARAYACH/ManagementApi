package com.example.managementApi.WorkingDay;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/WorkingDays")
public class DayController {

    private final DayService dayService;

    //employee's methods
    @GetMapping(path = "/employee/{userId}/all")
    public List<Day> getAllWorkedDays(@PathVariable int userId){
        return dayService.getAllWorkedDays(userId);
    }

    @GetMapping(path = "/employee/{userId}/all/approved")
    public List<Day> getAllApprovedWorkedDays(@PathVariable int userId){
        return dayService.getAllApprovedWorkedDays(userId);
    }

    @PostMapping(path = "/employee/add")
    public Day addDayForEmployee(@RequestBody Day day){
        return dayService.addDay(day);
    }

    //TODO:when it's not approved he can change but if its submitted friday you have to change the week time sheet also it may be complicated

//    @PutMapping(path = "/employee/{userId}/modify/{dayId}" )
//    public Day modifyUnApprovedDay(@PathVariable int userId, @PathVariable int dayId,@RequestBody Day day){
//        return dayService.modifyUnApprovedDay(userId,dayId,day);
//    }

//    supervisors method
    @PostMapping(path = "/supervisor/add")
    public Day addDayForSupervisor(@RequestBody final Day day){
        return dayService.addDay(day);
    }


}

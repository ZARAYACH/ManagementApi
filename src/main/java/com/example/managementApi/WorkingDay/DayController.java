package com.example.managementApi.WorkingDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/WorkingDays")
public class DayController {

    private final DayService dayService;
    @Autowired
    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

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

//    suprvisors method
    @PostMapping(path = "/supervisor/add")
    public Day addDayForSupervisor(@RequestBody final Day day){
        return dayService.addDay(day);
    }


}

package com.example.managementApi.WorkingDay;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
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
    @DeleteMapping(path = "/employee/WorkingDay/{dayId}/delete")
    public ResponseEntity<?> addDayForEmployee(@PathVariable int dayId,Authentication authentication) {
        return dayService.deleteUnapprovedDay(dayId,authentication);

    }

    //TODO:TEST IT

    @PutMapping(path = "/employee/WorkingDay/modify/day" )
    public ResponseEntity<?> modifyUnApprovedDay(@RequestBody Day day, Authentication authentication){
        return dayService.modifyUnApprovedDay(day,authentication);
    }

//    supervisors method
    @PostMapping(path = "/supervisor/WorkingDay/add")
    public Day addDayForSupervisor(@RequestBody Day day,Authentication authentication){
        return dayService.addDay(day,authentication);
    }

    @GetMapping(path = "/supervisor/WorkingDay/employee/{employeeId}")
    public ResponseEntity<?> addDayForSupervisor(@PathVariable int employeeId,Authentication authentication){
        return dayService.getWorkingDaysBySuperVisorEmployee(employeeId,authentication);
    }

}

package com.example.managementApi.WeekTimeSheet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/weeklyTimeSheets/")
public class WeekTimeSheetController {

    private final WeekTimeSheetService weekTimeSheetService ;
    public WeekTimeSheetController(WeekTimeSheetService weekTimeSheetService) {
        this.weekTimeSheetService = weekTimeSheetService;
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

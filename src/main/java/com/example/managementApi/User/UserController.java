package com.example.managementApi.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class UserController {
    private final UserService userService ;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/supervisor/employee/all")
    public List<User> getAllEmployee(){
        return userService.getAllEmployeeInfoWithoutWeekAndDay();
    }

    @PostMapping("/supervisor/employee/add")
    public User addEmployee(@RequestBody User user){
        return userService.addEmployee(user);
    }

    @PutMapping(path = "/supervisor/employee/modify/{userId}")
    public User updateEmployee(@PathVariable Integer userId, @RequestBody User user ){
       return   userService.updateEmployee(userId,user);
    }

    @DeleteMapping(path = "/supervisor/employee/delete/{userId}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable int userId){
        return userService.deleteEmployee(userId);
    }

    //employee methods
    @GetMapping(path = "/employee/{userID}")
    public User getInfoEmployee(@PathVariable Integer userID){
        return userService.getUserInfo(userID);//TODO:return the info of the authenticate User only
    }
    @GetMapping(path = "/employee/{userId}/supervisor")
    public User getInfoOfMySupervisor(@PathVariable Integer userId){
        return userService.getInfoOfMySupervisor(userId);
    }



}

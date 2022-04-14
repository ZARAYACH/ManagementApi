package com.example.managementApi.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class UserController {
    private final UserService userService ;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping( path = "/testSession")
//    public User testSession(HttpSession session){
//        User user = userService.getUserInfo();
//        session.setAttribute("user",user);
//        return (User)session.getAttribute("user");
//    }

    @GetMapping("/supervisor/employee/all")
    public List<User> getAllEmployee(Authentication authentication){
        return userService.getAllEmployeeInfoWithoutWeekAndDay(authentication);
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
    @GetMapping(path = "/supervisor/all")
    public User getInfoSuperVisor(Authentication authentication){
        return userService.getUserInfo(authentication);//TODO:return the info of the authenticate User only
    }

    //employee methods
    @GetMapping(path = "employee/test/{userId}")
    public String test(){
        return "working";

    }
    @GetMapping(path = "/supervisor/test")
    public String testa(Authentication authentication){
        return authentication.getPrincipal().toString();

    }

    @GetMapping(path = "/employee/{userID}")
    public User getInfoEmployee(Authentication authentication ){
        return userService.getUserInfo(authentication);
    }
    @GetMapping(path = "/employee/{userId}/supervisor")
    public User getInfoOfMySupervisor(@PathVariable Integer userId){
        return userService.getInfoOfMySupervisor(userId);
    }



}

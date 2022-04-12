package com.example.managementApi.signInLogs;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/Logs")
public class LogsController {

    private final LogsService logsService;

    @GetMapping(path = "/supervisor/employee/{userId}/All")
    public List<Logs> getAllLogsByUser(@PathVariable int userId, HttpSession session){
        return logsService.getAllLogsByUser(userId,session);
    }

}

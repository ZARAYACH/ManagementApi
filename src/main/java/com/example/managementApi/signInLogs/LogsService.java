package com.example.managementApi.signInLogs;

import com.example.managementApi.User.User;
import com.example.managementApi.User.UserRepo;
import com.example.managementApi.User.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@AllArgsConstructor
public class LogsService {

    public final UserRepo userRepo;
    public final LogsRepo logsRepo;

    public List<Logs> getAllLogsByUser(int userId, HttpSession session) {
        if (userRepo.existsById(userId)){
            int superVisorId = userRepo.findById(userId).get().getSuperVisorId();
            User superVisor =(User)session.getAttribute("user");
            if (superVisorId == superVisor.getId()){
                return logsRepo.findAllByUserId(userId);
            }else throw new IllegalStateException("you're not the supervisor of this user");
        }else throw new IllegalStateException("this user doesn't exists");
    }

    public Logs addLog(Logs log){
        return logsRepo.save(log);
    }
    public Logs addLogoutTime(Logs logs){
        return logsRepo.save(logs);
    }
}

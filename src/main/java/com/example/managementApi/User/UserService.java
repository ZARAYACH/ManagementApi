package com.example.managementApi.User;

import com.example.managementApi.UserCredentiels.UserCredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.*;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCredentialsService userCredentialsService;

    public List<User> getAllEmployeeInfoWithoutWeekAndDay(Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        User user = userRepo.getUserByEmail(email);
        List<User> users = userRepo.getAllUserBySuperVisorId(user.getSuperVisorId(),user.getId());
        List<User> employees = new ArrayList<>();
        for (User employee : users) {
            employee.setDays(null);
            employee.setWeekTimeSheets(null);
            employee.setUserCredentials(null);
            employees.add(employee);
        }
        return employees;
    }
    public User addEmployee(User user) {
        if (!checkUserIsEmpty(user)) {
            if (validEmail(user)) {
                if (!checkExistingEmail(user)) {
                   if (user.getUserCredentials() != null){
//                       user.setRole("ROLE_"+user.getRole().toUpperCase());
                       user.getUserCredentials().setEmail(user.getEmail());
                       if (userCredentialsService.cheekStrongestOfPassword(user.getUserCredentials())){
                           user.getUserCredentials().setPassword(passwordEncoder.encode(user.getUserCredentials().getPassword()));
                       }else throw new IllegalStateException("the password must be strong");
                       user.getUserCredentials().setUser(user);
                       return userRepo.save(user);
                   }else throw new IllegalStateException("you can't add a user without Credentials");
                } else throw new IllegalStateException("This user is already exist");
            } else throw new IllegalStateException("This email is wrong");
        } else throw new IllegalStateException("one or more field is empty");
    }

    private boolean checkExistingEmail(User user) {
        boolean result;
        User searched = userRepo.existsByEmail(user.getEmail());
        result = searched != null;
        return result;
    }

    private boolean validEmail(User user) {
        String email = user.getEmail();
        var regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regexPattern);
    }

    public User updateEmployee(int userId, User user) {
        if (userExists(userId)) {
            User user2 = userRepo.getById(userId);
            user2.setFirstName(user.getFirstName());
            user2.setLastName(user.getLastName());
            user2.setPhone(user.getPhone());
            return userRepo.save(user2);

        } else {
            throw new IllegalStateException("this user doesn't exists");
        }


    }

    public boolean checkUserIsEmpty(User user) {
        return user.getEmail().isBlank() ||
                user.getFirstName().isBlank() ||
                user.getLastName().isBlank() ||
                user.getEmail().isBlank() ||
                user.getPhone().isBlank();

    }

    public boolean userExists(int Id) {
        return userRepo.existsById(Id);
    }

    public ResponseEntity<HttpStatus> deleteEmployee(int userID) {
        ResponseEntity response ;
        if (userRepo.existsById(userID)) {
            userRepo.deleteById(userID);
            if (!userRepo.existsById(userID)) response = new ResponseEntity<HttpStatus>(HttpStatus.OK);
            else response = new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else response = new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        return response;
    }

    public User getInfoOfMySupervisor(Integer userId) {
        if (userRepo.existsById(userId)) {
            User user = userRepo.findById(userId).get();
            int supervisorId = user.getSuperVisorId();
            if (userRepo.existsById(supervisorId)) {
                User userTemp = userRepo.findById(supervisorId).get();
                return new User(userTemp.getId(),
                        userTemp.getFirstName(),
                        userTemp.getLastName(),
                        userTemp.getEmail(),
                        userTemp.getRoles(),
                        userTemp.getJobTitle(),
                        userTemp.getPhone(),
                        userTemp.getSuperVisorId());
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public User getUserInfo(Authentication authentication) {
        User tempUser = userRepo.getUserByEmail(authentication.getPrincipal().toString());
            User user = new User();
            user.setId(tempUser.getId());
            user.setFirstName(tempUser.getFirstName());
            user.setLastName(tempUser.getLastName());
            user.setEmail(tempUser.getEmail());
            user.setRoles(tempUser.getRoles());
            user.setPhone(tempUser.getPhone());
            user.setJobTitle(tempUser.getJobTitle());
            user.setSuperVisorId(tempUser.getSuperVisorId());
            user.setActive(tempUser.isActive());
            return user;
    }

//    in the working day service


}

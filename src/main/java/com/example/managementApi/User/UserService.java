package com.example.managementApi.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllEmployeeInfoWithoutWeekAndDay() {
        List<User> users = userRepo.findAll();
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
        User save;
        if (!checkUserIsEmpty(user)) {
            if (validEmail(user)) {
                if (!checkExistingEmail(user)) {
                    save = userRepo.save(user);
                } else {
                    throw new IllegalStateException("This user is already exist");
                }
            } else {
                throw new IllegalStateException("This email is wrong");
            }
        } else {
            throw new IllegalStateException("one or more field is empty");
        }
        return save;
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
                user.getRole().isBlank() ||
                user.getLevel().isBlank() ||
                user.getPhone().isBlank();

    }

    public boolean userExists(int Id) {
        return userRepo.existsById(Id);
    }

    public ResponseEntity<HttpStatus> deleteEmployee(int userID) {
        ResponseEntity response = null;
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
                        userTemp.getRole(),
                        userTemp.getLevel(),
                        userTemp.getPhone(),
                        userTemp.getSuperVisorId());
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public User getUserInfo(Integer userID) {
        if (userRepo.existsById(userID)) {
            User tempUser = userRepo.findById(userID).get();
            User user = new User();
            user.setId(tempUser.getId());
            user.setFirstName(tempUser.getFirstName());
            user.setLastName(tempUser.getLastName());
            user.setEmail(tempUser.getEmail());
            user.setRole(tempUser.getRole());
            user.setPhone(tempUser.getPhone());
            user.setLevel(tempUser.getLevel());
            user.setSuperVisorId(tempUser.getSuperVisorId());
            user.setActive(tempUser.isActive());
            return user;
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }

//    in the working day service


}

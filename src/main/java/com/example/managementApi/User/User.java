package com.example.managementApi.User;

import com.example.managementApi.UserCredentiels.UserCredentials;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;
import com.example.managementApi.WorkingDay.Day;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String level ;
    private boolean isActive;
    private String phone;
    private int superVisorId;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Day> days;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserCredentials userCredentials;

    @OneToMany(mappedBy = "user")
    private List<WeekTimeSheet> weekTimeSheets;

    public User(String firstName, String lastName, String email, String role, String level, boolean isActive, String phone, int superVisorId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.level = level;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }
    public User(int id, String firstName, String lastName, String email, String role, String level, String phone,Integer superVisorId){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.level = level;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }

    public User(int id, String firstName, String lastName, String email, String role, String level, boolean isActive, String phone, int superVisorId,List<WeekTimeSheet> weekTimeSheets) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.level = level;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
        this.weekTimeSheets = weekTimeSheets;
    }

}


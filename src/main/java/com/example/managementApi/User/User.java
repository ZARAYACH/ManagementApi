package com.example.managementApi.User;

import com.example.managementApi.UserCredentiels.UserCredentials;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;
import com.example.managementApi.WorkingDay.Day;
import com.example.managementApi.signInLogs.Logs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String jobTitle ;
    private boolean isActive;
    private String phone;
    private int superVisorId;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Day> days;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credentials_id" ,referencedColumnName = "id")
    private UserCredentials userCredentials;

    @OneToMany(mappedBy = "user")
    private List<WeekTimeSheet> weekTimeSheets;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private  List<Logs> logs;

    public User(String firstName, String lastName, String email, UserRole role, String jobTitle, boolean isActive, String phone, int superVisorId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.jobTitle = jobTitle;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }
    public User(int id, String firstName, String lastName, String email, UserRole role, String jobTitle, String phone,Integer superVisorId){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.jobTitle = jobTitle;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }

    public User(int id, String firstName, String lastName, String email, UserRole role, String jobTitle, boolean isActive, String phone, int superVisorId,List<WeekTimeSheet> weekTimeSheets) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.jobTitle = jobTitle;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
        this.weekTimeSheets = weekTimeSheets;
    }

}


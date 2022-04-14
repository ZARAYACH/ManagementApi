package com.example.managementApi.User;

import com.example.managementApi.Role.UserRoleAuth;
import com.example.managementApi.UserCredentiels.UserCredentials;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;
import com.example.managementApi.WorkingDay.Day;
import com.example.managementApi.signInLogs.Logs;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<UserRoleAuth> roles = new ArrayList<>();

    private String jobTitle ;
    private boolean isActive;
    private String phone;
    @Column(nullable = true)
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

    @OneToMany(mappedBy = "user")
    private  List<Logs> logs;

    public User(String firstName, String lastName, String email, Collection<UserRoleAuth> roles, String jobTitle, boolean isActive, String phone, int superVisorId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.jobTitle = jobTitle;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }

    public User(int id, String firstName, String lastName, String email , Collection<UserRoleAuth> roles , String jobTitle, String phone, Integer superVisorId){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.jobTitle = jobTitle;
        this.phone = phone;
        this.superVisorId = superVisorId;
    }

    public User(int id, String firstName, String lastName, String email , Collection<UserRoleAuth> roles, String jobTitle, boolean isActive, String phone, int superVisorId,List<WeekTimeSheet> weekTimeSheets) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.jobTitle = jobTitle;
        this.isActive = isActive;
        this.phone = phone;
        this.superVisorId = superVisorId;
        this.weekTimeSheets = weekTimeSheets;
    }

}


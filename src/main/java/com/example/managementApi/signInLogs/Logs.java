package com.example.managementApi.signInLogs;

import com.example.managementApi.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime loginTime;
    @Column
    private LocalDateTime logoutTime;
    @Column
    private String ipAddress;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public Logs(LocalDateTime loginTime, LocalDateTime logoutTime, String ipAddress, User user) {
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.ipAddress = ipAddress;
        this.user = user;
    }
}

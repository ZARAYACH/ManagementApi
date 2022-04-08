package com.example.managementApi.UserCredentiels;

import com.example.managementApi.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredentials {

    @Id
    @Column(name = "id")
    private int id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean isActive;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

}

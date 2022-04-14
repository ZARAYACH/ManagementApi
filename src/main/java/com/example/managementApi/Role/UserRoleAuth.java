package com.example.managementApi.Role;


import com.example.managementApi.User.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="role")
@Getter
@Setter
public class UserRoleAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

}

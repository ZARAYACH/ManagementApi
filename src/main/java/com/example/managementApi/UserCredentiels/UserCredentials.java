package com.example.managementApi.UserCredentiels;

import com.example.managementApi.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredentials implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean isActive = true;

    @JsonBackReference
    @OneToOne(mappedBy = "userCredentials")
    private User user;


    public UserCredentials(String email, String password, boolean isActive, User user) {
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        user.getRoles().forEach(role->
                authority.add(new SimpleGrantedAuthority(role.getName())));
        System.out.println(authority.toString());
        return authority;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public  String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.getUser().isActive();
    }
}

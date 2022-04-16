package com.example.managementApi.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User , Integer> {
    @Query(value = "SELECT * FROM user WHERE email =:email", nativeQuery = true)
    User existsByEmail(@Param("email") String email) ;

    @Modifying
    @Query(value = "update user set first_name =:firstName,last_name=:lastName,phone=:phone where id=:Id", nativeQuery = true)
    void updateUserInfo(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phone") String phone, @Param("Id") int Id);

    User getUserByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE super_visor_id=:superVisorId and super_visor_id <> :userId ",nativeQuery = true)
    List<User> getAllUserBySuperVisorId(int superVisorId,int userId);
}

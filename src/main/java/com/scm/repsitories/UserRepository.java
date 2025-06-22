package com.scm.repsitories;

import com.scm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u.name FROM user u WHERE u.name <> :currentUser")
    List<String> findAllUsernamesExcept(@Param("currentUser") String currentUser);
}

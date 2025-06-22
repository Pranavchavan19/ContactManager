package com.scm.repsitories;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.username FROM User u WHERE u.username <> :currentUser")
    List<String> findAllUsernamesExcept(@Param("currentUser") String currentUser);
}

package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,String> {
    User findByEmail(String email);

    @Query("select u from User u where (u.name like ?1% or u.id like ?1%)")
    List<User> findBySearchQuery(String query);
}

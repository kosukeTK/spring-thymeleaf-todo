package com.kosuke.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The UserRepository class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User a set a.enabled = true where a.email = ?1")
	int enableUser(String email);
}
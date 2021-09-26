package com.kosuke.user;

import java.util.Collection;

/**
 * The UserService interface
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
public interface UserService {

    /**
     * @param user
     * @return
     */
    User save(User user);

    Boolean delete(int id);

    User update(User user);

    User findById(int id);

    User findByUserName(String username);

    User findByEmail(String email);

    Collection<User> findAll();
    
    int enableUser(String email);
    
    String confirmToken(String token);
}

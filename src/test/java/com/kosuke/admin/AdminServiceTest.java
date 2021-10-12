/**
 * 
 */
package com.kosuke.admin;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kosuke.user.User;

/**
 * @author torit
 *
 */
@SpringBootTest
class AdminServiceTest {

	@Autowired
	AdminService adminService;
	
	@Autowired
	AdminMapper adminMapper;
	
	@Test
	void test() {
		List<User> userList = adminService.findEmail();
		userList.forEach(user -> System.out.println(user.getEmail()));
	}

}

/**
 * 
 */
package com.kosuke.admin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
		adminService.getEmail();
	}

}

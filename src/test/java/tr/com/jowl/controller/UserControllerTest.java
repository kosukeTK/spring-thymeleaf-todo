package tr.com.jowl.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.user.User;
import com.kosuke.user.UserController;


public class UserControllerTest {
	
	RedirectAttributes redirectAttributes;
	
	@Autowired
	private UserController userController;
	
	@Test
	public void test1() {
		String result = userController.register(
				new User("takesan",
						"aaaaa",
						"toriton30022@gmail.com",
						2),
				redirectAttributes
				);
		assertNotNull(result);
	}
	
}

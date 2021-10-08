package com.kosuke.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class UserControllerTest {

	
	MockMvc mockMvc;
	
	 @Autowired 
	 UserController target;
	 
	 @Autowired
	 UserService userSerivce;
	
	 @BeforeEach
	 void beforeEach() {
		 mockMvc =
	        MockMvcBuilders.standaloneSetup(target).build();
	 }
	 
	@Test
	void login() throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		//.andExpect(view().name("login"))
		.andExpect(model().attribute("reqUser", new User()));
	}
	
	@Disabled
	@SuppressWarnings("unchecked")
	@Test
	void username() throws Exception {
		Optional<User> user = Optional.of(new User("tomato", "tomato@email", "tomato1", 1));
		MvcResult result =
			mockMvc.perform(get("/username"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(model().attribute("user", user))
			.andReturn();
		Optional<User> userModels = (Optional<User>) result.getModelAndView().getModel().get("user");
		Optional<String> username = userModels.map(userModel -> userModel.getUsername());
		Assertions.assertEquals(username, "tomato");
	}
	
	@Test
	void messageTest() throws Exception {
		MvcResult result =
			mockMvc.perform(get("/message"))
			.andExpect(status().isOk())
			.andExpect(view().name("home"))
			.andReturn();
		String msgModel = (String) result.getModelAndView().getModel().get("msg");
		Assertions.assertEquals(msgModel, "fugaさん、おはようございます。");
	}

	  @Test
	  public void registerTest() throws Exception {
		  
		  User reqUser = new User("tomato", "tomato1", "tomato@email", 1);
	    // when
	    mockMvc.perform((post("/user/register"))
	    	.flashAttr("reqUser", reqUser))
//	    	.param("form", user))
//	        .andExpect(model().attribute("saveUser", "success"))
	        .andExpect(flash().attribute("saveUser", "success"))
	        .andExpect(redirectedUrl("/register"));

	    // then
	    //verify(userSerivce, times(1)).save(reqUser);
	  }
}
package com.example.demo.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@SpringBootTest
class UserControllerTest {

	
	MockMvc mockMvc;
	
	 @Autowired 
	 UserController target;
	
	 @BeforeEach
	 void beforeEach() {
		 mockMvc =
	        MockMvcBuilders.standaloneSetup(target).build();
	 }
	 
	@Test
	void login() throws Exception {
		mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("index"))
		.andExpect(model().attribute("reqUser", new User()));
	}
	
	@Test
	void username() throws Exception {
		Optional<User> user = Optional.of(new User("tomato", "tomato@email", "tomato1", 1));
		MvcResult result =
			mockMvc.perform(get("/username"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(model().attribute("user", user))
			.andReturn();
		Optional<User> u = (Optional<User>) result.getModelAndView().getModel().get("user");
	}

}

package com.kosuke.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.kosuke.user.User;


@Controller
public class AdminController {
	
	private final AdminService adminService;
	
	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping(path="/admin")
	public String admin() {
		List<User> emailList = adminService.getEmail();
		return "admin";
	}
}

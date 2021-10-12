package com.kosuke.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.todo.Task;
import com.kosuke.user.User;


@Controller
public class AdminController {
	
	private final AdminService adminService;
	
	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping(path="/admin/getUserTodoInfo")
	public String getUserTodoInfo(	@RequestParam("email") String email,
									final RedirectAttributes redirectAttributes) {
		List<User> userList = adminService.findByEmailGetUserTodo(email);
		Optional<List<Task>> taskList = userList.stream()
				.map(ulist -> ulist.getTaskList())
				.findFirst();
		redirectAttributes.addFlashAttribute("taskList", taskList.get());
		redirectAttributes.addFlashAttribute("userList", userList);
		return "redirect:/admin";
	}
	
	@PostMapping(path="/admin/editUser")
	public String editUser(
//			@RequestParam("username") String username,
			@ModelAttribute("reqUser") User reqUser,
			final RedirectAttributes redirectAttributes) {
			List<User> userList = adminService.findByEmailGetUserTodo(reqUser.getEmail());
			Optional<List<Task>> taskList = userList.stream()
					.map(ulist -> ulist.getTaskList())
					.findFirst();
			redirectAttributes.addFlashAttribute("taskList", taskList.get());
			redirectAttributes.addFlashAttribute("userList", userList);
		return "redirect:/admin";
	}
}

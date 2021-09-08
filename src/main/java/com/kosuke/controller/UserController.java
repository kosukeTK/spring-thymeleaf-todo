package com.kosuke.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.model.Task;
import com.kosuke.model.User;
import com.kosuke.service.TaskService;
import com.kosuke.service.UserService;
import com.kosuke.utils.PassEncoding;
import com.kosuke.utils.Roles;
import com.kosuke.utils.Status;

/**
 * The UserController Class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	GlobalController globalController;

	@Autowired
	TaskService taskService;

	@Autowired
	UserService userService;

	@RequestMapping("/")
	public String root(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("root");
		return "login";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("login");
		return "login";
	}

	@RequestMapping("/home")
	public String home(Model model) {
		Task task = new Task();
		model.addAttribute("reqTask", task);
		model.addAttribute("allTask",
				taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.ACTIVE.getValue()));
		model.addAttribute("allPassiveTask",
				taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.PASSIVE.getValue()));
		logger.info("home");
		return "home";
	}

	@RequestMapping("/admin")
	public String helloAdmin() {
		logger.info("admin");
		return "admin";
	}

	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("register");
		return "register";
	}

	@RequestMapping(value = { "/user/register" }, method = RequestMethod.POST)
	public String register(@ModelAttribute("reqUser") User reqUser, final RedirectAttributes redirectAttributes) {

		logger.info("/user/register");
		User user = userService.findByUserName(reqUser.getUsername());
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-name");
			return "redirect:/register";
		}
		user = userService.findByEmail(reqUser.getEmail());
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-email");
			return "redirect:/register";
		}

		reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
		reqUser.setRole(Roles.ROLE_USER.getValue());

		if (userService.save(reqUser) != null) {
			redirectAttributes.addFlashAttribute("saveUser", "success");
		} else {
			redirectAttributes.addFlashAttribute("saveUser", "fail");
		}

		return "redirect:/register";
	}

}

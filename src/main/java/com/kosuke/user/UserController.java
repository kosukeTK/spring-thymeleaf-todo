package com.kosuke.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.global.GlobalController;
import com.kosuke.todo.Task;
import com.kosuke.todo.TaskService;
import com.kosuke.token.ConfirmationToken;
import com.kosuke.token.ConfirmationTokenService;
import com.kosuke.user.email.EmailSender;
import com.kosuke.utils.PassEncoding;
import com.kosuke.utils.Roles;
import com.kosuke.utils.Status;
import com.kosuke.utils.TaskCategories;

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
	
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	
	@Autowired
	private EmailSender emailSendar;

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
		//有効task
		model.addAttribute("allTask", taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.ACTIVE.getValue()));
		//無効task
		model.addAttribute("allPassiveTask",
				taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.PASSIVE.getValue()));
		//categoryリスト
		model.addAttribute("allCategory", TaskCategories.values());
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
	
	@RequestMapping(value = { "/user/register"}, method = RequestMethod.POST)
	public String register(@ModelAttribute("reqUser") User reqUser, final RedirectAttributes redirectAttributes) {

		logger.info("/user/register");
		//名前重複チェック
		User user = userService.findByUserName(reqUser.getUsername());
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-name");
			return "redirect:/register";
		}
		//メール重複チェック
		user = userService.findByEmail(reqUser.getEmail());
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-email");
			return "redirect:/register";
		}
		
		reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
		reqUser.setRole(Roles.ROLE_USER.getValue());
			
		//ユーザー登録
		if (userService.save(reqUser) != null) {
			//トークン作成
			String token = UUID.randomUUID().toString();
			//トークン登録
			ConfirmationToken confirmationToken = new ConfirmationToken(
					token, 
					LocalDateTime.now(), 
					LocalDateTime.now().plusMinutes(15), 
					reqUser);
			 confirmationTokenService.saveConfirmationToken(
		                confirmationToken);
			 //メール送信
			 String link = "http://localhost:8080/confirm?token=" + token;
			 emailSendar.send(
					 reqUser.getEmail(),
					 emailSendar.buildEmail(reqUser.getUsername(), link));
			redirectAttributes.addFlashAttribute("saveUser", "success");
		} else {
			redirectAttributes.addFlashAttribute("saveUser", "fail");
		}

		return "redirect:/register";
	}
	
	@GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
	}
}

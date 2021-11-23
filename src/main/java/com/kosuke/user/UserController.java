package com.kosuke.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.admin.AdminService;
import com.kosuke.global.GlobalController;
import com.kosuke.todo.Task;
import com.kosuke.todo.TaskService;
import com.kosuke.token.ConfirmationToken;
import com.kosuke.token.ConfirmationTokenService;
import com.kosuke.user.email.EmailSender;
import com.kosuke.utils.LogUtils;
import com.kosuke.utils.PassEncoding;
import com.kosuke.utils.Roles;
import com.kosuke.utils.Status;
import com.kosuke.utils.TaskCategories;

import lombok.AllArgsConstructor;

/**
 * The UserController Class
 *
 * @author kosuke takeuchi
 * @version 1.0 Date 2021/8/15.
 */
@Controller
@AllArgsConstructor
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final GlobalController globalController;

	private final TaskService taskService;

	private final UserService userService;

	private final AdminService adminService;

	private final ConfirmationTokenService confirmationTokenService;

	private final EmailSender emailSendar;

	private final MessageSource messagesource;

	/**
	 * ログイン初期表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	public String root(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("root");
		LogUtils.info("LogUtils info");
		return "login";
	}

	/**
	 * ログイン初期表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("login");
		return "login";
	}

	/**
	 * ホーム初期表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/home")
	public String home(Model model) {
		Task task = new Task();
		model.addAttribute("reqTask", task);
		taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.ACTIVE.getValue());
		// 有効task
		model.addAttribute("allTask",
				taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.ACTIVE.getValue()));
		// 無効task
		model.addAttribute("allPassiveTask",
				taskService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.PASSIVE.getValue()));
		// categoryリスト
		model.addAttribute("allCategory", TaskCategories.values());
		logger.info("home");
		return "home";
	}

	/**
	 * 管理初期表示
	 * @return
	 */
	@RequestMapping("/admin")
	public String helloAdmin(Model model) {
		List<User> emailList = adminService.findEmail();
		model.addAttribute("emailList", emailList);
		model.addAttribute("reqUser", new User());
		model.addAttribute("reqTask", new Task());
		return "admin";
	}

	/**
	 * 登録初期表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("reqUser", new User());
		logger.info("register");
		return "register";
	}

	/**
	 * 登録機能
	 * 
	 * @param reqUser
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = { "/user/register" }, method = RequestMethod.POST)
	public String register(@ModelAttribute("reqUser") User reqUser, final RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		logger.info("/user/register");
		// 名前重複チェック
		User user = userService.findByUserName(reqUser.getUsername());
//		user.getTaskListJPA().stream()
//			.flatMap(task -> task.getImageListJPA().stream())
//			.forEach(System.out::println);
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-name");
			return "redirect:/register";
		}
		// メール重複チェック
		user = userService.findByEmail(reqUser.getEmail());
		if (user != null) {
			redirectAttributes.addFlashAttribute("saveUser", "exist-email");
			return "redirect:/register";
		}

		reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
		reqUser.setRole(Roles.ROLE_USER.getValue());
		// ユーザー登録
		if (userService.save(reqUser) != null) {
			String URL = request.getRequestURL().toString();
			if (URL.contains("localhost")) {
				redirectAttributes.addFlashAttribute("saveUser", "success");
			} else {
				// トークン作成
				String token = UUID.randomUUID().toString();
				// トークン登録
				ConfirmationToken confirmationToken = new ConfirmationToken(
						token, 
						LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(15), 
						reqUser);
				confirmationTokenService.saveConfirmationToken(confirmationToken);
				// メール送信
				String link = "http://localhost:8080/confirm?token=" + token;
				emailSendar.send(reqUser.getEmail(), emailSendar.buildEmail(reqUser.getUsername(), link));
				redirectAttributes.addFlashAttribute("saveUser", "success");
			}
		} else {
			redirectAttributes.addFlashAttribute("saveUser", "fail");
		}

		return "redirect:/register";
	}

	/**
	 * メール認証機能
	 * 
	 * @param token
	 * @param model
	 * @return
	 */
	@GetMapping(path = "/confirm")
	public String confirm(@RequestParam("token") String token, Model model) {
		userService.confirmToken(token);
		model.addAttribute("msg", "confirm");
		model.addAttribute("msgText", "confirmed! Please login!!");
		return "/login";
	}

	@GetMapping("/message")
	public String showMessage(Model model) {
		String message = messagesource.getMessage("hoge", new String[] { "fuga" }, Locale.JAPAN);
		model.addAttribute("msg", message);
		return "home";
	}
}

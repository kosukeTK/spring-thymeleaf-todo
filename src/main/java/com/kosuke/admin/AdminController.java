package com.kosuke.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	/**
	 * ユーザー、タスク情報表示
	 * @param email
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(path="/admin/getUserTodoInfo")
	public String getUserTodoInfo(	@ModelAttribute("email") String email,
									@ModelAttribute("sort") String sortColumn,
									final RedirectAttributes redirectAttributes) {
		//ユーザ
		List<User> userList = adminService.findByEmailGetUserTodo(email, sortColumn);
		//タスク
		List<Task> taskList = userList.stream()
				.flatMap(ulist -> ulist.getTaskList().stream())
				.collect(Collectors.toList());
		//タスクソート
		Map<String,String> taskSort = new HashMap<>();
		taskSort.put("予定日",	"task_date");
		taskSort.put("カテゴリー",	"category");
		taskSort.put("タスク名",	"task_name");
		taskSort.put("ステータス",	"status");
		taskSort.put("説明",		"description");
		taskSort.put("内容",		"content");
		
		redirectAttributes.addFlashAttribute("userList", userList);
		redirectAttributes.addFlashAttribute("taskList", taskList);
		redirectAttributes.addFlashAttribute("taskSort", taskSort);
		return "redirect:/admin";
	}
	
	/**
	 * ユーザー情報更新
	 * @param reqUser
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(path="/admin/editUser")
	public String editUser(
			@ModelAttribute("reqUser") User reqUser,
			final RedirectAttributes redirectAttributes) {
		
			Boolean boo = adminService.userUpdate(reqUser);
			String msg = boo ? "更新しました。" : "更新に失敗しました";
			
			redirectAttributes.addFlashAttribute("msg", msg);
			redirectAttributes.addFlashAttribute("email", reqUser.getEmail());
		return "redirect:/admin/getUserTodoInfo";
	}
	
	/**
	 * ユーザー情報新規作成
	 * @param reqUser
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(path="/admin/insertUser")
	public String insertUser(@ModelAttribute("reqUser") User reqUser,
			final RedirectAttributes redirectAttributes) {
		Boolean boo = adminService.userInsert(reqUser);
		
		String msg = boo ? "新規作成しました。" : "新規作成に失敗しました";
		
		redirectAttributes.addFlashAttribute("msg", msg);
		redirectAttributes.addFlashAttribute("email", reqUser.getEmail());
		return "redirect:/admin/getUserTodoInfo";
	}
	
	/**
	 * タスク情報削除
	 * @param reqTask
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(path="/admin/deleteTask" /*, produces="application/json; charset=UTF-8" */)
	@ResponseBody
	public String deleteTask(@RequestBody JsonModel json, RedirectAttributes redirectAttributes) {
//	public ResponseEntity<String> deleteTask(@RequestParam("task_id") int taskId) {
//		return new ResponseEntity<>("tinpo", HttpStatus.OK);
		adminService.deleteTask(json.getTaskJsonList());
		
		String msg = "削除しました。";
//		redirectAttributes.addFlashAttribute("msg", msg);
//		return "redirect:/admin/getUserTodoInfo";
		return msg;
	}
}

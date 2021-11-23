package com.kosuke.admin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosuke.user.User;
import com.kosuke.utils.PassEncoding;

@Service
public class AdminService {

	private final AdminMapper adminMapper;
	
	@Autowired
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}

	public List<User> findEmail() {
		List<User> list = adminMapper.findEmail();
//		return adminMapper.getEmail();
		return list;
		
	}

	/**
	 * ユーザー、タスク情報表示
	 * @param email
	 * @return
	 */
	public List<User> findByEmailGetUserTodo(String email, String sortColumn) {
		return adminMapper.findByEmailGetUserTodo(email, sortColumn);
	}
	
	/**
	 * ユーザー情報更新
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean userUpdate(User user) {
		if(!StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(PassEncoding.getInstance().passwordEncoder.encode(user.getPassword()));
		}
		return adminMapper.userUpdate(user);
		
	}

	/**
	 * ユーザー情報新規作成
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean userInsert(User user) {
		if(!StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(PassEncoding.getInstance().passwordEncoder.encode(user.getPassword()));
		}
		return adminMapper.userInsert(user);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteTask(List<TaskJsonList> taskJsonList) {
		taskJsonList.forEach(taskJson -> {
			Boolean boo = adminMapper.deleteTask(taskJson.getId());
			if(!boo) {
				throw new IllegalStateException("delete error");
			}
		});
		return true;
	}
}

package com.kosuke.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosuke.user.User;

@Service
public class AdminService {

	private final AdminMapper adminMapper;
	
	@Autowired
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}

	@Transactional
	public void findAll() {
//		adminMapper.findAll();
	}

	public List<User> getEmail() {
		List<User> list = adminMapper.getEmail();
//		return adminMapper.getEmail();
		return list;
		
	}

}

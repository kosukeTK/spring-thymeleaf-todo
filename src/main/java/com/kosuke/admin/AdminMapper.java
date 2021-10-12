package com.kosuke.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kosuke.user.User;

@Mapper
public interface AdminMapper {

	List<User> findEmail();

	List<User> findByEmailGetUserTodo(String email);

}

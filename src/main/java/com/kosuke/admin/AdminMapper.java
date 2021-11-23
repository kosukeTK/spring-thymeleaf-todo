package com.kosuke.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kosuke.user.User;

@Mapper
public interface AdminMapper {

	List<User> findEmail();

	List<User> findByEmailGetUserTodo(String email, String sortColumn);

	Boolean userUpdate(User user);

	Boolean userInsert(User user);

	Boolean deleteTask(Integer id);

}

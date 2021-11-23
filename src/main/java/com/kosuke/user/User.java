package com.kosuke.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kosuke.todo.Task;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The User Entity class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "user", schema = "sampledb")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Transient
	@Column(name = "password_2")
	private String password_2;

	@Column(name = "email")
	private String email;

	@Column(name = "role")
	private int role;
	
	@Column(name = "enabled")
	private boolean enabled = false;
	
	@Column(name = "locked")
	private boolean locked = false;
	
	//MyBatisで使用	
	@Transient
	private List<Task> taskList;
	
	@OneToMany(mappedBy = "user", 
	//		fetch = FetchType.EAGER, 
			cascade=CascadeType.ALL)
	@ToString.Exclude
	private List<Task> taskListJPA;

	public User(String username, String password, String email, int role) {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setRole(role);
	}
}

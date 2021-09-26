package com.kosuke.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;

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
public class User {

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
	
//	public User() {
//	}

	public User(String username, String password, String email, int role) {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setRole(role);

	}

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getPassword_2() {
//		return password_2;
//	}
//
//	public void setPassword_2(String password_2) {
//		this.password_2 = password_2;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public int getRole() {
//		return role;
//	}
//
//	public void setRole(int role) {
//		this.role = role;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		User user = (User) o;
//		return id == user.id && role == user.role && Objects.equals(username, user.username)
//				&& Objects.equals(password, user.password) && Objects.equals(password_2, user.password_2)
//				&& Objects.equals(email, user.email);
//	}
//
//	@Override
//	public int hashCode() {
//
//		return Objects.hash(id, username, password, password_2, email, role);
//	}
//
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public boolean isLocked() {
//		return locked;
//	}
//
//	public void setLocked(boolean locked) {
//		this.locked = locked;
//	}
}

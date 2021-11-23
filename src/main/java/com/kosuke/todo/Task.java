package com.kosuke.todo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kosuke.image.Image;
import com.kosuke.user.User;
import com.kosuke.utils.TaskCategories;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Task Entity class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "task", schema = "sampledb")
@JsonPropertyOrder({"ID", "タスク名", "内容", "説明"})
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	@JsonProperty("ID")
	private int id;
	
//	@NotNull //nullの場合エラー
	@Column(name = "user_name", length = 20)
	private String userName;
	
//	@NotEmpty //null or 空文字の場合エラー
	@NotBlank(message = "{Task.taskName.invalid}")
	@Column(name = "task_name", length = 20)
	@JsonProperty("タスク名")
	private String taskName;
	
	@NotBlank(message = "{Task.description.invalid}") // null or 空文字 or 空白の場合エラー
	@Column(name = "description", length = 20)
	@JsonProperty("説明")
	private String description;

	@Column(name = "content", length = 50)
	@JsonProperty("内容")
	private String content;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "task_date", nullable = false)
	@JsonProperty("実施日")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate taskDate;

	@Column(name = "create_date")
	@JsonProperty("作成日")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", length = 10)
	@JsonProperty("カテゴリー")
	private TaskCategories category;

	@Column(name = "status", length = 5)
	@JsonProperty("状態")
	private String status;

	@Column(name = "user_id", length = 2)
	@JsonIgnore
	private int userId;
	
	@Column(name = "image_dir", length = 100)
	@JsonIgnore
	private String imageDir;
	
	@Transient
	@Column(name = "task_image")
	@JsonIgnore
	private MultipartFile taskImage;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="user_id", referencedColumnName="id", insertable=false, updatable=false),
		@JoinColumn(name="user_name", referencedColumnName="username", insertable=false, updatable=false)
	})
//	@JoinColumn(name="user_id", referencedColumnName="id", insertable=false, updatable=false)
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<Image> imageListJPA;
	
	//Mybatisで使用
	@Transient
	private List<Image> imageList;
	
}

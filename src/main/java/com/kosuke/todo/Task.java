package com.kosuke.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kosuke.image.Image;
import com.kosuke.utils.TaskCategories;

import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	@JsonProperty("ID")
	private int id;

	@Column(name = "task_name")
	@JsonProperty("タスク名")
	private String taskName;

	@Column(name = "description")
	@JsonProperty("説明")
	private String description;

	@Column(name = "content")
	@JsonProperty("内容")
	private String content;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "task_date", nullable = false)
	@JsonProperty("実施日")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate taskDate;

	@Column(name = "create_date")
	@JsonProperty("作成日")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	@JsonProperty("カテゴリー")
	private TaskCategories category;

	@Column(name = "status")
	@JsonProperty("状態")
	private String status;

	@Column(name = "user_id")
	@JsonIgnore
	private int userId;
	
	@Column(name = "image_dir")
	@JsonIgnore
	private String imageDir;
	
	@Transient
	@Column(name = "task_image")
	@JsonIgnore
	private MultipartFile taskImage;
	
	@Transient
	@OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Image> images = new ArrayList<>();
}

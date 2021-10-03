package com.kosuke.image;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.kosuke.todo.Task;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@Table(name = "images", schema = "sampledb")
@IdClass(value=ImageKey.class)
public class Image implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable = false)
	private int id;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "task_id", nullable = false)
    private Task taskId;
	
	@Column(name="image_name")
	private String imageName;
	
	@Column(name="image_path")
	private String imagePath;
	
	@Column(name="image_data")
	private Blob imageData;
	
	@Column(name="create_date")
	private LocalDateTime createDate;
	
	
}

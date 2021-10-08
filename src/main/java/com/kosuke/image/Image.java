package com.kosuke.image;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import com.kosuke.todo.Task;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@Table(name = "images", schema = "sampledb")
//@IdClass(value=ImageKey.class)
public class Image implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable = false)
	private int id;
	
	@Column(name="image_name")
	private String imageName;
	
	@Column(name="image_path")
	private String imagePath;
	
	@Lob // @Lobはサイズが大きいデータのカラムにつける。@Typeがないと「bigintのデータが出力されてますよ」的なエラーが出る
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name="image_data")
	private byte[] imageData;
	
	@Column(name="create_date")
	private LocalDateTime createDate;
	
	@ManyToOne
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;
	
	@Transient
	private int taskId;
	
	@Transient
	private List<MultipartFile> files;
	
	public Image(List<MultipartFile> files) {
		this.files = files;
	};

	public Image(String imageName, String imagePath, byte[] imageData, LocalDateTime createDate, Task task) {
		super();
		this.imageName = imageName;
		this.imagePath = imagePath;
		this.imageData = imageData;
		this.createDate = createDate;
		this.task = task;
	}
	
	
	
	
}

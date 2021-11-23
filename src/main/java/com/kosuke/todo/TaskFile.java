package com.kosuke.todo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TaskFile {

	private MultipartFile taskImage;
	
}

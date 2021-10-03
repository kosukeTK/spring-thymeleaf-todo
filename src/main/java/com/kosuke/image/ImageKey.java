package com.kosuke.image;

import java.io.Serializable;

import com.kosuke.todo.Task;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ImageKey implements Serializable {
	
	private int id;
	
	private Task taskId;
	
}

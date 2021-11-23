package com.kosuke.todo;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class EditAllTaskList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Valid
	private List<Task> editAllTasks;

}

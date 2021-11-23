/**
 * 
 */
package com.kosuke.image;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.kosuke.todo.Task;

/**
 * @author torit
 *
 */
public interface ImageService {
	
	public List<Image> saveImage(Image image, Task task);
	
	public ResponseEntity<StreamingResponseBody> getAllImages(Task task, HttpServletResponse response);

	public List<Image> findByImageName(String imageName, int taskId);

	public List<Image> findByTaskId(int taskId);
}

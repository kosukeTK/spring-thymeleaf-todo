/**
 * 
 */
package com.kosuke.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kosuke.todo.Task;

/**
 * @author torit
 *
 */
public interface ImageService {
	
	public void saveImage(int id, Image reqImage, List<MultipartFile> files);
}

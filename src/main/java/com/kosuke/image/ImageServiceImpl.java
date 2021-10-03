package com.kosuke.image;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageServiceImpl implements ImageService {

	@Override
	public void saveImage(int id, Image reqImage, List<MultipartFile> files) {
		files.forEach(file -> {
			reqImage.setCreateDate(LocalDateTime.now());
		});
	};
	
	
}

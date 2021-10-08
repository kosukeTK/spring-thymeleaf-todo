package com.kosuke.image;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
	
	List<Image> findByImageName(String imageName, int taskId);
	
}

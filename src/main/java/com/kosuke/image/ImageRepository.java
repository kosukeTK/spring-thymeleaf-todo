package com.kosuke.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

	List<Image> findByTaskId(int taskId);

//	@Query("select * from images i where  ")
//	void findByTaskId(int id);
	
}

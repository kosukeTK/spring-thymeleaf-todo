package com.kosuke.todo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosuke.config.Property;

import lombok.AllArgsConstructor;

/**
 * The TaskServiceImpl class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    
    private final Property property;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Boolean delete(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Task update(Task task) {
        return taskRepository.save(task);
    }
    
    @Override
    public List<Task> updateAll(List<Task> task) {
    	return (List<Task>) taskRepository.saveAll(task);
    }

    @Override
    public Task findById(int id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findByUserIdStatus(int userId, String status) {
        return  taskRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Task> findBetween(int start, int end) {
        return taskRepository.findBetween(start, end);
    }
    
    
    /**
     *
     * 対象ユーザのタスクIDのMAXを取得
     * @param	userId
     * @return	maxTaskId
     */
    @Override
    public int findMaxTaskId(int userId) {
    	return taskRepository.findMaxTaskId(userId);
    }
    
    /**
     *
     * 対象ユーザの全てのタスク取得
     * @param	userId
     * @return	maxTaskId
     */
    @Override
    public List<Task> findByUserId(int userId) {
    	return taskRepository.findByUserId(userId);
    }
    
	/**
	 *
	 * ファイルアップロード
	 *
	 * @param reqTask
	 * @return 
	 */
	@Override
	public void uploadTaskImage(Task reqTask, TaskFile reqTaskFile) {
		//ディレクトリ、ファイル名を定義
		String baseDir = property.getBaseDir();
		String userId = Integer.toString(reqTask.getUserId());
		String fileName = reqTaskFile.getTaskImage().getOriginalFilename().toString();
		//タスクIDを定義
//		String taskId = Integer.toString(findMaxTaskId(reqTask.getUserId()) + 1);
		//ファイル存在チェック
		if (reqTaskFile.getTaskImage().isEmpty()) {
			return;
		}
		//ファイル種類チェック
		if (!Arrays.asList(
				ContentType.IMAGE_JPEG.getMimeType(),
				ContentType.IMAGE_PNG.getMimeType()).contains(reqTaskFile.getTaskImage().getContentType())) {
			return;
		}
		try {
			//ユーザーディレクトリを作成
			File uploadUserDirFile = new File(baseDir, userId);
			if(!uploadUserDirFile.isDirectory()) {
				uploadUserDirFile.mkdir();
			}
//			//タスクディレクトリを作成
//			File uploadDirFile = new File(uploadUserDirFile, taskId);
//			if(!uploadDirFile.exists()) {
//				uploadDirFile.mkdir();
//			}
			//ファイルをアップロード
			File uploadFile = new File(uploadUserDirFile + "/" + fileName);
			byte[] bytes = reqTaskFile.getTaskImage().getBytes();
			BufferedOutputStream uploadFileStream = 
					new BufferedOutputStream(new FileOutputStream(uploadFile));
			uploadFileStream.write(bytes);
			uploadFileStream.flush();
			uploadFileStream.close();
		} catch (Exception e) {
			System.out.println("ファイルアップロード失敗");
		}
	}
}

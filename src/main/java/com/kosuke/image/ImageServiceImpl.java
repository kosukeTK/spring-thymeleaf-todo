package com.kosuke.image;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.kosuke.config.Property;
import com.kosuke.todo.Task;

@Service
@Transactional(rollbackFor = Exception.class)
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ImageMapper imageMapper;

	@Autowired
	private Property property;

	/**
	 * 複数ファイルをディレクトリとDBに保存する
	 * @param image
	 * @param task
	 * @return List<Image>
	 */
	@Override
	public List<Image> saveImage(Image image, Task task) {

		String baseDir = property.getBaseDir();
		File taskDir = new File(baseDir, Integer.toString(task.getId()));
		// ファイルをディレクトリに配置
		image.getFiles().forEach(multiPartFile -> {
			try {
				if (!FileUtils.isDirectory(taskDir)) {
					FileUtils.forceMkdir(taskDir);
				}
				File taskImage = new File(taskDir, multiPartFile.getOriginalFilename());
				byte[] byteArray = multiPartFile.getBytes();
				FileUtils.writeByteArrayToFile(taskImage, byteArray);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		// ファイル情報を保存
		List<Image> fileList = image.getFiles().stream().map(multiPartFile -> {
			Image imageConst = null;
			try {
				 imageConst = new Image(
					multiPartFile.getOriginalFilename(),
					property.getBaseDir() + Integer.toString(task.getId()), 
					multiPartFile.getBytes(), 
					LocalDateTime.now(), 
					task.getId()
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return imageConst;
		}).collect(Collectors.toList());
		List<Image> result = imageRepository.saveAll(fileList);
		return result;
	}

	/**
	 * タスクIDをキーとしてディレクトリから画像をダウンロードする 
	 * 画像が見つからない場合は、DBからバイナリを取得してダウンロードする
	 * @param  task
	 * @param  response
	 * @return ResponseEntity<StreamingResponseBody>
	 */
	@Override
	public ResponseEntity<StreamingResponseBody> getAllImages(Task task, HttpServletResponse response) {
		String baseDir = property.getBaseDir();
		File taskDir = new File(baseDir, Integer.toString(task.getId()));
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename=" + task.getTaskName() + ".zip");
		StreamingResponseBody stream = _out -> {
			// ディレクトリから画像ダウンロード
			if (taskDir.exists() && taskDir.isDirectory() && taskDir.listFiles().length != 0) {
				try {
					ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
					for (File file : taskDir.listFiles()) {
						BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
						ZipEntry zipEntry = new ZipEntry(file.getName());
						out.putNextEntry(zipEntry);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = in.read(bytes)) != -1) {
							out.write(bytes, 0, length);
							out.flush();
						}
						in.close();
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// DBから画像ダウンロード
			} else {
				ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
				task.getImageListJPA().forEach(file -> {
					try {
						ByteArrayInputStream in = new ByteArrayInputStream(file.getImageData());
						ZipEntry zipEntry = new ZipEntry(file.getImageName());
						out.putNextEntry(zipEntry);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = in.read(bytes)) != -1) {
							out.write(bytes, 0, length);
							out.flush();
						}
						in.close();
					} catch (IOException e2) {
						// TODO: handle exception
					}
				});
				out.close();
			}
		};
		return new ResponseEntity<StreamingResponseBody>(stream, HttpStatus.OK);
	}

	@Override
	public List<Image> findByImageName(String imageName, int taskId) {
		return imageMapper.findByImageName(imageName, taskId);
	}

	@Override
	public List<Image> findByTaskId(int taskId) {
		return imageRepository.findByTaskId(taskId);
	}
	
	
}

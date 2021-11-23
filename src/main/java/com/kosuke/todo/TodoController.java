package com.kosuke.todo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosuke.global.GlobalController;
import com.kosuke.image.Image;
import com.kosuke.image.ImageService;
import com.kosuke.utils.OutputCSV;
import com.kosuke.utils.Status;

import javassist.expr.NewArray;
import lombok.AllArgsConstructor;

/**
 * The TodoController  Class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Controller
@AllArgsConstructor
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    
    private final TaskService taskService;
    
    private final ImageService imageService;

    private final GlobalController globalController;
    
    /**
     * TASK新規登録
     * @param reqTask
     * @param redirectAttributes
     * @return home
     */
    @RequestMapping(value = {"/task/saveTask"}, method = RequestMethod.POST)
    public String saveTodo(	@Validated //先頭に配置する
    						@ModelAttribute("reqTask") Task reqTask,
    						BindingResult result, //チェックしたいentityの直後に配置する
    						@ModelAttribute("reqTaskFile") TaskFile reqTaskFile,
                           	final RedirectAttributes redirectAttributes,
                           	Model model) {
        logger.info("/task/save");
        
        //Validateチェック
        if(result.hasErrors()) {
        	List<String> errorList = result.getAllErrors().stream()
        							.map(e -> e.getDefaultMessage())
        						 	.collect(Collectors.toList());
        	redirectAttributes.addFlashAttribute("validationError", errorList);
        	return "redirect:/home";
        }
        try {
            reqTask.setCreateDate(LocalDateTime.now());
            reqTask.setStatus(Status.ACTIVE.getValue());
            reqTask.setUserId(globalController.getLoginUser().getId());
            if(!reqTaskFile.getTaskImage().getOriginalFilename().equals("")) {
            	taskService.uploadTaskImage(reqTask ,reqTaskFile);
            }
            taskService.save(reqTask);
            redirectAttributes.addFlashAttribute("msg", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "fail");
            logger.error("save: " + e.getMessage());
        }

        return "redirect:/home";
    }

    /**
     * TASK編集
     * @param editTask
     * @param model
     * @return edit
     */
    @RequestMapping(value = {"/task/editTask"}, method = RequestMethod.POST)
    public String editTodo(@ModelAttribute("editTask") Task editTask, Model model) {
        logger.info("/task/editTask");
        try {
            Task task = taskService.findById(editTask.getId());
            if (!task.equals(editTask)) {
                taskService.update(editTask);
                model.addAttribute("msg", "success");
            } else {
                model.addAttribute("msg", "same");
            }
        } catch (Exception e) {
            model.addAttribute("msg", "fail");
            logger.error("editTask: " + e.getMessage());
        }
        model.addAttribute("editTodo", editTask);
        return "edit";
    }
    
    
    /**
     * 全タスク編集画面へ遷移する
     * @param userId
     * @param model
     * @return
     */
    @GetMapping(path = "/task/editAllTask/{userId}")
    public String editAllTransition(	@PathVariable("userId") int userId,
    									Model model) {
    	logger.info("/task/editTaskAll");
    	List<Task> allTaskByUserId = taskService.findByUserId(userId);
    	
    	EditAllTaskList editAllTaskList = new EditAllTaskList();
    	editAllTaskList.setEditAllTasks(allTaskByUserId);
    	model.addAttribute("editAllTaskList", editAllTaskList);
    	return "editAll";
    }
    /**
     * 全タスク編集する
     * @param userId
     * @param mav
     * @return
     */
    @PostMapping(path = "/edit/editAllTask/{userId}")
    public String editTaskAll(	@Validated
    							@ModelAttribute EditAllTaskList editAllTaskList,
    							BindingResult result,
    							RedirectAttributes redirectAttributes) {
    	logger.info("/edit/editAllTask");
    	if(result.hasErrors()) {
    		List<String> errorList = 
    				result.getAllErrors().stream()
    				.map(x -> x.getDefaultMessage())
    				.collect(Collectors.toList());
    		redirectAttributes.addFlashAttribute("validationError", errorList);
    		return "redirect:/task/editAllTask/{userId}";
    	}
    	try {
    		if (!taskService.updateAll(editAllTaskList.getEditAllTasks()).isEmpty()) {
    			redirectAttributes.addFlashAttribute("msg", "更新しました。");
    		} else {
    			redirectAttributes.addFlashAttribute("msg", "更新対象がありません。");
    		}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", e.getMessage());
		}
    	return "redirect:/task/editAllTask/{userId}";
    }
    
    /**
     * TASK削除、編集
     * @param operation
     * @param id
     * @param redirectAttributes
     * @param model
     * @return
     */
    @RequestMapping(value = "/task/{operation}/{id}", method = RequestMethod.GET)
    public String todoOperation(@PathVariable("operation") String operation,
                                @PathVariable("id") int id, final RedirectAttributes redirectAttributes,
                                Model model) {

        logger.info("/task/operation: {} ", operation);
        if (operation.equals("trash")) {
            Task task = taskService.findById(id);
            if (task != null) {
                task.setStatus(Status.PASSIVE.getValue());
                taskService.update(task);
                redirectAttributes.addFlashAttribute("msg", "trash");
            } else {
                redirectAttributes.addFlashAttribute("msg", "notfound");
            }
        }
        if (operation.equals("restore")) {
            Task task = taskService.findById(id);
            if (task != null) {
                task.setStatus(Status.ACTIVE.getValue());
                taskService.update(task);
                redirectAttributes.addFlashAttribute("msg", "active");
                redirectAttributes.addFlashAttribute("msgText", "Task " + task.getTaskName() + " Restored Successfully.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "active_fail");
                redirectAttributes.addFlashAttribute("msgText", "Task Activation failed !!! Task:" + task.getTaskName());

            }
        } else if (operation.equals("delete")) {
            if (taskService.delete(id)) {
                redirectAttributes.addFlashAttribute("msg", "del");
                redirectAttributes.addFlashAttribute("msgText", " Task deleted permanently");
            } else {
                redirectAttributes.addFlashAttribute("msg", "del_fail");
                redirectAttributes.addFlashAttribute("msgText", " Task could not deleted. Please try later");
            }
        } else if (operation.equals("edit")) {
            Task editTask = taskService.findById(id);
            if (editTask != null) {
                model.addAttribute("editTask", editTask);
                return "edit";
            } else {
                redirectAttributes.addFlashAttribute("msg", "notfound");
            }
        }
        return "redirect:/home";
    }

	/**
	 * FILEダウンロード
	 * @param response
	 * @return
	 */
	@GetMapping (value = "/download/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> download(
    		@PathVariable("userId") int userId, 
    		final HttpServletResponse response) {
		
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowTime = dateTimeFormatter.format(localDateTime);
		
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=" + nowTime + ".zip");
        
        StreamingResponseBody stream = out -> {
            final File directory = new File(
            		"C:\\Users\\torit\\eclipse-workspace\\todo\\src\\main\\resources\\static\\image\\" + Integer.toString(userId));
            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

            if(directory.exists() && directory.isDirectory()) {
                try {
                    for (final File file : directory.listFiles()) {
                        final InputStream inputStream=new FileInputStream(file);
                        final ZipEntry zipEntry=new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);
                        byte[] bytes=new byte[1024];
                        int length;
                        while ((length=inputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        inputStream.close();
                    }
                    zipOut.close();
                } catch (final IOException e) {
                    logger.error("Exception while reading and streaming data {} ", e);
                }
            }
        };
        System.out.println(stream);
        logger.info("steaming response {} ", stream);
        return new ResponseEntity<StreamingResponseBody>(stream, HttpStatus.OK);
    }
	
	/**
	 * CSV作成
	 * @param userId
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping(value="/csv/{userId}/*.csv",//URLパスパラメータ
//	@GetMapping(value="/csv", 				//リクエストパラメータ
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE + "; "
						+ "charset=Shift_JIS; Content-Disposition: attachment")
	@ResponseBody
	public Object getCsv(@PathVariable("userId") int userId) throws JsonProcessingException { //URLパスパラメータ
//	public Object getCsv(@RequestParam("userId") int userId) throws JsonProcessingException { //リクエストパラメータ
		List<Task> task = taskService.findByUserId(userId);
		return OutputCSV.write(task);
	}
	
	/**
	 * タスクの画像一覧を表示
	 * @param taskId
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(path = "/task/image/{taskId}")
	public String displayTaskImage(	@PathVariable("taskId") int taskId,
									Model model) throws UnsupportedEncodingException {
		List<Image> imageList = imageService.findByTaskId(taskId);
		
		List<String> encodeImageList = new ArrayList<>();
		for (Image image : imageList) {
			if(image.getImageData() == null) continue;
			String encodeImage = new String(Base64.encodeBase64(image.getImageData()), "ASCII");
			encodeImageList.add(encodeImage);
		}
		model.addAttribute("image", new Image());
		model.addAttribute("imageList", imageList);
		model.addAttribute("encodeImageList", encodeImageList);
		
		return "images";
	}
	
	/**
	 * 複数ファイル保存
	 * @param id
	 * @param files
	 * @param model
	 * @return 
	 */
	@PostMapping(path="/image/save/{taskId}")
	public String saveImage(@PathVariable("taskId")  int taskId, 
							@ModelAttribute Image image,
							BindingResult result,
							RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			System.out.println(result.getFieldError());
			redirectAttributes.addAttribute("msg", "failed");
			return "redirect:/task/image/{taskId}";
		}
		
		Task task = taskService.findById(taskId);
		//ディレクトリにファイルを配置、DBにファイルを保存
		if(!CollectionUtils.isEmpty(imageService.saveImage(image, task))) {
			redirectAttributes.addAttribute("msg", "success");
		} else {
			redirectAttributes.addAttribute("msg", "fail");
		}
		return "redirect:/task/image/{taskId}";
	}
	
	
	/**
	 * 全ての画像を取得
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(path="/image/getAll/{id}")
	@ResponseBody
	public ResponseEntity<StreamingResponseBody> getAllImage(@PathVariable("id") int id,
							  final HttpServletResponse response
							  ) {
		Task task = taskService.findById(id);
		return imageService.getAllImages(task, response);
		
	}
	
	@GetMapping(path="/image/{imageName}/{id}")
	public String getImageFindName(	@PathVariable("imageName") String imageName,
									@PathVariable("id") int taskId,
									Model model) {
		List<Image> imageList = imageService.findByImageName(imageName, taskId);
		model.addAttribute("imageList", imageList);
		return "";
	}
}

package com.kosuke.todo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kosuke.global.GlobalController;
import com.kosuke.utils.Status;

/**
 * The TodoController  Class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
@Controller
@ComponentScan
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private GlobalController globalController;

    /**
     * TASK新規登録
     * @param reqTask
     * @param redirectAttributes
     * @return home
     */
    @RequestMapping(value = {"/task/saveTask"}, method = RequestMethod.POST)
    public String saveTodo(@ModelAttribute("reqTask") Task reqTask,
                           final RedirectAttributes redirectAttributes) {
        logger.info("/task/save");
        try {
            reqTask.setCreateDate(LocalDateTime.now());
            reqTask.setStatus(Status.ACTIVE.getValue());
            reqTask.setUserId(globalController.getLoginUser().getId());
            if(!reqTask.getTaskImage().getOriginalFilename().equals("")) {
            	taskService.uploadTaskImage(reqTask);
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
//            final String home = System.getProperty("user.home");
//        	File directory = new File(task.getImageDir());
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
		CsvMapper mapper = new CsvMapper();
		mapper.registerModules(new JavaTimeModule());
		CsvSchema schema = mapper.schemaFor(Task.class).withHeader();
		return mapper.writer(schema).writeValueAsString(task);
	}
}

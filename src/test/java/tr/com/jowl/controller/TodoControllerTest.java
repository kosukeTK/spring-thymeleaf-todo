package tr.com.jowl.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.kosuke.todo.TodoController;

class TodoControllerTest {
	
//	@Autowired
//	private TodoController todo;
	
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void download() {
//		HttpServletResponse response = mock(HttpServletResponse.class); 
//		TodoController todo = new TodoController();
//		ResponseEntity<StreamingResponseBody> responseEntity = todo.download(8, response);
//		assertNotNull(responseEntity);
	}
	
	@Test
	@Disabled
	public void dateTest() {
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowTime = dateTimeFormatter.format(localDateTime);
		System.out.print(nowTime);
	}

}

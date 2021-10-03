package com.kosuke.image;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.kosuke.user.UserController;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class ImageServiceImplTest {

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private static WebApplicationContext webApplicationContext;
	
    @Autowired
    private static MockMvc mockMvc;
	
	private static MockMultipartFile file;
	
	@BeforeAll
	public static void beforeAll() {
//		System.out.println("beforeAll");
//		file = new MockMultipartFile(
//			"file", 
//			"hello.txt", 
//			MediaType.TEXT_PLAIN_VALUE,
//			"Hello, World!".getBytes());
//		 
//		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	    mockMvc.perform(MockMvcRequestBuilders.multipart("/")
//	    		.file(file))
//	    		.andExpect(status().is(200))
//	    		.andExpect(content().string("success"));
	}

//	@Test
//	void test() {
//		imageService.imageRegist(file);
//	}

//	@Test
//	    public void contextLoads() {
//	}

}

package tr.com.jowl.utils;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

class LogicTest {

	@Test
	public void dateTest() {
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowTime = dateTimeFormatter.format(localDateTime);
		System.out.print(nowTime);
	}
	
	@Test
	public void mapTest() {
		List<String> listTest = new ArrayList<>();
		listTest.add("tomato");
		listTest.add("kinoko");
		listTest.add("tinpo");
		
		List<String> newListTest = listTest.stream().map(test -> {
			return test.concat("です");
		}).collect(Collectors.toList());
//		}).forEach(t-> System.out.println(t));
		;
	}
	
	@Test
	public void byteTest() throws IOException {
		File file = mock(File.class);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		System.out.println(bytes);
	}
	
	@Test
	public void RequestURLTest() throws IOException {
		String str = "abcfefg";
		if (str.contains("b")) {
			System.out.println("test conp");
		}
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		System.out.println(request.getRequestURI());
	}
}

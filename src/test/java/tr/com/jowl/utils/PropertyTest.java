package tr.com.jowl.utils;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kosuke.config.Property;

//@AutoConfigureMockMvc
//@SpringBootTest(classes = DbMvcTestApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropertyTest {
	
	@Autowired
	private MessageSource messageSource;
	
//	@BeforeEach
//	public void setUp() {
//		property = new Property();
//	}
	
	@Test
	public void greet() {
		Property property = new Property();
	    System.out.println("テスト結果" + property.getBaseDir());
	}
	
	@Test
	public void message() {
		String message = messageSource.getMessage("hoge", new String[]{"fuga"}, Locale.JAPAN);
		System.out.println(message);
	}
}

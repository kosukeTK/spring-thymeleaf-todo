package tr.com.jowl.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kosuke.config.Property;

//@AutoConfigureMockMvc
//@SpringBootTest(classes = DbMvcTestApplication.class)
public class PropertyTest {

	private Property property;
	
	@BeforeEach
	public void setUp() {
		property = new Property();
	}
	
	@Test
	public void greet() {
	    System.out.println("テスト結果" + property.getBaseDir());
	}
}

package tr.com.jowl.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosuke.user.User;
import com.kosuke.user.UserController;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	
	RedirectAttributes redirectAttributes;
	
	private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        // Spring MVCのモックを設定する
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(null, null, null, null, null, null)).build();
    }
	
//    @Test
//    void GETでアクセスする() throws Exception {
//
//        // GETで「/」にアクセスする
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                // レスポンスのステータスコードが200であることを検証する
//                .andExpect(status().isOk())
//                // レスポンスボディが「Hello World」であることを検証する
//                .andExpect(content().string("Hello World"));
//    }
	
}

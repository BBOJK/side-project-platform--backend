package bbojk.sideprojectplatformbackend.auth.server;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestConfig.class)
@AutoConfigureMockMvc
public class TokenIssueTest {
    @Autowired
    MockMvc mvc;

    @Test
    void issueAccessToken() throws Exception {
        mvc.perform(post("/token")
                        .header(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "test")
                        .param("password", "password"))
                .andExpect(status().isOk());
    }
}

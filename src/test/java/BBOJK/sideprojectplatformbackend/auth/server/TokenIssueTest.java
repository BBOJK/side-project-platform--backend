package bbojk.sideprojectplatformbackend.auth.server;

import bbojk.sideprojectplatformbackend.auth.TestUserConstant;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenIssueTest {
    @Autowired
    MockMvc mvc;
    private static String refreshToken;

    @Test
    @Order(0)
    void issueAccessTokenSuccessfully_withFormLogin() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", TestUserConstant.TEST_USERNAME)
                        .param("password", TestUserConstant.TEST_PASSWORD))
                .andExpect(status().isOk())
                .andReturn();

        refreshToken = JsonPath.read(mvcResult.getResponse().getContentAsString(),
                "$." + AccessTokenParameterNames.REFRESH_TOKEN);
        assertThat(refreshToken).isNotNull();
    }

    @Test
    @Order(1)
    @Description("Use refresh token issued during form login test to get another access token.")
    void issueAccessTokenSuccessfully_withRefreshToken() throws Exception {
        assertThat(refreshToken).isNotNull();
        mvc.perform(post("/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param(AccessTokenParameterNames.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Description("Fail to issue access token when reuse refresh token used before.")
    void failTokenIssue_whenRefreshTokenIsReused() throws Exception {
        assertThat(refreshToken).isNotNull();
        mvc.perform(post("/token")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param(AccessTokenParameterNames.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized());
    }

}

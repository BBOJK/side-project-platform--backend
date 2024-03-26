package bbojk.sideprojectplatformbackend.auth;

import bbojk.sideprojectplatformbackend.auth.server.AccessTokenParameterNames;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenAuthTest {
    private static String accessToken;
    private static String refreshToken;
    @Autowired
    MockMvc mvc;

    @Test
    @Order(0)
    void issueAccessTokenSuccessfully_withFormLogin() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", TestUserConstant.TEST_USERNAME)
                        .param("password", TestUserConstant.TEST_PASSWORD))
                .andExpect(status().isOk())
                .andReturn();

        String rawJsonResult = mvcResult.getResponse().getContentAsString();

        accessToken = JsonPath.read(rawJsonResult,
                "$." + AccessTokenParameterNames.ACCESS_TOKEN);
        assertThat(accessToken).isNotNull();

        refreshToken = JsonPath.read(rawJsonResult,
                "$." + AccessTokenParameterNames.REFRESH_TOKEN);
        assertThat(refreshToken).isNotNull();
    }

    @Test
    void respondWith400Error_ifAuthFormatNotSupported() throws Exception {
        mvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("not_supported", "OMG"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    @Description("Use refresh token issued during form login test to get another access token.")
    void issueAccessTokenSuccessfully_withRefreshToken() throws Exception {
        assertThat(refreshToken).isNotNull();
        mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(AccessTokenParameterNames.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @Description("Fail to issue access token when reuse refresh token used before.")
    void failTokenIssue_whenRefreshTokenIsReused() throws Exception {
        assertThat(refreshToken).isNotNull();
        mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(AccessTokenParameterNames.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    @Description("Try access to resource /test with access token issued at @Test(0)")
    void accessResource_withAccessToken() throws Exception {
        assertThat(accessToken).isNotNull();
        mvc.perform(get("/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

}

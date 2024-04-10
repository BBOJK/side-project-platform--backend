package bbojk.sideprojectplatformbackend.auth;

import bbojk.sideprojectplatformbackend.auth.server.AccessTokenParameterNames;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static bbojk.sideprojectplatformbackend.ApiDocumentUtils.preprocessDocumentRequest;
import static bbojk.sideprojectplatformbackend.ApiDocumentUtils.preprocessDocumentResponse;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenAuthTest {
    private static String accessToken;
    private static String refreshToken;

    @Autowired
    private MockMvc mvc;

    private static FieldDescriptor[] tokenIssueFields() {
        return new FieldDescriptor[]{fieldWithPath("access_token").description(
                "access token"),
                fieldWithPath("refresh_token").description("refresh token"),
                fieldWithPath("expires_in").description("access token의 유효 시간(초)")};
    }

    @Test
    @Tag("api-doc")
    @Order(0)
    void issueAccessTokenSuccessfully_withFormLogin() throws Exception {
        mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", TestUserConstant.TEST_USERNAME)
                        .param("password", TestUserConstant.TEST_PASSWORD))
                .andExpect(status().isOk())
                .andDo(this::saveAccessToken)
                .andDo(this::saveRefreshToken)
                .andDo(document("form-login-token-issue",
                        preprocessDocumentRequest(),
                        preprocessDocumentResponse(),
                        formParameters(
                                parameterWithName("username").description("유저 id"),
                                parameterWithName("password").description("유저 비밀번호")
                        ),
                        responseFields(tokenIssueFields())
                ));
    }

    private void saveAccessToken(MvcResult mvcResult) throws UnsupportedEncodingException {
        String rawJsonResult = mvcResult.getResponse().getContentAsString();

        accessToken = JsonPath.read(rawJsonResult,
                "$." + AccessTokenParameterNames.ACCESS_TOKEN);
        assertThat(accessToken).isNotNull();
    }

    private void saveRefreshToken(MvcResult mvcResult) throws UnsupportedEncodingException {
        String rawJsonResult = mvcResult.getResponse().getContentAsString();
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
    @Tag("api-doc")
    @Order(1)
    @Description("Use refresh token issued during form login test to get another access token.")
    void issueAccessTokenSuccessfully_withRefreshToken() throws Exception {
        assertThat(refreshToken).isNotNull();
        mvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(AccessTokenParameterNames.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isOk())
                .andDo(document("refresh-token-token-issue",
                        preprocessDocumentRequest(),
                        preprocessDocumentResponse(),
                        formParameters(
                                parameterWithName("refresh_token").description("refresh token")),
                        responseFields(tokenIssueFields()
                        )));
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
    @Tag("api-doc")
    @Order(3)
    @Description("Try access to resource /test with access token issued at @Test(0)")
    void accessResource_withAccessToken() throws Exception {
        assertThat(accessToken).isNotNull();
        mvc.perform(get("/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(document("access-resource-with-token",
                        preprocessDocumentRequest(),
                        preprocessDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer access token"))));
    }

}

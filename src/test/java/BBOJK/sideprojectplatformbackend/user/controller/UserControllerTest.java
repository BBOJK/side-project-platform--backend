package bbojk.sideprojectplatformbackend.user.controller;

import bbojk.sideprojectplatformbackend.auth.TestConfig;
import bbojk.sideprojectplatformbackend.auth.TestUserConstant;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.io.UnsupportedEncodingException;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static bbojk.sideprojectplatformbackend.ApiDocumentUtils.preprocessDocumentRequest;
import static bbojk.sideprojectplatformbackend.ApiDocumentUtils.preprocessDocumentResponse;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  @Tag("api-doc")
  @Order(0)
  void registrationSuccessfully() throws Exception {
    mvc.perform(
            post("/user/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"user1\", \"password\": \"password1\" }")
            )
            .andExpect(status().isOk())
            .andDo(this::saveUser)
            .andDo(
                    document(
                            "user-registration-register",
                            preprocessDocumentRequest(),
                            preprocessDocumentResponse(),
                            requestFields(
                                    fieldWithPath("username").description("유저 id"),
                                    fieldWithPath("password").description("유저 비밀번호")),
                            responseFields(
                                    fieldWithPath("username").description("저장된 유저의 이름")
                            )
                    )
            );
  }

  private void saveUser(MvcResult mvcResult) throws UnsupportedEncodingException {
    String rawJsonResult = mvcResult.getResponse().getContentAsString();
    String username = JsonPath.read(rawJsonResult, "$.username");
    Assertions.assertThat(username).isEqualTo("user1");
  }
}

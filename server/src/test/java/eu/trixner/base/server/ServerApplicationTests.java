package eu.trixner.base.server;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import eu.trixner.base.dto.*;
import eu.trixner.base.server.service.EmailService;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ServerApplicationTests {
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    public static final String PREFIX = "/api";
    public static final String USER = PREFIX + "/user";
    public static final String AUTH_LOGIN = PREFIX + "/auth/login";
    public static final String USER_LIST = PREFIX + "/userlist";
    public static final String REGISTER = PREFIX + "/user/registration/register";
    public static final String CONFIRM_REGISTER = PREFIX + "/user/registration/confirmRegistration/";
    public static final String FORGOT_PASSWORD = PREFIX + "/user/forgotPassword";
    public static final String RESET_PASSWORD = PREFIX + "/user/forgotPassword/resetPassword";
    public static final String USERNAME_AVAILABLE = PREFIX + "/user/available/username/{username}";
    public static final String EMAIL_AVAILABLE = PREFIX + "/user/available/email/{email}";
    public static final String AUTH_LOGOUT = PREFIX + "/auth/logout";
    public static final String CHANGE_PASSWORD = PREFIX + "/user/change/changePassword";
    public static final String APPLICATION_JSON = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_MAIL = "user@test.com";
    public static final String USERNAME = "user";
    public static final String NEW_PASS = "asdf";
    public static final String TEST_MAIL = "test@asdf.com";
    public static final String ADMIN = "admin";
    public static final String WRONG_PASS = "wrongPass";

    public static final String USER_UUID = "b374bb51-8950-4e0d-8141-454feeaf838a";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;

    @Test
    void contextLoads() {
        //Fails automatically if nothing happens
    }

    @Test
    void logonWorks() throws Exception {
        mvc.perform(
            get(USER))
          .andDo(print())
          .andExpect(status().isUnauthorized());

        String token = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(USERNAME))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(
            get(USER)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isOk());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isNoContent());

        mvc.perform(
            get(USER))
          .andDo(print())
          .andExpect(status().isUnauthorized());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isUnauthorized());

        mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper()
                .writeValueAsString(new LoginDto().username(USERNAME).password(WRONG_PASS))))
          .andDo(print())
          .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordWorks() throws Exception {
        String token = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(USERNAME))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(post(CHANGE_PASSWORD)
            .contentType(APPLICATION_JSON)
            .content(Json.mapper().writeValueAsString(new ChangePasswordDto().userId(UUID.fromString(USER_UUID))
              .newPassword(NEW_PASS)))
            .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isOk());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isNoContent());

        mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(USERNAME))))
          .andDo(print())
          .andExpect(status().isUnauthorized());

        token = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(NEW_PASS))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(
            get(USER)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isOk());

        mvc.perform(post(CHANGE_PASSWORD)
            .contentType(APPLICATION_JSON)
            .content(Json.mapper().writeValueAsString(new ChangePasswordDto()
              .userId(UUID.fromString(USER_UUID))
              .newPassword(USERNAME)))
            .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isOk());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isNoContent());
    }


    @Test
    void userRolesWork() throws Exception {
        String token = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(USERNAME))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(get(USER_LIST)
            .contentType(APPLICATION_JSON)
            .header(AUTHORIZATION, token))
          .andExpect(status().isForbidden());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isNoContent());

        token = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(ADMIN).password(ADMIN))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(get(USER_LIST)
            .contentType(APPLICATION_JSON)
            .header(AUTHORIZATION, token))
          .andExpect(status().isOk());

        mvc.perform(
            post(AUTH_LOGOUT)
              .header(AUTHORIZATION, token))
          .andDo(print())
          .andExpect(status().isNoContent());
    }

    @Test
    public void registerWorks() throws Exception {
        mvc.perform(post(REGISTER)
            .contentType(APPLICATION_JSON)
            .content(Json.mapper()
              .writeValueAsString(new RegistrationDto().email(TEST_MAIL)
                .password(NEW_PASS)
                .username(NEW_PASS))))
          .andDo(print())
          .andExpect(status().isOk());

        ArgumentCaptor<String> tokenCapture = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendUserRegistrationMessage(any(), tokenCapture.capture(), any(), any());
        String message = tokenCapture.getValue();

        assertThat(message).isNotNull();


        mvc.perform(get(CONFIRM_REGISTER + message))
          .andDo(print())
          .andExpect(status().isFound());

        String logintoken = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(NEW_PASS).password(NEW_PASS))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(
            get(USER)
              .header(AUTHORIZATION, logintoken))
          .andDo(print())
          .andExpect(status().isOk());

        verify(emailService, times(1)).sendUserRegistrationMessage(any(), any(), any(), any());
    }

    @Test
    public void resetPasswordWorks() throws Exception {
        mvc.perform(post(FORGOT_PASSWORD)
            .contentType(APPLICATION_JSON)
            .content(Json.mapper().writeValueAsString(new ForgotPasswordDto()
              .email(USER_MAIL)
              .username(USERNAME))))
          .andDo(print())
          .andExpect(status().isOk());

        ArgumentCaptor<String> tokenCapture = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendUserPasswordResetMessage(any(), tokenCapture.capture(), any(), any());
        String message = tokenCapture.getValue();
        assertThat(message).isNotNull();

        mvc.perform(post(RESET_PASSWORD)
            .contentType(APPLICATION_JSON)
            .content(Json.mapper().writeValueAsString(new PasswordResetDto().uuid(message).newPassword(NEW_PASS))))
          .andDo(print())
          .andExpect(status().isOk());

        String logintoken = mvc.perform(
            post(AUTH_LOGIN)
              .contentType(APPLICATION_JSON)
              .content(Json.mapper().writeValueAsString(new LoginDto().username(USERNAME).password(NEW_PASS))))
          .andDo(print())
          .andExpect(header().exists(AUTHORIZATION))
          .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(
            get(USER)
              .header(AUTHORIZATION, logintoken))
          .andDo(print())
          .andExpect(status().isOk());
    }

    @Test
    public void usernameAndEmailAvailableWorks() throws Exception {
        mvc.perform(get(USERNAME_AVAILABLE, USERNAME))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(content().string("false"));
        mvc.perform(get(USERNAME_AVAILABLE, "userB"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(content().string("true"));

        mvc.perform(get(EMAIL_AVAILABLE, USER_MAIL))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(content().string("false"));
        mvc.perform(get(EMAIL_AVAILABLE, "userB@test.com"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json"))
          .andExpect(content().string("true"));
    }

}

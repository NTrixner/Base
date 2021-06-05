package eu.trixner.base.server;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import eu.trixner.base.dto.ChangePasswordDto;
import eu.trixner.base.dto.ForgotPasswordDto;
import eu.trixner.base.dto.LoginDto;
import eu.trixner.base.dto.PasswordResetDto;
import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.server.controller.UserController;
import io.swagger.util.Json;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ServerApplicationTests
{
    public static final String USER = "/user";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String APPLICATION_JSON = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String USERNAME_AVAILABLE = "/user/available/username/{username}";
    public static final String EMAIL_AVAILABLE = "/user/available/email/{email}";
    @Autowired
    private MockMvc mvc;

    public ListAppender<ILoggingEvent> getLogAppender(Class clazz)
    {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);

        ListAppender<ILoggingEvent> loggingEventListAppender = new ListAppender<>();
        loggingEventListAppender.start();

        logger.addAppender(loggingEventListAppender);

        return loggingEventListAppender;
    }

    @Test
    void contextLoads()
    {
        //Fails automatically if nothing happens
    }

    @Test
    void logonWorks() throws Exception
    {
        mvc.perform(
                get(USER))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        String token = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
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
                                .writeValueAsString(new LoginDto().username("user").password("wrongPass"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordWorks() throws Exception
    {
        String token = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(header().exists(AUTHORIZATION))
                .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(post("/user/changePassword")
                .contentType(APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(new ChangePasswordDto().oldPassword("user").newPassword(
                        "abc")))
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
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        token = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("abc"))))
                .andDo(print())
                .andExpect(header().exists(AUTHORIZATION))
                .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(
                get(USER)
                        .header(AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/user/changePassword")
                .contentType(APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(new ChangePasswordDto().oldPassword("abc").newPassword(
                        "user")))
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
    void userRolesWork() throws Exception
    {
        String token = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(header().exists(AUTHORIZATION))
                .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(get("/userlist")
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
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("admin").password("admin"))))
                .andDo(print())
                .andExpect(header().exists(AUTHORIZATION))
                .andReturn().getResponse().getHeader(AUTHORIZATION);

        mvc.perform(get("/userlist")
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
    public void registerWorks() throws Exception
    {
        ListAppender<ILoggingEvent> loggingEventListAppender = getLogAppender(UserController.class);

        mvc.perform(post("/user/registration/register")
                .contentType(APPLICATION_JSON)
                .content(Json.mapper()
                        .writeValueAsString(new RegistrationDto().email("test@asdf.com")
                                .password("asdf")
                                .username("asdf"))))
                .andDo(print())
                .andExpect(status().isOk());

        String message = (loggingEventListAppender.list)
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .findFirst()
                .orElse(null);

        assertThat(message).isNotNull();

        String token = message.substring(message.length() - 8);

        assertThat(token).isNotNull();

        mvc.perform(get("/user/registration/confirmRegistration/" + token))
                .andDo(print())
                .andExpect(status().isOk());

        String logintoken = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("asdf").password("asdf"))))
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
    public void resetPasswordWorks() throws Exception
    {
        ListAppender<ILoggingEvent> loggingEventListAppender = getLogAppender(UserController.class);

        mvc.perform(post("/user/forgotPassword")
                .contentType(APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(new ForgotPasswordDto().email("user@test.com").username(
                        "user"))))
                .andDo(print())
                .andExpect(status().isOk());

        String message = (loggingEventListAppender.list)
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .findFirst()
                .orElse(null);

        assertThat(message).isNotNull();

        String token = message.substring(message.length() - 8);

        assertThat(token).isNotNull();

        mvc.perform(post("/user/forgotPassword/resetPassword")
                .contentType(APPLICATION_JSON)
                .content(Json.mapper().writeValueAsString(new PasswordResetDto().token(token).newPassword("asdf"))))
                .andDo(print())
                .andExpect(status().isOk());

        String logintoken = mvc.perform(
                post(AUTH_LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("asdf"))))
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
    public void usernameAndEmailAvailableWorks() throws Exception
    {
        mvc.perform(get(USERNAME_AVAILABLE, "user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("false"));
        mvc.perform(get(USERNAME_AVAILABLE, "userB"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("true"));

        mvc.perform(get(EMAIL_AVAILABLE, "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("false"));
        mvc.perform(get(EMAIL_AVAILABLE, "userB@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("true"));
    }

}

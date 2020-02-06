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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ServerApplicationTests {
    @Autowired
    private MockMvc mvc;

    public ListAppender<ILoggingEvent> getLogAppender(Class clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);

        ListAppender<ILoggingEvent> loggingEventListAppender = new ListAppender<>();
        loggingEventListAppender.start();

        logger.addAppender(loggingEventListAppender);

        return loggingEventListAppender;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void logonWorks() throws Exception {
        mvc.perform(
                get("/user"))
                .andDo(print())
                .andExpect(status().isForbidden());

        String token = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(
                get("/user")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isFound());

        mvc.perform(
                get("/user"))
                .andDo(print())
                .andExpect(status().isForbidden());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("wrongPass"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordWorks() throws Exception {
        String token = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(post("/user/changePassword")
                .contentType("application/json")
                .content(Json.mapper().writeValueAsString(new ChangePasswordDto().oldPassword("user").newPassword(
                        "abc")))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isFound());

        mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        token = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("abc"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(
                get("/user")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/user/changePassword")
                .contentType("application/json")
                .content(Json.mapper().writeValueAsString(new ChangePasswordDto().oldPassword("abc").newPassword(
                        "user")))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isFound());
    }


    @Test
    void userRolesWork() throws Exception {
        String token = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("user"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(get("/userlist")
                .contentType("application/json")
                .header("Authorization", token))
                .andExpect(status().isForbidden());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isFound());

        token = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("admin").password("admin"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(get("/userlist")
                .contentType("application/json")
                .header("Authorization", token))
                .andExpect(status().isOk());

        mvc.perform(
                post("/auth/logout")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isFound());
    }

    @Test
    public void registerWorks() throws Exception {
        ListAppender<ILoggingEvent> loggingEventListAppender = getLogAppender(UserController.class);

        mvc.perform(post("/user/registration/register")
                .contentType("application/json")
                .content(Json.mapper().writeValueAsString(new RegistrationDto().email("test@asdf.com").password("asdf").username("asdf"))))
                .andDo(print())
                .andExpect(status().isOk());

        String message = (loggingEventListAppender.list)
                .stream()
                .map(ILoggingEvent::getFormattedMessage).findFirst().get();

        assertNotNull(message);

        String token = message.substring(message.length() - 8);

        assertNotNull(token);

        mvc.perform(get("/user/registration/confirmRegistration/" + token))
                .andDo(print())
                .andExpect(status().isOk());

        String logintoken = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("asdf").password("asdf"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(
                get("/user")
                        .header("Authorization", logintoken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void resetPasswordWorks() throws Exception {
        ListAppender<ILoggingEvent> loggingEventListAppender = getLogAppender(UserController.class);

        mvc.perform(post("/user/forgotPassword")
                .contentType("application/json")
                .content(Json.mapper().writeValueAsString(new ForgotPasswordDto().email("user@test.com").username(
                        "user"))))
                .andDo(print())
                .andExpect(status().isOk());

        String message = (loggingEventListAppender.list)
                .stream()
                .map(ILoggingEvent::getFormattedMessage).findFirst().get();

        assertNotNull(message);

        String token = message.substring(message.length() - 8);

        assertNotNull(token);

        mvc.perform(post("/user/forgotPassword/resetPassword")
                .contentType("application/json")
                .content(Json.mapper().writeValueAsString(new PasswordResetDto().token(token).newPassword("asdf"))))
                .andDo(print())
                .andExpect(status().isOk());

        String logintoken = mvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(Json.mapper().writeValueAsString(new LoginDto().username("user").password("asdf"))))
                .andDo(print())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");

        mvc.perform(
                get("/user")
                        .header("Authorization", logintoken))
                .andDo(print())
                .andExpect(status().isOk());
    }

}

package eu.trixner.base.server.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EmailService {
    public static final String REGISTRATION_SUB_URI = "/user/registration/confirmRegistration";
    public static final String REGISTER_TPL = "register.ftlh";

    public static final String PW_RESET_SUB_URI = "/user/forgotPassword/resetPassword";

    public static final String PW_RESET_TPL = "pwReset.ftlh";
    public static final String USERNAME = "username";
    public static final String APPLICATION_NAME = "applicationName";
    public static final String URL = "url";
    public static final String TOKEN = "token";

    private final JavaMailSenderImpl emailSender;

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${email.sender.address}")
    private String senderAddress;

    @Value("${application.name}")
    private String applicationName;


    @Autowired
    public EmailService(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        emailSender = new JavaMailSenderImpl();
        emailSender.setHost(senderAddress);
    }

    public void sendUserRegistrationMessage(String username, String token, String address) {
        Map<String, Object> templateModel = setUserManagementMailData(username, token, REGISTRATION_SUB_URI);
        createAndSendMessage(address, REGISTER_TPL, "Registration at " + applicationName, templateModel);
    }

    public void sendUserPasswordResetMessage(String username, String token, String address) {
        Map<String, Object> templateModel = setUserManagementMailData(username, token, PW_RESET_SUB_URI);

        createAndSendMessage(address, PW_RESET_TPL, "Password reset at " + applicationName, templateModel);
    }

    private Map<String, Object> setUserManagementMailData(String username, String token, String registrationSubUri) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put(USERNAME, username);
        templateModel.put(APPLICATION_NAME, applicationName);
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        String url = builder.build().toUriString() + registrationSubUri;
        templateModel.put(URL, url);
        templateModel.put(TOKEN, token);
        return templateModel;
    }

    private void createAndSendMessage(String address,
                                      String templateLocation,
                                      String subject,
                                      Map<String, Object> templateModel) {
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateLocation);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
            sendHtmlMessage(address, subject, htmlBody);
        } catch (IOException | TemplateException e) {
            log.error("Error when trying to create email message", e);
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom(senderAddress);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error when sending mail!", e);
        }
    }
}

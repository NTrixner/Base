package eu.trixner.base.server.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService {
    public static final String REGISTRATION_SUB_URI = "/api/user/registration/confirmRegistration";
    public static final String REGISTER_TPL = "register.ftlh";

    public static final String PW_RESET_SUB_URI = "/resetPassword";

    public static final String PW_RESET_TPL = "pwReset.ftlh";
    public static final String USERNAME = "username";
    public static final String APPLICATION_NAME = "applicationName";
    public static final String URL = "url";
    public static final String TOKEN = "token";
    public static final String PASSWORD_RESET_SUBJECT = "mail.passwordReset.subject";
    public static final String REGISTER_SUBJECT = "mail.register.subject";

    private final JavaMailSenderImpl emailSender = new JavaMailSenderImpl();

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    private final ResourceBundleMessageSource messageSource;

    @Value("${email.sender}")
    private String senderAddress;

    @Value("${email.host}")
    private String mailHost;

    @Value("${email.username}")
    private String mailUser;

    @Value("${email.password}")
    private String mailPassword;

    @Value("${email.port}")
    private int mailPort;

    @Value("${email.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${email.properties.mail.smtp.starttls.enable}")
    private boolean startTls;

    @Value("${application.name}")
    private String applicationName;

    @PostConstruct
    void afterPropertiesSet()
    {
        emailSender.setHost(mailHost);
        emailSender.setPort(mailPort);
        emailSender.setUsername(mailUser);
        emailSender.setPassword(mailPassword);
        Properties properties = emailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", startTls);
        properties.put("mail.debug", "true");
    }

    public void sendUserRegistrationMessage(String username, String token, String address, Locale locale) {
        Map<String, Object> templateModel = setUserManagementMailData(username, token, REGISTRATION_SUB_URI);
        createAndSendMessage(address, Path.of(locale.getLanguage(), REGISTER_TPL), messageSource.getMessage(REGISTER_SUBJECT, new Object[]{applicationName}, locale), templateModel);
    }

    public void sendUserPasswordResetMessage(String username, String token, String address, Locale locale) {
        Map<String, Object> templateModel = setUserManagementMailData(username, token, PW_RESET_SUB_URI);

        createAndSendMessage(address, Path.of(locale.getLanguage(), PW_RESET_TPL), messageSource.getMessage(PASSWORD_RESET_SUBJECT, new Object[]{applicationName}, locale), templateModel);
    }

    private Map<String, Object> setUserManagementMailData(String username, String token, String registrationSubUri) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put(USERNAME, username);
        templateModel.put(APPLICATION_NAME, applicationName);
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        builder.port(8080);
        String url = builder.build().toUriString() + registrationSubUri;
        templateModel.put(URL, url);
        templateModel.put(TOKEN, token);
        return templateModel;
    }

    private void createAndSendMessage(String address,
                                      Path templateLocation,
                                      String subject,
                                      Map<String, Object> templateModel) {
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateLocation.toString());
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
            log.info("Sent mail {}", message.getContent().toString());
            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            log.error("Error when sending mail!", e);
        }
    }
}

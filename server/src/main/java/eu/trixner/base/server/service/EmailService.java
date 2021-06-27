package eu.trixner.base.server.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailService
{
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    public static final String REGISTRATION_SUB_URI = "/user/registration/confirmRegistration";
    public static final String REGISTER_TPL = "register.ftlh";
    public static final String USERNAME = "username";
    public static final String APPLICATION_NAME = "applicationName";
    public static final String URL = "url";
    public static final String TOKEN = "token";

    private final JavaMailSender emailSender;
    private final FreeMarkerConfigurer freemarkerConfigurer;

    @Value("${email.sender.address}")
    private String senderAddress;

    @Value("${application.name}")
    private String applicationName;

    @Autowired
    public EmailService(JavaMailSender emailSender,
                        FreeMarkerConfigurer freemarkerConfigurer)
    {
        this.emailSender = emailSender;
        this.freemarkerConfigurer = freemarkerConfigurer;
    }

    public void sendUserRegistrationMessage(String username, String token, String address)
    {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put(USERNAME, username);
        templateModel.put(APPLICATION_NAME, applicationName);
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
        String url = builder.build().toUriString() + REGISTRATION_SUB_URI;
        templateModel.put(URL, url);
        templateModel.put(TOKEN, token);

        createAndSendMessage(address, REGISTER_TPL, "Registration at " + applicationName, templateModel);
    }

    private void createAndSendMessage(String address,
                                      String templateLocation,
                                      String subject,
                                      Map<String, Object> templateModel)
    {
        try
        {
            Template template = freemarkerConfigurer.getConfiguration().getTemplate(templateLocation);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
            sendHtmlMessage(address, subject, htmlBody);
        }
        catch (IOException | TemplateException e)
        {
            LOG.error("Error when trying to create email message", e);
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody)
    {
        try
        {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom(senderAddress);
            emailSender.send(message);
        }
        catch (MessagingException e)
        {
            LOG.error("Error when sending mail!", e);
        }
    }
}

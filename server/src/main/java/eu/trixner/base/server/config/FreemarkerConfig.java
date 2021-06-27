package eu.trixner.base.server.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfig
{
    @Bean
    public FreeMarkerConfigurer freemarkerClassLoaderConfig() throws IOException
    {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        TemplateLoader templateLoader = new ClassTemplateLoader(getClass(), "/templates/mail");
        configuration.setTemplateLoader(templateLoader);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}

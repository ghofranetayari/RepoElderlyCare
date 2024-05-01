package elderlycare.Services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreeMarkerConfig {
    @Bean
    public FreeMarkerConfigurer customFreeMarkerConfigurer() {
        // Define your FreeMarker configuration
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        // Configure FreeMarker settings
        configurer.setTemplateLoaderPaths("classpath:/templates/");
        return configurer;
    }
}
package elderlycare.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.stripe.model.PaymentIntent;
import elderlycare.Services.PaymentIntentSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // Disable FAIL_ON_EMPTY_BEANS
        return objectMapper;
    }
    @Bean
    public ObjectMapper objectMapperr() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(PaymentIntent.class, new PaymentIntentSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
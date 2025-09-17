package investments.vega.helloworld.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:etc/helloworld-app.${ENV_NAME}.properties")
public class AppConfig {}

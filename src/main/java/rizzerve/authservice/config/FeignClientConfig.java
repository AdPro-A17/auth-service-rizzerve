package rizzerve.authservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "rizzerve.authservice.client")
public class FeignClientConfig {
}
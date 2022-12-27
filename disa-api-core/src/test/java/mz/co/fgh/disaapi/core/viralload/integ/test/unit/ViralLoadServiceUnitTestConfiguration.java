package mz.co.fgh.disaapi.core.viralload.integ.test.unit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadServiceImpl;

@Configuration
@Profile("unitTest")
public class ViralLoadServiceUnitTestConfiguration {
    @Bean
    @Primary
    public ViralLoadService viralLoadService() {
        return new ViralLoadServiceImpl();
    }
}

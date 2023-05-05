package mz.co.fgh.disaapi.core.viralload.integ.test.unit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import mz.org.fgh.disaapi.core.result.service.LabResultService;
import mz.org.fgh.disaapi.core.result.service.LabResultServiceImpl;

@Configuration
@Profile("unitTest")
public class ViralLoadServiceUnitTestConfiguration {
    @Bean
    @Primary
    public LabResultService viralLoadService() {
        return new LabResultServiceImpl();
    }
}

package mz.org.fgh.disaapi.integ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("mz.org.fgh.disaapi.core, mz.org.fgh.disaapi.integ")
public class DisaApiIntegApplication {

	public static void main(final String[] args) {
		SpringApplication.run(DisaApiIntegApplication.class, args);
	}

}

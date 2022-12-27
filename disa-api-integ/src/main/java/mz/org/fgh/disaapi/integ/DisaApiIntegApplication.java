package mz.org.fgh.disaapi.integ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("mz.org.fgh.disaapi.core, mz.org.fgh.disaapi.integ")
public class DisaApiIntegApplication {

	private static Logger logger = LoggerFactory.getLogger(DisaApiIntegApplication.class);

	public static void main(final String[] args) {
		try {
			SpringApplication.run(DisaApiIntegApplication.class, args);
		} catch (Exception e) {
			logger.error("Error:", e);
		}
	}

}

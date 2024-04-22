/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.config;

import java.util.Collections;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Service;

import mz.org.fgh.disaapi.integ.resources.converter.LocalDateTimeParamConverterProvider;
import mz.org.fgh.disaapi.integ.resources.monitoring.SyncMonitoringResource;
import mz.org.fgh.disaapi.integ.resources.orgunit.OrgUnitResource;
import mz.org.fgh.disaapi.integ.resources.viralload.LabResultResource;

/**
 * @author Stélio Moiane
 *
 */
@Service
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		this.register(LabResultResource.class);
		this.register(OrgUnitResource.class);
		this.register(SyncMonitoringResource.class);
		this.register(LocalDateTimeParamConverterProvider.class);
		// Configure Jersey to use setStatus(int) rather sendError(int).
		// https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/html/howto-jersey.html#howto-jersey-spring-security
		setProperties(Collections.singletonMap("jersey.config.server.response.setStatusOverSendError", true));
	}
}

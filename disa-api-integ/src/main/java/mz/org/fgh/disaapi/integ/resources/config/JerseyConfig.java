/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import mz.org.fgh.disaapi.integ.resources.converter.LocalDateTimeParamConverterProvider;
import mz.org.fgh.disaapi.integ.resources.monitoring.SyncMonitoringResource;
import mz.org.fgh.disaapi.integ.resources.orgunit.OrgUnitResource;
import mz.org.fgh.disaapi.integ.resources.viralload.LabResultResource;

/**
 * @author Stélio Moiane
 *
 */
@Configuration
@ApplicationPath("/services")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		this.register(LabResultResource.class);
		this.register(OrgUnitResource.class);
		this.register(SyncMonitoringResource.class);
		this.register(LocalDateTimeParamConverterProvider.class);
		this.register(OpenApiResource.class);
		this.register(ConstraintViolationExceptionMapper.class);
		// Configure Jersey to use setStatus(int) rather sendError(int).
		// https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/html/howto-jersey.html#howto-jersey-spring-security
		property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
		property(ServerProperties.WADL_FEATURE_DISABLE, true);
	}
}

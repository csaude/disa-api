/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Service;

import mz.org.fgh.disaapi.integ.resources.converter.LocalDateTimeParamConverterProvider;
import mz.org.fgh.disaapi.integ.resources.orgunit.OrgUnitResource;
import mz.org.fgh.disaapi.integ.resources.viralload.ViralLoadResource;
import mz.org.fgh.disaapi.integ.resources.viralload.ViralLoadResourceV2;
import mz.org.fgh.disaapi.integ.resources.viralload.ViralLoadResourceV2_2;

/**
 * @author Stélio Moiane
 *
 */
@Service
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		this.register(ViralLoadResource.class);
		this.register(ViralLoadResourceV2.class);
		this.register(ViralLoadResourceV2_2.class);
		this.register(OrgUnitResource.class);
		this.register(LocalDateTimeParamConverterProvider.class);
	}
}

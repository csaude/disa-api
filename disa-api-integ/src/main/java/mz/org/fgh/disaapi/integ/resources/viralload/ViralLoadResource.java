/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.viralload;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;

/**
 * @author Stélio Moiane
 *
 */
@Path("viralloads")
@Service(ViralLoadResource.NAME)
public class ViralLoadResource {
	public static final String NAME = "mz.org.fgh.disaapi.integ.viralload.ViralLoadResource";

	@Inject
	private ViralLoadQueryService viralLoadQueryService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoads() throws BusinessException {
		final List<ViralLoad> viralLoads = this.viralLoadQueryService.findAllViralLoad();
		return Response.ok(viralLoads).build();
	}
}

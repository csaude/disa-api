/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.viralload;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.viralload.config.AbstractUserContext;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.Page;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

@Path("/v2/viralloads")
@Service(ViralLoadResourceV2.NAME)
public class ViralLoadResourceV2 extends AbstractUserContext {

	public static final String NAME = "mz.org.fgh.disaapi.integ.viralload.ViralLoadResourceV2";

	@Inject
	private ViralLoadQueryService viralLoadQueryService;

	@Inject
	private ViralLoadService viralLoadService;

	@GET
	@Path("/search-form")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoadsByForm(
			@QueryParam("requestId") final String requestId,
			@QueryParam("nid") final String nid,
			@QueryParam("healthFacilityLabCode") final List<String> healthFacilityLabCode,
			@QueryParam("referringRequestID") final String referringRequestID,
			@QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus,
			@QueryParam("notProcessingCause") NotProcessingCause notProcessingCause,
			@QueryParam("startDate") final LocalDateTime startDate,
			@QueryParam("endDate") final LocalDateTime endDate,
			@QueryParam("pageNumber") int pageNumber,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("search") String search,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("direction") String direction)
			throws BusinessException {

		Page<ViralLoad> vls = this.viralLoadQueryService.findByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, viralLoadStatus, notProcessingCause,
				startDate, endDate, search, pageNumber, pageSize, orderBy, direction);

		return Response.ok(vls).build();
	}

	@GET
	@Path("/export")
	@Produces(MediaType.APPLICATION_JSON)
	public Response exportViralLoads(
			@QueryParam("requestId") final String requestId,
			@QueryParam("nid") final String nid,
			@QueryParam("healthFacilityLabCode") final List<String> healthFacilityLabCode,
			@QueryParam("referringRequestID") final String referringRequestID,
			@QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus,
			@QueryParam("notProcessingCause") NotProcessingCause notProcessingCause,
			@QueryParam("startDate") final LocalDateTime startDate,
			@QueryParam("endDate") final LocalDateTime endDate,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("direction") String direction)
			throws BusinessException {

		List<ViralLoad> vls = this.viralLoadQueryService.findAllByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, viralLoadStatus, notProcessingCause,
				startDate, endDate);

		return Response.ok(vls).build();
	}

	@GET
	@Path("/{requestId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("requestId") String requestId) throws BusinessException {

		List<ViralLoad> viralLoads = viralLoadQueryService.findViralLoadByRequestId(Arrays.asList(requestId));

		if (viralLoads.isEmpty()) {
			throw new NotFoundException("Viral load not found");
		}

		ViralLoad viralLoad = viralLoads.get(0);

		return Response.ok(viralLoad).build();
	}

	@DELETE
	@Path("/{requestId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("requestId") String requestId) throws BusinessException {

		List<ViralLoad> viralLoads = viralLoadQueryService.findViralLoadByRequestId(Arrays.asList(requestId));

		if (viralLoads.isEmpty()) {
			throw new NotFoundException("Viral load not found");
		}

		ViralLoad viralLoad = viralLoads.get(0);
		viralLoad.inactive();
		updateViralLoad(viralLoad);

		return Response.ok().build();
	}

	@PATCH
	@Path("/{requestId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("requestId") String requestId,
			@RequestBody Map<String, Object> propertyValues) throws BusinessException {

		try {
			ViralLoad viralLoad = new ViralLoad();
			viralLoad.setRequestId(requestId);
			ViralLoad updatedVl = this.viralLoadService.updateViralLoad(getUserContext(), viralLoad, propertyValues);
			return Response.ok(updatedVl).build();
		} catch (NotFoundBusinessException e) {
			throw new NotFoundException("Viral load not found");
		}

	}

	private void updateViralLoad(ViralLoad viralLoad) {
		try {
			viralLoadService.updateViralLoad(getUserContext(), viralLoad);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
}

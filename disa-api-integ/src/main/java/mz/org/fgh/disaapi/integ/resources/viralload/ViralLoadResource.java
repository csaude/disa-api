/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.viralload;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.Page;
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;

@Path("/v2/viralloads")
@Service
@Deprecated
public class ViralLoadResource {

	@Inject
	private LabResultQueryService viralLoadQueryService;
	@GET
	@Path("/search-form")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoadsByForm(
			@QueryParam("requestId") final String requestId,
			@QueryParam("nid") final String nid,
			@QueryParam("healthFacilityLabCode") final List<String> healthFacilityLabCode,
			@QueryParam("referringRequestID") final String referringRequestID,
			@QueryParam("viralLoadStatus") final LabResultStatus viralLoadStatus,
			@QueryParam("labResultStatus") final LabResultStatus labResultStatus,
			@QueryParam("notProcessingCause") NotProcessingCause notProcessingCause,
			@QueryParam("typeOfResult") TypeOfResult typeOfResult,
			@QueryParam("startDate") final LocalDateTime startDate,
			@QueryParam("endDate") final LocalDateTime endDate,
			@QueryParam("pageNumber") int pageNumber,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("search") String search,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("direction") String direction)
			throws BusinessException {

		LabResultStatus status = labResultStatus != null ? labResultStatus : viralLoadStatus;

		Page<LabResult> vls = this.viralLoadQueryService.findByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, status, notProcessingCause, typeOfResult,
				startDate, endDate, search, pageNumber, pageSize, orderBy, direction);

		return Response.ok(vls).build();
	}
}

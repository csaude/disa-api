/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.viralload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;
import mz.org.fgh.disaapi.core.result.service.LabResultService;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 *
 *
 * @deprecated see {@link ViralLoadResourceV2}.
 */
@Path("viralloads")
@Deprecated
@Service
public class ViralLoadResource {

	@Inject
	private LabResultQueryService labResultQueryService;

	@Inject
	private LabResultService labResultService;

	private List<LabResult> viralLoads;

	@GET
	@Path("/requestProvince")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoads(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("requestingProvinceName") String requestingProvinceName)
			throws BusinessException {
		viralLoads = this.labResultQueryService.findPendingByLocationCodeAndProvince(locationCodes, requestingProvinceName);
		// This endpoint should only return viral loads
		List<LabResult> vls = viralLoads.stream().filter(vl -> vl.getTypeOfResult() == TypeOfResult.HIVVL).collect(Collectors.toList());
		return Response.ok(vls).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoads(@QueryParam("locationCodes") final List<String> locationCodes)
			throws BusinessException {
		viralLoads = this.labResultQueryService.findPendingByLocationCode(locationCodes);
		return Response.ok(viralLoads).build();

	}

	@GET
	@Path("/requestProvince/search-form")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public Response findViralLoadsByForm(
			@QueryParam("requestId") final String requestId,
			@QueryParam("nid") final String nid,
			@QueryParam("healthFacilityLabCode") final List<String> healthFacilityLabCode,
			@QueryParam("referringRequestID") final String referringRequestID,
			@QueryParam("viralLoadStatus") final LabResultStatus labResultStatus,
			@QueryParam("notProcessingCause") final NotProcessingCause notProcessingCause,
			@QueryParam("startDate") final LocalDateTime startDate,
			@QueryParam("endDate") final LocalDateTime endDate)
			throws BusinessException {

		List<LabResult> vls = this.labResultQueryService.findAllByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, labResultStatus, notProcessingCause, null,
				startDate, endDate);

		return Response.ok(vls).build();
	}

	@GET
	@Path("/requestProvince/viral-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _findViralLoadsByStatus(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("viralLoadStatus") final LabResultStatus labResultStatus) throws BusinessException {
		viralLoads = this.labResultQueryService.findByLocaationCodeAndStatus(locationCodes, labResultStatus);
		return Response.ok(viralLoads).build();
	}

	@GET
	@Path("/requestProvince/viral-status-dates")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _findViralLoadsByStatusAndDates(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("viralLoadStatus") final LabResultStatus labResultStatus,
			@QueryParam("startDate") final String strStartDate, @QueryParam("endDate") final String strEndDate)
			throws BusinessException {
		viralLoads = this.labResultQueryService.findByLocationCodeAndStatusBetweenDates(locationCodes, labResultStatus,
				convertToLocalDateTime(strStartDate), convertToLocalDateTime(strEndDate));
		return Response.ok(viralLoads).build();
	}

	@PUT
	@Path("/requestProvince/not-processed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _updateViralLoadNotProcessedViralLoad(
			@QueryParam("notProcessedNids") final List<String> notProcessedNids,
			@QueryParam("reasonForNotProcessing") final String reasonForNotProcessing,
			@QueryParam("defaultLocationUuid") final String defaultLocationUuid) throws BusinessException {

		viralLoads = labResultQueryService.findViralLoadByRequestId(notProcessedNids);

		viralLoads.forEach(viralLoad -> {
			viralLoad.setNotProcessed();
			viralLoad.setSynchronizedBy(defaultLocationUuid);
			if (reasonForNotProcessing.equals("nid")) {
				viralLoad.setCauseNoNID();
			} else if (reasonForNotProcessing.equals("result")) {
				viralLoad.setCauseInvalidResult();
			} else if (reasonForNotProcessing.equals("review")) {
				viralLoad.setCauseInvalidResult();
			} else if (reasonForNotProcessing.equals("duplicate")) {
				viralLoad.setCauseDuplicateNid();
			} else if (reasonForNotProcessing.equals("duplicatedReqId")) {
				viralLoad.setCauseDuplicateReqId();
				viralLoad.inactive();
			}
			updateViralLoad(viralLoad);
		});

		return Response.ok(viralLoads).build();
	}

	private void updateViralLoad(LabResult viralLoad) {
		try {
			labResultService.updateLabResult(viralLoad);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	@PUT
	@Path("/requestProvince/processed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _updateViralLoadProcessedViralLoad(@QueryParam("processedNids") final List<String> processedNids,
			@QueryParam("defaultLocationUuid") final String defaultLocationUuid)
			throws BusinessException {

		viralLoads = labResultQueryService.findViralLoadByRequestId(processedNids);

		viralLoads.forEach(viralLoad -> {
			viralLoad.setProcessed();
			viralLoad.setSynchronizedBy(defaultLocationUuid);
			updateViralLoad(viralLoad);
		});

		return Response.ok(viralLoads).build();
	}

	@PUT
	@Path("/requestProvince/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _updateViralLoadPendingViralLoad(@QueryParam("pendingNids") final List<String> pendingNids)
			throws BusinessException {

		viralLoads = labResultQueryService.findViralLoadByRequestId(pendingNids);

		viralLoads.forEach(viralLoad -> {
			viralLoad.setPending();
			updateViralLoad(viralLoad);
		});

		return Response.ok(viralLoads).build();
	}

	private LocalDateTime convertToLocalDateTime(final String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return LocalDateTime.parse(date, formatter);
	}

}

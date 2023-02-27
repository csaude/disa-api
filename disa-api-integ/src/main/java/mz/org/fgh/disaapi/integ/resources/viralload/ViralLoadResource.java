/**
 *
 */
package mz.org.fgh.disaapi.integ.resources.viralload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import mz.org.fgh.disaapi.core.viralload.config.AbstractUserContext;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Path("viralloads")
@Service(ViralLoadResource.NAME)
public class ViralLoadResource extends AbstractUserContext {

	public static final String NAME = "mz.org.fgh.disaapi.integ.viralload.ViralLoadResource";

	@Inject
	private ViralLoadQueryService viralLoadQueryService;

	@Inject
	private ViralLoadService viralLoadService;

	private List<ViralLoad> viralLoads;

	@GET
	@Path("/requestProvince")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoads(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("requestingProvinceName") String requestingProvinceName)
			throws BusinessException {
		viralLoads = this.viralLoadQueryService.findByLocationCodeAndStatus(locationCodes, requestingProvinceName);
		return Response.ok(viralLoads).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findViralLoads(@QueryParam("locationCodes") final List<String> locationCodes)
			throws BusinessException {
		viralLoads = this.viralLoadQueryService.findByLocationCodeAndStatus(locationCodes);
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
			@QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus,
			@QueryParam("notProcessingCause") final NotProcessingCause notProcessingCause,
			@QueryParam("startDate") final LocalDateTime startDate,
			@QueryParam("endDate") final LocalDateTime endDate)
			throws BusinessException {

		List<ViralLoad> vls = this.viralLoadQueryService.findAllByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, viralLoadStatus, notProcessingCause,
				startDate, endDate);

		return Response.ok(vls).build();
	}

	@GET
	@Path("/requestProvince/viral-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _findViralLoadsByStatus(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus) throws BusinessException {
		viralLoads = this.viralLoadQueryService.findByStatus(locationCodes, viralLoadStatus);
		return Response.ok(viralLoads).build();
	}

	@GET
	@Path("/requestProvince/viral-status-dates")
	@Produces(MediaType.APPLICATION_JSON)
	public Response _findViralLoadsByStatusAndDates(@QueryParam("locationCodes") final List<String> locationCodes,
			@QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus,
			@QueryParam("startDate") final String strStartDate, @QueryParam("endDate") final String strEndDate)
			throws BusinessException {
		viralLoads = this.viralLoadQueryService.findByStatusAndDates(locationCodes, viralLoadStatus,
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

		viralLoads = viralLoadQueryService.findViralLoadByRequestId(notProcessedNids);

		viralLoads.forEach(viralLoad -> {
			viralLoad.setNotProcessed();
			viralLoad.setSynchronizedBy(defaultLocationUuid);
			if (reasonForNotProcessing.equals("nid")) {
				viralLoad.setCauseNoNID();
			} else if (reasonForNotProcessing.equals("result")) {
				viralLoad.setCauseNoResult();
			} else if (reasonForNotProcessing.equals("review")) {
				viralLoad.setCauseFlaggedForReview();
			} else if (reasonForNotProcessing.equals("duplicate")) {
				viralLoad.setCauseDuplicateNid();
			} else if (reasonForNotProcessing.equals("duplicatedReqId")) {
				viralLoad.setCauseDuplicateReqId();
			}
			updateViralLoad(viralLoad);
		});

		return Response.ok(viralLoads).build();
	}

	private void updateViralLoad(ViralLoad viralLoad) {
		try {
			viralLoadService.updateViralLoad(getUserContext(), viralLoad);
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

		viralLoads = viralLoadQueryService.findViralLoadByRequestId(processedNids);

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

		viralLoads = viralLoadQueryService.findViralLoadByRequestId(pendingNids);

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

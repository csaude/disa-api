package mz.org.fgh.disaapi.integ.resources.viralload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;
import mz.org.fgh.disaapi.core.result.service.LabResultService;

@Path("/lab-results")
@Component
public class LabResultResource {

    @Inject
    private LabResultQueryService viralLoadQueryService;

    @Inject
    private LabResultService viralLoadService;

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findViralLoadsByForm(
            @QueryParam("requestId") final String requestId,
            @QueryParam("nid") final String nid,
            @QueryParam("healthFacilityLabCode") final List<String> healthFacilityLabCode,
            @QueryParam("referringRequestID") final String referringRequestID,
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

        if (healthFacilityLabCode.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide at least one healthFacilityLabCode param.")
                    .build();
        }

        // TODO use one of typeOfResult or the class
        LabResult result = new HIVVLLabResult();
        result.setTypeOfResult(typeOfResult);

        result.setRequestId(requestId);
        result.setNid(nid);
        result.setReferringRequestID(referringRequestID);
        result.setLabResultStatus(labResultStatus);
        result.setNotProcessingCause(notProcessingCause);
        result.active();

        if (pageSize == 0) {
            pageSize = LabResultQueryService.DEFAULT_PAGE_SIZE;
        }

        if (StringUtils.isEmpty(direction)) {
            direction = LabResultQueryService.DEFAULT_DIRECTION;
        }

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = LabResultQueryService.DEFAULT_ORDER_BY;
        }

        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Direction.fromString(direction), orderBy);

        Page<LabResult> vls = this.viralLoadQueryService.findByForm(result, healthFacilityLabCode, startDate, endDate,
                pageable);

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
            @QueryParam("labResultStatus") final LabResultStatus labResultStatus,
            @QueryParam("notProcessingCause") NotProcessingCause notProcessingCause,
            @QueryParam("typeOfResult") TypeOfResult typeOfResult,
            @QueryParam("startDate") final LocalDateTime startDate,
            @QueryParam("endDate") final LocalDateTime endDate)
            throws BusinessException {

        if (healthFacilityLabCode.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide at least one healthFacilityLabCode param.")
                    .build();
        }

        // TODO use one of typeOfResult or the class
        LabResult result = new HIVVLLabResult();
        result.setTypeOfResult(typeOfResult);

        result.setRequestId(requestId);
        result.setNid(nid);
        result.setReferringRequestID(referringRequestID);
        result.setLabResultStatus(labResultStatus);
        result.setNotProcessingCause(notProcessingCause);
        result.active();

        List<LabResult> vls = this.viralLoadQueryService.findAllByForm(result, healthFacilityLabCode, startDate,
                endDate);

        return Response.ok(vls).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {

        LabResult labResult = findById(id);

        return Response.ok(labResult).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {

        LabResult labResult = findById(id);

        labResult.inactive();
        updateViralLoad(labResult);

        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id,
            @RequestBody Map<String, Object> propertyValues) throws BusinessException {

        try {
            LabResult labResult = findById(id);
            LabResult updatedVl = this.viralLoadService.updateLabResult(labResult, propertyValues);
            return Response.ok(updatedVl).build();
        } catch (NotFoundBusinessException e) {
            throw new NotFoundException("Viral load not found");
        }
    }

    private void updateViralLoad(LabResult viralLoad) {
        try {
            viralLoadService.updateLabResult(viralLoad);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    private LabResult findById(Long id) {
        try {
            List<LabResult> labResult = viralLoadQueryService.findById(id);
            if (labResult.isEmpty()) {
                throw new NotAuthorizedException(Response.status(Status.UNAUTHORIZED));
            }
            return labResult.get(0);
        } catch (NotFoundBusinessException e) {
            throw new NotFoundException("Viral load not found");
        }
    }
}
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadService;

@Path("/v2.1.2/viralloads")
@Component
public class ViralLoadResourceV2_1_2 {

    @Inject
    private ViralLoadQueryService viralLoadQueryService;

    @Inject
    private ViralLoadService viralLoadService;

    @GET
    @Path("/search")
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

        ViralLoad result = new ViralLoad();
        result.setRequestId(requestId);
        result.setNid(nid);
        result.setReferringRequestID(referringRequestID);
        result.setViralLoadStatus(viralLoadStatus);
        result.setNotProcessingCause(notProcessingCause);
        result.active();

        if (pageSize == 0) {
            pageSize = ViralLoadQueryService.DEFAULT_PAGE_SIZE;
        }

        if (StringUtils.isEmpty(direction)) {
            direction = ViralLoadQueryService.DEFAULT_DIRECTION;
        }

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = ViralLoadQueryService.DEFAULT_ORDER_BY;
        }

        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Direction.fromString(direction), orderBy);

        Page<ViralLoad> vls = this.viralLoadQueryService.findByForm(result, healthFacilityLabCode, startDate, endDate,
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
            @QueryParam("viralLoadStatus") final ViralLoadStatus viralLoadStatus,
            @QueryParam("notProcessingCause") NotProcessingCause notProcessingCause,
            @QueryParam("startDate") final LocalDateTime startDate,
            @QueryParam("endDate") final LocalDateTime endDate,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("direction") String direction)
            throws BusinessException {

        ViralLoad result = new ViralLoad();
        result.setRequestId(requestId);
        result.setNid(nid);
        result.setReferringRequestID(referringRequestID);
        result.setViralLoadStatus(viralLoadStatus);
        result.setNotProcessingCause(notProcessingCause);
        result.active();

        List<ViralLoad> vls = this.viralLoadQueryService.findAllByForm(result, healthFacilityLabCode, startDate,
                endDate);

        return Response.ok(vls).build();
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
            ViralLoad updatedVl = this.viralLoadService.updateViralLoad(viralLoad, propertyValues);
            return Response.ok(updatedVl).build();
        } catch (NotFoundBusinessException e) {
            throw new NotFoundException("Viral load not found");
        }

    }

    private void updateViralLoad(ViralLoad viralLoad) {
        try {
            viralLoadService.updateViralLoad(viralLoad);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
}

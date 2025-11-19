package mz.org.fgh.disaapi.integ.resources.monitoring;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoring;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoringDetail;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoringRepository;

@Path("/monitoring")
@Component
@Tag(name = "Monitoring")
public class SyncMonitoringResource {

    @Inject
    private SyncMonitoringRepository syncMonitoringRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Sync monitoring", description = """
            DISA-SESP interoperability monitoring -
            Allows monitoring the interoperability of laboratory results at any time,
            presenting aggregated data from the first day of interoperability until the current date.""")
    public List<SyncMonitoring> getSyncMonitoring() {
        return syncMonitoringRepository.getSyncMonitoring();
    }
    
    @GET
    @Path("/detail")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Sync monitoring detailed", description = """
            DISA-SESP interoperability monitoring - Detailed view -
            Returns disaggregated data showing individual viral load records with full details.
            This endpoint provides record-level information instead of aggregated summaries,
            allowing for detailed analysis of individual requests and their processing status.""")
    public List<SyncMonitoringDetail> getSyncMonitoringDetail(){
    	return syncMonitoringRepository.getSyncMonitoringDetail();
    }
}

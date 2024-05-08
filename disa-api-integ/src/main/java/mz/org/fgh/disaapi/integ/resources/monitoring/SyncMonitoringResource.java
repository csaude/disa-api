package mz.org.fgh.disaapi.integ.resources.monitoring;

import java.util.List;

import javax.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import mz.org.fgh.disaapi.core.monitoring.SyncMonitoring;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoringRepository;

@Path("/monitoring")
@Component
public class SyncMonitoringResource {

    @Inject
    private SyncMonitoringRepository syncMonitoringRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SyncMonitoring> getSyncMonitoring() {
        return syncMonitoringRepository.getSyncMonitoring();
    }
}

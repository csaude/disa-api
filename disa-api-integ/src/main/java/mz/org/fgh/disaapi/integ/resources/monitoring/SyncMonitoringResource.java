package mz.org.fgh.disaapi.integ.resources.monitoring;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

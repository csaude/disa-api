package mz.org.fgh.disaapi.integ.resources.monitoring;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoring;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoringDetail;
import mz.org.fgh.disaapi.core.monitoring.SyncMonitoringRepository;
import mz.org.fgh.disaapi.core.util.DateUtils;

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
    public List<SyncMonitoringDetail> getSyncMonitoringDetail(
    		@Parameter(description = "Data de início (obrigatória). Formato: yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss", required = true, example = "2025-01-01") 
    		@QueryParam("dataInicio") String dataInicio,
            @Parameter(description = "Data de fim (opcional). Se não fornecida, será considerada a data/hora atual. Formato: yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss", required = false, example = "2025-01-31")
    		@QueryParam("dataFim") String dataFim){
    	
        if (dataInicio == null || dataInicio.trim().isEmpty()) {
            throw new BadRequestException("Parâmetro 'dataInicio' é obrigatório. Formato esperado: yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss");
        }
        
        LocalDateTime startDate;
        LocalDateTime endDate;
        
        try {
            startDate = DateUtils.parseDateTime(dataInicio.trim(), true);
            
            if (dataFim == null || dataFim.trim().isEmpty()) {
                endDate = LocalDateTime.now();
            } else {
                endDate = DateUtils.parseDateTime(dataFim.trim(), false);
            }

            if (startDate.isAfter(endDate)) {
                throw new BadRequestException("A 'dataInicio' não pode ser posterior à 'dataFim'");
            }

        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato de data inválido. Use yyyy-MM-dd ou yyyy-MM-dd HH:mm:ss. Erro: " + e.getMessage());
        }
    	
    	return syncMonitoringRepository.getSyncMonitoringDetail(startDate, endDate);
    }
}

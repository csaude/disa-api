package mz.org.fgh.disaapi.integ.resources.viralload;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;

@Path("/save-results")
public class LabResultLoadResource {

	@Inject
	private LabResultRepository labResultRepository;

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response uploadData(@RequestBody List<LabResult> labResultList) {
		try {
			labResultRepository.saveAll(labResultList);
			return Response.status(Status.CREATED).entity("Data inserted successfully").build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error inserting data: " + e.getMessage()).build();
		}
	}
}

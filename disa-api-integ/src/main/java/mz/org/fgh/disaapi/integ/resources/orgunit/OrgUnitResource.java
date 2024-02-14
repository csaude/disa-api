package mz.org.fgh.disaapi.integ.resources.orgunit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import mz.org.fgh.disaapi.core.orgunit.service.OrgUnitService;

@Path("/orgunits")
@Service
public class OrgUnitResource {

    @Inject
    protected OrgUnitService orgUnitService;

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("term") String term) {
        if (term == null || term.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide the term param.")
                    .build();
        }
        return Response.ok(this.orgUnitService.search(term)).build();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCode(@PathParam("code") String code) {
        return Response.ok(this.orgUnitService.findByCode(code)).build();
    }
}

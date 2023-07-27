package mz.org.fgh.disaapi.integ.resources.orgunit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

/**
 * @deprecated Added to support old clients that use v2 versining in the URI
 *             since
 *             we are moving away from this pattern.
 */
@Deprecated
@Path("/v2/orgunits")
@Service
public class OrgUnitResourceV2 extends OrgUnitResource {

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("term") String term) {
        return Response.ok(this.orgUnitService.search(term)).build();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCode(@PathParam("code") String code) {
        return Response.ok(this.orgUnitService.findByCode(code)).build();
    }
}

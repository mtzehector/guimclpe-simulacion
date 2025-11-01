package mx.gob.dpes.simulacionfront.restclient;

import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
@Path("/capacidadCredito")
public interface CapacidadCreditoClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(CapacidadCredito request);
}

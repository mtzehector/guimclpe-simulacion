package mx.gob.dpes.simulacionfront.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.dpes.simulacionfront.model.SelloElectronicoRequest;

@Path("/selloElectronico")
@RegisterRestClient
public interface SelloElectronicoClient {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response create(mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoRequest cadenaOriginal);
}




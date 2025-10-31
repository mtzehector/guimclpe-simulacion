/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.dpes.simulacionfront.model.PensionadoSimulacionRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;

/**
 *
 * @author salvador.pocteco
 */
@Path("/pensionado")
@RegisterRestClient
public interface PensionadoClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener los datos de un pensionado",
            description = "Obtener los datos de un pensionado")
    public Response consultaPensionado(Pensionado request);
    
    
     @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response load(PensionadoSimulacionRequest request);

}


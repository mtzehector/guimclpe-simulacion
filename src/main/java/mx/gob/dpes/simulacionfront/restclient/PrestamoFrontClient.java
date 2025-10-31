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
import mx.gob.dpes.simulacionfront.model.PrestamosEnRecuperacionRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author juan.garfias
 */
@RegisterRestClient
@Path("/prestamo")
public interface PrestamoFrontClient {

    @POST
    @Path("/prestamosEnRecuperacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response prestamosEnRecuperacion(PrestamosEnRecuperacionRequest p);

}

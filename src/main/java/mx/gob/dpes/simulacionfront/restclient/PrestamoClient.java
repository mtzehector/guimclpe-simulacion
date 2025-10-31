/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author antonio
 */
@RegisterRestClient
@Path("/prestamo")
public interface PrestamoClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(Prestamo request);

    @GET
    @Path("/{idSolicitud}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response load(@PathParam("idSolicitud") Long idSolicitud);
    
    @POST
    @Path("/event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response event(Long idSolicitud, EventEnum event);
    
    @POST
    @Path("/prestamoEnRecuperacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response prestamoEnRecuperacion(PrestamoRecuperacion p);
     
    
}

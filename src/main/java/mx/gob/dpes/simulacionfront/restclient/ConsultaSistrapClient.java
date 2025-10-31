/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosPorSolicitudRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author juan.garfias
 */
@Path("/prestamo")
@RegisterRestClient
public interface ConsultaSistrapClient {
        
    @POST
    @Path("/consultaDescuentosPorSolicitud")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(ConsultaDescuentosPorSolicitudRequest rq);
    
    @POST
    @Path("/registroPrestamo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistroPrestamoResponse save(RegistroPrestamoRequest request);

}

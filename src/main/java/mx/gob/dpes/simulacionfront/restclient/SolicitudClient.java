/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.dpes.simulacionfront.model.DiaInhabilRequest;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author antonio
 */
@RegisterRestClient
@Path("/solicitud")
public interface SolicitudClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(Solicitud request);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualizafoliocontrol")
    public Response updateFolio(Solicitud request);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualiza-fechavigencia")
    public Response updateFechaVigencia(Solicitud solicitud);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualizaestado")
    public Response updateEstado(Solicitud request);

    @GET
    @Path("/{idSolicitud}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response load(@PathParam("idSolicitud") Long idSolicitud);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/calendarioNomina/fechas-para-documento")
    public Response fechasParaDocumento(DiaInhabilRequest diaInhabilRequest);

}

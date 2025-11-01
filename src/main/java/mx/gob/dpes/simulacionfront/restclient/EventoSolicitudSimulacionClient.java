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
import mx.gob.imss.dpes.interfaces.evento.model.Evento;
import mx.gob.imss.dpes.interfaces.evento.service.BaseEventoClient;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author antonio
 */
@RegisterRestClient
@Path("/evento")
public interface EventoSolicitudSimulacionClient extends BaseEventoClient{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(Evento evento);
    
}

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
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author gabriel.rios
 */
@RegisterRestClient
@Path("/prestamoSistrap")
public interface PrestamoSIPREClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistroPrestamoResponse save(RegistroPrestamoRequest request);

    
    
    
}

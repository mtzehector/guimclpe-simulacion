/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultarNombreComercialSipre extends ServiceDefinition<EntidadFinanciera,EntidadFinanciera>{
    @Inject
    @RestClient
    private EntidadFinancieraBackClient backClient;
     
      @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        
        Response response = backClient.load(String.valueOf(request.getPayload().getIdSipre()));
        EntidadFinanciera e = response.readEntity(EntidadFinanciera.class);
        request.setPayload(e);
        return request;
    }
}

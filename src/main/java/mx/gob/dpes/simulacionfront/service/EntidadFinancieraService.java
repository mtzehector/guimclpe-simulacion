/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
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
public class EntidadFinancieraService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private EntidadFinancieraBackClient clientEF;
    
    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        String id = request.getPayload().getCveEntidadFinSIPRE().replace(" ", "");
        Response resp = clientEF.load(id);
        if (resp.getStatus() == 200) {
            EntidadFinanciera ef = resp.readEntity(EntidadFinanciera.class);
            request.getPayload().setEntidadFinanciera(ef);
        }
        return request;
        }
    
    
}

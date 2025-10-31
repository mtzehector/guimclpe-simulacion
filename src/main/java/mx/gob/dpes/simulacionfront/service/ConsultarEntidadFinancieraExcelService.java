/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.CondicionOfertaRequest;
import mx.gob.dpes.simulacionfront.model.EntidadFinancieraLogo;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraClient;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraFrontClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;

import org.eclipse.microprofile.rest.client.inject.RestClient;
/**
 *
 * @author juanf.barragan
 */
@Provider
public class ConsultarEntidadFinancieraExcelService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest>{
    
    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;
    
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>simulacionFront|ConsultarEntidadFinancieraService|execute: {0}", request.getPayload());
        
        CondicionOfertaRequest condicionOfertaRequest = new CondicionOfertaRequest();
        condicionOfertaRequest.setClave(request.getPayload().getPrestamo().getIdOferta());
        
        Response respuesta = entidadFinancieraClient.load(condicionOfertaRequest);

        if (respuesta.getStatus() == 200) {
            Oferta oferta = respuesta.readEntity(Oferta.class);
            request.getPayload().setOferta(oferta);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
        
    }
    
    
}

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
import mx.gob.dpes.simulacionfront.model.PersonaModel;
import mx.gob.dpes.simulacionfront.model.PersonaRequest;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.ConsultaPersonaClient;
import mx.gob.dpes.simulacionfront.restclient.PersonaClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class PersonaService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {
    
  @Inject
  @RestClient
  private ConsultaPersonaClient personaClient;

  @Override
  public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request) throws BusinessException {

      //log.log(Level.INFO,">>>INICIA STEP PERSONA: {0}", request.getPayload());
        
        Pensionado pe = new Pensionado();
        pe.setNss(request.getPayload().getSolicitud().getNss());
        pe.setCurp(request.getPayload().getSolicitud().getCurp());
    
        Response load = personaClient.load(pe);
        if (load.getStatus() == 200) {
             PersonaModel res = load.readEntity(PersonaModel.class);
             request.getPayload().setPersonaModel(res);
             return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.restclient.EventoSolicitudSimulacionClient;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.interfaces.evento.model.Evento;
import mx.gob.imss.dpes.interfaces.evento.service.BaseCreateEventService;
import mx.gob.imss.dpes.interfaces.evento.service.BaseEventoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class CreateEventSolicitudSimulacionService extends BaseCreateEventService{

  @Inject
  @RestClient
  private EventoSolicitudSimulacionClient eventoSolicitudSimulacionClient;

  @Override
  public BaseEventoClient getClient() {
    return eventoSolicitudSimulacionClient;
  }

  @Override
  public void initEvent(Evento evento) {        
    evento.setEvent(EventEnum.CREAR_SOLICITUD_SIMULACION);    
  }

}

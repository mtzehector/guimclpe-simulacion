/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.NotificacionCorreo;
import mx.gob.dpes.simulacionfront.restclient.ReporteResumenSimulacionClient;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 * @author salvador.pocteco
 */
@Provider
public class ReadReporteResumenSimulacionService extends ServiceDefinition<NotificacionCorreo, NotificacionCorreo> {

  @Inject
  @RestClient
  private ReporteResumenSimulacionClient client;

  @Override
  public Message<NotificacionCorreo> execute(Message<NotificacionCorreo> request) throws BusinessException {

    log.log(Level.INFO, "El request general es : {0}", request.getPayload());    
    Response respuesta = client.load(request.getPayload().getIdSolicitud() );

    if (respuesta.getStatus() == 200) {
      byte[] archivo = respuesta.readEntity(byte[].class);
      Adjunto adjunto = new Adjunto();
      adjunto.setNombreAdjunto("ReporteResumenSimulacion.pdf");
      adjunto.setAdjuntoBase64(archivo);
      request.getPayload().getCorreo().getAdjuntos().add(adjunto);
      
      log.log(Level.INFO, "El tamaño del adjunto es {0}", archivo.length);
      return new Message<>(request.getPayload());
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
  }
}

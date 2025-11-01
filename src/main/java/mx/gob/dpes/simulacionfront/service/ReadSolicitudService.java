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
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ReadSolicitudService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {

        log.log(Level.INFO, "Request hacia SolicitudBack: {0}", request.getPayload().getSolicitud());

        Response load = service.load(request.getPayload().getSolicitud().getId());
        if (load.getStatus() == 200) {
            Solicitud solicitud = load.readEntity(Solicitud.class);
            log.log(Level.INFO, "Request hacia SolicitudBack solicitud: {0}", solicitud);

            solicitud.setCveEstadoSolicitud(request.getPayload().getSolicitud().getCveEstadoSolicitud());
            solicitud.setEstadoSolicitud(request.getPayload().getSolicitud().getEstadoSolicitud());

            request.getPayload().setSolicitud(solicitud);
            // Incluier el assembler
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}

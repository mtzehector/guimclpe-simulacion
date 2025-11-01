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
import mx.gob.dpes.simulacionfront.exception.SimulacionException;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SolicitudHabilitarService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {
        log.log(Level.WARNING, "SolicitudHabilitarService 1 {0}", request.getPayload().getSolicitud());

        //Solicitud s = request.getPayload().getSolicitud();
        //s.setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR);
        //s.setCveEstadoSolicitud(new EstadoSolicitud());
        //s.getCveEstadoSolicitud().setId(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR.toValue());
        //log.log(Level.WARNING, "SolicitudPorAsignarPromotorService 1.1 Solicitud s {0}", request.getPayload().getSolicitud());
        Response load = service.updateEstado(request.getPayload().getSolicitud());
        //log.log(Level.WARNING, "SolicitudHabilitarService 2 load.getStatus(){0}", load.getStatus());

        if (load.getStatus() == 200) {

            Solicitud solicitudOut = load.readEntity(Solicitud.class);
            log.log(Level.WARNING, "SolicitudHabilitarService 2 solicitudOut{0}", solicitudOut);

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

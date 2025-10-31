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
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraBackClient;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.dpes.simulacionfront.rules.CreateSolicitudRule;
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SolicitudService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Inject
    @RestClient
    private EntidadFinancieraBackClient clientEF;

    @Inject
    private CreateSolicitudRule rule;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        int mejorOferta = request.getPayload().getSolicitud().getMejorOferta();
        log.log(Level.INFO, ">>>Step 2");
        //se inserta solicitud
        Solicitud solicitud = rule.apply(request.getPayload());
        // solicitud.setOrigenSolictud(OrigenSolicitudEnum.SIMULACION);
        log.log(Level.INFO, "ERPE Request hacia SolicitudBack: {0}", solicitud);
        Response load = service.load(solicitud);
        if (load.getStatus() == 200) {
            Solicitud solicitudOut = load.readEntity(Solicitud.class);
            log.log(Level.INFO, "JGV Response solicitudOut Back: {0}", solicitudOut);
            solicitudOut.setMejorOferta(mejorOferta);
            request.getPayload().setSolicitud(solicitudOut);

            request.getPayload().getSolicitud().setEstadoSolicitud(
                    TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR
            );

            request.getPayload().getSolicitud().setCveEstadoSolicitud(
                    new EstadoSolicitud(
                            TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR.toValue()
                    )
            );
            log.log(Level.INFO, "JGV Response request.getPayload().getSolicitud(): {0}", request.getPayload().getSolicitud());

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

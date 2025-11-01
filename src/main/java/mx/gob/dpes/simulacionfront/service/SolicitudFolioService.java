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
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.dpes.simulacionfront.rules.CreateFolioSolicitudRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SolicitudFolioService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private SolicitudClient solicitudClient;

    @Inject
    private CreateFolioSolicitudRule rule;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        //int mejorOferta = request.getPayload().getSolicitud().getMejorOferta();
        log.log(Level.INFO, "Step 6: request.solicitud: {0}", request.getPayload().getSolicitud());
        //inserta el folio Generado chaval
        Solicitud folio = rule.apply(request.getPayload());

        log.log(Level.WARNING, "Request Solicitud Back actualización folio {0}", folio);
        Response load = solicitudClient.updateFolio(
                folio
        );
        if (load.getStatus() == 200) {
            Solicitud sol = load.readEntity(Solicitud.class);
            //sol.setMejorOferta(mejorOferta);

            log.log(Level.WARNING, "Response Solicitud Back update folio {0}", sol);

            request.getPayload().getSolicitud().setNumFolioSolicitud(sol.getNumFolioSolicitud());

            request.getPayload().getSolicitud().setConsecutivo(
                    request.getPayload().getConsecutivo().getNumConsecutivo()
            );

            request.getPayload().getSolicitud().setDelegacion(
                    request.getPayload().getPensionado().getDelegacion().getCveDelegacion()
            );

            request.getPayload().getSolicitud().setAltaRegistro(
                    request.getPayload().getSolicitud().getAltaRegistro()
            );
        log.log(Level.INFO, "Step 6: request.solicitud: {0}", request.getPayload().getSolicitud());

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

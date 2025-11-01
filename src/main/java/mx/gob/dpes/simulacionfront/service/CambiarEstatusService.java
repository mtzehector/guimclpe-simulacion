package mx.gob.dpes.simulacionfront.service;

import mx.gob.dpes.simulacionfront.exception.CambiaEstatusException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class CambiarEstatusService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private SolicitudClient solicitudClient;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {

        if (request.getPayload().getValidacionTablaAmortizacionExitosa())
            return request;

        try {
            log.log(Level.INFO,
                    "SE CAMBIARA ESTADO A {0}-{1}, DEBIDO A MONTOS NEGATIVOS Y/O DESCUENTO DIFERENTE, ID SOLICITUD: {2}",
                    new Object[]{
                            TipoEstadoSolicitudEnum.CANCELADO_DIFERENTE_TABLA_AMORTIZACION.getDescripcion(),
                            TipoEstadoSolicitudEnum.CANCELADO_DIFERENTE_TABLA_AMORTIZACION.toValue(),
                            request.getPayload().getSolicitud().getId()
                    }
            );
            request.getPayload().getSolicitud().setEstadoSolicitud(TipoEstadoSolicitudEnum.CANCELADO_DIFERENTE_TABLA_AMORTIZACION);
            request.getPayload().getSolicitud().setCveEstadoSolicitud(new EstadoSolicitud(TipoEstadoSolicitudEnum.CANCELADO_DIFERENTE_TABLA_AMORTIZACION.toValue()));
            solicitudClient.updateEstado(request.getPayload().getSolicitud());
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR CambiarEstatusService = {0}", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new CambiaEstatusException(CambiaEstatusException.ERROR_MONTOS_NEGATIVOS_O_DECUENTO_DIFERENTE),
                null
        );

    }
}

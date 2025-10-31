/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.PrestamoClient;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class PrestamoRecuperacionService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Inject
    @RestClient
    private SolicitudClient solicitudClient;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO, "Step 8 ");
        log.log(Level.INFO, ">>>PrestamoRecuperacionService Request Prestamo Recuperacion Back ={0}", request.getPayload());
        log.log(Level.INFO, ">>>PrestamoRecuperacionService solicitud={0}", request.getPayload().getSolicitud());
        log.log(Level.INFO, ">>>PrestamoRecuperacionService solicitud.getMejorOferta={0}", request.getPayload().getSolicitud().getMejorOferta());
        Boolean flatMejorOferta = false;
        // Se ejecuta si contiene prestamos en recuperación.
        if (request.getPayload().getLstPrestamoRecuperacion() != null) {

            if (!request.getPayload().getLstPrestamoRecuperacion().isEmpty()) {

                List<PrestamoRecuperacion> prestamoRecuperacionOut = new ArrayList<>();
                int i = 0;
                for (PrestamoRecuperacion p : request.getPayload().getLstPrestamoRecuperacion()) {
                    p.setSolicitud(
                            request.getPayload().getSolicitud().getId()
                    );

                    p.setPrestamo(
                            request.getPayload().getPrestamo().getId()
                    );

                    p.setImpRealPrestamo(
                            p.getCanMontoSol()
                    );
                    
                    p.setCanMontoSol(null);
                    
                    String cveEFSipre = p.getNumEntidadFinanciera().trim();
                    p.setNumEntidadFinanciera(cveEFSipre);

                    if (p.getMejorOferta() == 1) {
                        flatMejorOferta = true;
                    }
                    if(p.getNumEntidadFinanciera().equals(request.getPayload().getOferta().getEntidadFinanciera().getIdSipre())){
                        p.setMontoActualizado(1L);
                    }
                    Response load = service.prestamoEnRecuperacion(p);
                    if (load.getStatus() == 200) {
                        PrestamoRecuperacion prestamoRecuperacion = load.readEntity(PrestamoRecuperacion.class);
                        log.log(Level.INFO, ">>>PrestamoRecuperacionService prestamoRecuperacion[" + (i++) + "]={0}", prestamoRecuperacion + " flatMejorOferta=" + flatMejorOferta);
                        prestamoRecuperacion.setCorreoAdminEF(p.getCorreoAdminEF());
                        prestamoRecuperacionOut.add(prestamoRecuperacion);
                    }
                }

                request.getPayload().setLstPrestamoRecuperacion(prestamoRecuperacionOut);
                //if (flatMejorOferta) {
                log.log(Level.INFO, "COMPRA DE CARTERA SIN IMPORTAR EL CAT, TIPO CREDITO {0} ", request.getPayload().getPrestamo().getTipoCreditoId());
                if (request.getPayload().getPrestamo().getTipoCreditoId() == 3 || request.getPayload().getPrestamo().getTipoCreditoId() == 6 ){
                    //Solicitud s = request.getPayload().getSolicitud();
                    //s.setEstadoSolicitud(TipoEstadoSolicitudEnum.PENDIENTE_MONTO_LIQUIDAR);
                    request.getPayload().getSolicitud().setMejorOferta(0);
                    request.getPayload().getSolicitud().setEstadoSolicitud(TipoEstadoSolicitudEnum.PENDIENTE_MONTO_LIQUIDAR);
                    request.getPayload().getSolicitud().setCveEstadoSolicitud(new EstadoSolicitud(TipoEstadoSolicitudEnum.PENDIENTE_MONTO_LIQUIDAR.toValue()));
                    //solicitudClient.updateEstado(s);
                }
                log.log(Level.INFO, "Step 8: {0}", request.getPayload().getSolicitud());

                return new Message<>(request.getPayload());

            } else {
                return new Message<>(request.getPayload());
            }
        } else {
            return new Message<>(request.getPayload());
        }
    }
}

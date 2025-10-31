/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.HashMap;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.exception.SimulacionException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ValidateSimulacionService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO, "Step 1");
        log.log(Level.INFO, ">>>ValidateSimulacionService request.getPayload().getSolicitud().getMejorOpcion()="+request.getPayload().getSolicitud().getMejorOferta());
        if (request.getPayload().getPensionado().getCurp() == null 
             || "".equals( request.getPayload().getPensionado().getCurp() ) ) {
            throw new SimulacionException(SimulacionException.TEST, "CURP");
        }
        if (request.getPayload().getPrestamo().getIdOferta() == null) {
            throw new SimulacionException(SimulacionException.TEST, "Oferta");
        }
        if (request.getPayload().getPensionado().getNss() == null 
           || "".equals(request.getPayload().getPensionado().getNss())) {
            throw new SimulacionException(SimulacionException.TEST, "NSS");
        }
        if (request.getPayload().getPensionado().getGrupoFamiliar() == null 
           || "".equals(request.getPayload().getPensionado().getGrupoFamiliar())) {
            throw new SimulacionException(SimulacionException.TEST, "Grupo Familiar");
        }
        if (request.getPayload().getPensionado().getDelegacion() == null ) {
            throw new SimulacionException(SimulacionException.TEST, "Delegación");
        }

        return request;
    }

}

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
import mx.gob.dpes.simulacionfront.assembler.PrestamoRecuperacionAssembler;
import mx.gob.dpes.simulacionfront.exception.SimulacionException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.PrestamoClient;
import mx.gob.dpes.simulacionfront.rules.CreatePrestamoRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class PrestamoService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Inject
    CreatePrestamoRule rule;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO,"Step 4");
        int tipoCreditoId = request.getPayload().getPrestamo().getTipoCreditoId();
        Prestamo prestamo;
        if(request.getPayload().getPrestamoRecuperacion() != null){
            PrestamoRecuperacionAssembler assembler = new PrestamoRecuperacionAssembler();
            prestamo = assembler.assemble(request.getPayload());
            log.log(Level.INFO, ">>> Prestamo Assembler {0}", prestamo);
            
        }else{
            prestamo = rule.apply(request.getPayload());
            log.log(Level.INFO, "Request Prestamo Back {0}", prestamo);
        }
        
        Response load = service.load(prestamo);
        if (load.getStatus() == 200) {
            Prestamo prestamoOut = load.readEntity(Prestamo.class);
            prestamoOut.setTipoCreditoId(tipoCreditoId);
            request.getPayload().setPrestamo(prestamoOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

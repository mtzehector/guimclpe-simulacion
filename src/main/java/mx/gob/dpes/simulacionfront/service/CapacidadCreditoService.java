package mx.gob.dpes.simulacionfront.service;

import mx.gob.dpes.simulacionfront.assembler.CapacidadCreditoAssembler;
import mx.gob.dpes.simulacionfront.exception.SimulacionException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.CapacidadCreditoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class CapacidadCreditoService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private CapacidadCreditoClient capacidadCreditoClient;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {

        CapacidadCreditoAssembler capacidadCreditoAssembler = new CapacidadCreditoAssembler();
        CapacidadCredito capacidadCredito = capacidadCreditoAssembler.assemble(request.getPayload());
        log.log(Level.INFO, "Step 4.1 >>> CapacidadCredito Assembler {0}", capacidadCredito);

        Response load = capacidadCreditoClient.load(capacidadCredito);
        if (load.getStatus() == 200) {
            CapacidadCredito capacidadCreditoOut = load.readEntity(CapacidadCredito.class);
            request.getPayload().setCapacidadCredito(capacidadCreditoOut);
            log.log(Level.INFO, "Response request.getPayload().getCapacidadCredito(): {0}", request.getPayload().getCapacidadCredito());
            return new Message<>(request.getPayload());
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }
}

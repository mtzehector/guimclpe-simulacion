/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.assembler.RegistroPrestamoAssembler;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.ConsultaSistrapClient;
import mx.gob.dpes.simulacionfront.rules.CreatePrestamoRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.SIPRELoanException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class PrestamoSIPREService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private ConsultaSistrapClient sipreClient;

    @Inject
    CreatePrestamoRule rule;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO, ">>> simulacionFront PrestamoSIPREService.execute request={0}", request);
        RegistroPrestamoAssembler assembler = new RegistroPrestamoAssembler();
        RegistroPrestamoRequest registroPrestamoRequest = assembler.assemble(request.getPayload());
        //Prestamo prestamo = rule.apply(request.getPayload());
        log.log(Level.INFO, ">> simulacionFront PrestamoSIPREService.execute registroPrestamoRequest= {0}", registroPrestamoRequest);
        try {
            RegistroPrestamoResponse response = sipreClient.save(registroPrestamoRequest);
            int codigoError = 0;
            try {
                codigoError = Integer.parseInt(response.getCodigoError());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (codigoError == 200) {
                return new Message<>(request.getPayload());
            } else {
                log.log(Level.SEVERE, ">>>ERROR! simulacionFront PrestamoSIPREService.execute Error Msg= {0}", response.getMensajeError());
                throw new SIPRELoanException(codigoError);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknowException();
        }
        //return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

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
import mx.gob.dpes.simulacionfront.model.ControlFolioModel;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.NumeroControlPersistenciaClient;
import mx.gob.dpes.simulacionfront.rules.CreateControlFolioRule;
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
public class ControlService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private NumeroControlPersistenciaClient service;
    
    @Inject
    private CreateControlFolioRule rule;

    
    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO,"Step 5");
        //se consigue numero consecutivo
        ControlFolioModel control = rule.apply(request.getPayload());
        log.log(Level.INFO, "Request ControlInfo {0}", control);
        Response load = service.load(control);
        if (load.getStatus() == 200) {
            ControlFolioModel controlOut = load.readEntity(ControlFolioModel.class);
            request.getPayload().setConsecutivo(controlOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }

}

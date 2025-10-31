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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.exception.CartaInstruccionException;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.BitacoraClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.Bitacora;
import org.eclipse.microprofile.rest.client.inject.RestClient;


/**
 *
 * @author juanf.barragan
 */
@Provider
public class ConsultarBitacoraService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {
    
    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;
    
    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        try {
            Bitacora bitRequest = new Bitacora();
            bitRequest.setIdSolicitud(request.getPayload().getSolicitud().getId());

            Response response = bitacoraClient.byIdSolicitud(bitRequest);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                List<Bitacora> bitacoras = new ArrayList<Bitacora>();
                try {
                    bitacoras = response.readEntity(new GenericType<List<Bitacora>>() {});
                } catch (Exception e) {
                    log.log(Level.INFO, "La respuesta de la bitacora genero Error {0}", e.getMessage());
                }

                request.getPayload().setBitacoras(bitacoras);
                return request;
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarBitacoraService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }
    
}

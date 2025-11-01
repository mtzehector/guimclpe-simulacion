package mx.gob.dpes.simulacionfront.service;

import mx.gob.dpes.simulacionfront.exception.EntidadFinancieraException;
import mx.gob.dpes.simulacionfront.model.EntidadFinancieraLogo;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraFrontClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ConsultaEntidadFinancieraLogo extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraFrontClient entidadFinancieraFrontClient;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        try {
            //log.log(Level.INFO, "Obteniendo logo de la entidad financiera: {0}", request.getPayload().getSolicitud().getCveEntidadFinanciera());
            Response response = entidadFinancieraFrontClient.obtieneLogo(
                request.getPayload().getSolicitud().getCveEntidadFinanciera());
            if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
                EntidadFinancieraLogo logoEntidadFinanciera = response.readEntity(EntidadFinancieraLogo.class);
                request.getPayload().getOferta().getEntidadFinanciera().setImgB64(logoEntidadFinanciera.getArchivo());
                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE,
                "ERROR ConsultaEntidadFinancieraLogo.execute - obteniendo logo de la entidad financiera = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new EntidadFinancieraException(), null);
    }
}

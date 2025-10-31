package mx.gob.dpes.simulacionfront.service;

import mx.gob.dpes.simulacionfront.exception.OfertaException;
import mx.gob.dpes.simulacionfront.model.CondicionOfertaRequest;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoInsumoTaClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoInsumoTa;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ConsultarOfertaService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraClient efClient;

    @Inject
    @RestClient
    private PrestamoInsumoTaClient prestamoInsumoTaClient;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> resumenSimulacionRequest)
        throws BusinessException {

        try {
            CondicionOfertaRequest ofertaRequest = new CondicionOfertaRequest();
            ofertaRequest.setClave(resumenSimulacionRequest.getPayload().getPrestamo().getIdOferta());

            Response responseEfClient = efClient.load(ofertaRequest);
            if (responseEfClient != null && responseEfClient.getStatus() == Response.Status.OK.getStatusCode()) {
                Oferta oferta = responseEfClient.readEntity(Oferta.class);
                Response responsePrestamoInsumo = prestamoInsumoTaClient.obtenerPrestamoInsumoPorCveSolicitud(
                        resumenSimulacionRequest.getPayload().getSolicitud().getId()
                );

                if (responsePrestamoInsumo != null && responsePrestamoInsumo.getStatus() ==
                    Response.Status.OK.getStatusCode()) {
                    PrestamoInsumoTa prestmoInsumo = responsePrestamoInsumo.readEntity(PrestamoInsumoTa.class);
                    oferta.setCat(prestmoInsumo != null && prestmoInsumo.getCat() == null ? 0.0 :
                        prestmoInsumo.getCat().doubleValue());
                    resumenSimulacionRequest.getPayload().setOferta(oferta);
                    return resumenSimulacionRequest;
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarOfertaService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new OfertaException(), null);
    }
}

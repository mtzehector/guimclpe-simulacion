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

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.CondicionOfertaRequest;
import mx.gob.dpes.simulacionfront.model.EntidadFinancieraLogo;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraClient;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraFrontClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarEntidadFinancieraService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    @Inject
    @RestClient
    private EntidadFinancieraFrontClient entidadFinancieraFrontClient;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>simulacionFront|ConsultarEntidadFinancieraService|execute: {0}", request.getPayload());
        
        CondicionOfertaRequest condicionOfertaRequest = new CondicionOfertaRequest();
        condicionOfertaRequest.setClave(request.getPayload().getPrestamo().getIdOferta());
        
        Response respuesta = entidadFinancieraClient.load(condicionOfertaRequest);

        if (respuesta.getStatus() == 200) {
            Oferta oferta = respuesta.readEntity(Oferta.class);
            request.getPayload().setOferta(oferta);
            try {
                Response imgEF = entidadFinancieraFrontClient.obtieneLogo(oferta.getEntidadFinanciera().getId());

                if (imgEF.getStatus() == 200) {
                    EntidadFinancieraLogo efl = imgEF.readEntity(EntidadFinancieraLogo.class);
                    request.getPayload().getOferta().getEntidadFinanciera().setImgB64(efl.getArchivo());
                }
            } catch (Exception e) {
                log.log(Level.INFO, ">>>No se econtraro el LOGO");
                EntidadFinancieraLogo efl = new EntidadFinancieraLogo();
                request.getPayload().getOferta().getEntidadFinanciera().setImgB64(efl.getArchivo());
                return new Message<>(request.getPayload());
            }
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }
}











/*
@Provider
public class ConsultarEntidadFinancieraService extends ServiceDefinition<EntidadFinancieraRequest, EntidadFinancieraRequest> {

    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {

        CondicionOfertaRequest condicionOfertaRequest = new CondicionOfertaRequest();
        condicionOfertaRequest.setClave(request.getPayload().getCondicionOfertaRequest().getClave());
        request.getPayload().setCondicionOfertaRequest(condicionOfertaRequest);

        log.log(Level.INFO, "El request de la condicion oferta es : {0}", request.getPayload().getCondicionOfertaRequest());
        Response respuesta = entidadFinancieraClient.load(request.getPayload().getCondicionOfertaRequest());

        if (respuesta.getStatus() == 200) {
            Oferta oferta = respuesta.readEntity(Oferta.class);
            request.getPayload().setOferta(oferta);
            log.log(Level.INFO, "Los datos de la condicion oferta y entidad financiera son : {0}", request.getPayload().getOferta());

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }
}
*/

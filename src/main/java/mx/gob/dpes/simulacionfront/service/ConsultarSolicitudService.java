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
import mx.gob.dpes.simulacionfront.model.RequestPromotorModel;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.PromotorFrontClient;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarSolicitudService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private SolicitudClient solicitudClient;

    @Inject
    @RestClient
    private PromotorFrontClient promotorClient;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        Solicitud solicitudEntity = null;

        try {
            //log.log(Level.INFO, ">>>>ConsultarSolicitudService  request.getPayload().getSolicitud().getId()={0}",
            //    request.getPayload().getSolicitud().getId());
            Response respuesta = solicitudClient.load(request.getPayload().getSolicitud().getId());

            if (!(respuesta != null && respuesta.getStatus() == Response.Status.OK.getStatusCode()))
                return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);

            solicitudEntity = respuesta.readEntity(Solicitud.class);
            //log.log(Level.INFO, ">>>>ConsultarSolicitudService solicitudEntity= {0}", solicitudEntity);
            solicitudEntity.setIdSolPrFinanciero(request.getPayload().getSolicitud().getIdSolPrFinanciero());
            request.getPayload().setSolicitud(solicitudEntity);

            if (solicitudEntity.getCvePromotor() != null) {
                Response responsePromotorClient = promotorClient.obtenerPorClave(solicitudEntity.getCvePromotor());
                RequestPromotorModel promotor = responsePromotorClient.readEntity(RequestPromotorModel.class);
                request.getPayload().setPromotor(promotor);
            }
            else
                request.getPayload().setPromotor(new RequestPromotorModel());

            //log.log(Level.INFO, ">>>FRONT SIMULACION STEP 1 IF RESULT: {0}", request);
            return new Message<>(request.getPayload());
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarSolicitudService.execute = {0}", e);

            if(solicitudEntity != null && solicitudEntity.getCveEstadoSolicitud() != null &&
                solicitudEntity.getCveEstadoSolicitud().getId() != null &&
                solicitudEntity.getCveEstadoSolicitud().getId() != 1L) {
                request.getPayload().setPromotor(new RequestPromotorModel());
                return new Message<>(request.getPayload());
            }
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }

}

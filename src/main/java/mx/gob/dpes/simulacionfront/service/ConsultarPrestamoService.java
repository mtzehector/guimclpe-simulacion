/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.ConsultaDescuentos;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.ConsultaSistrapClient;
import mx.gob.dpes.simulacionfront.restclient.ConsultaTAClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosPorSolicitudRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarPrestamoService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private PrestamoClient prestamoClient;

    @Inject
    @RestClient
    private ConsultaTAClient client;

    @Inject
    @RestClient
    private ConsultaSistrapClient clienteConsultaSistrap;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        try {
            //log.log(Level.INFO, ">>>simulacionFront|ConsultarPrestamoService|execute {0}", request.getPayload().getSolicitud());
            AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
            amortizacionInsumos.setCveSolicitud(request.getPayload().getSolicitud().getId());

            Response amortizacionInsumosResponse = client.obtenerCatPromotor(amortizacionInsumos);
            if (amortizacionInsumosResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                amortizacionInsumos = amortizacionInsumosResponse.readEntity(AmortizacionInsumos.class);
                //log.log(Level.INFO, ">>>>ConsultarPrestamoService amortizacionInsumos = {0}", amortizacionInsumos);
                request.getPayload().setCatPromotor(amortizacionInsumos.getCat());
            }

            //ConsultaDescuentosPorSolicitudRequest rq = new ConsultaDescuentosPorSolicitudRequest(request.getPayload().getSolicitud().getNumFolioSolicitud(),"" );
            ConsultaDescuentosPorSolicitudRequest rq = new ConsultaDescuentosPorSolicitudRequest(
                request.getPayload().getSolicitud().getIdSolPrFinanciero(),
                request.getPayload().getSolicitud().getNumFolioSolicitud());
            //log.log(Level.INFO, "REQUEST DESCUENTOS: {0}", rq);

            Response loadDesc = clienteConsultaSistrap.load(rq);
            if (loadDesc.getStatus() == Response.Status.OK.getStatusCode()) {
                ConsultaDescuentos cd = loadDesc.readEntity(ConsultaDescuentos.class);
                request.getPayload().setDescuentosAplicados(cd.getResponse().getDescuentosAplicados());
            }

            //log.log(Level.INFO, "REQUEST JGV request.getPayload().getSolicitud().getId() 1: {0}", request.getPayload().getSolicitud());
            Response respuesta = prestamoClient.load(request.getPayload().getSolicitud().getId());
            //log.log(Level.INFO, "REQUEST JGV respuesta 2: {0}", respuesta);
            //log.log(Level.INFO, "REQUEST JGV respuesta.getStatus() 3: {0}", respuesta.getStatus());
            if (respuesta.getStatus() == Response.Status.OK.getStatusCode()) {

                Prestamo prestamo = respuesta.readEntity(Prestamo.class);

                //log.log(Level.INFO, "REQUEST JGV prestamo 4: {0}", prestamo);

                request.getPayload().setPrestamo(prestamo);
                Response load = client.load(amortizacionInsumos);
                if (load.getStatus() == Response.Status.OK.getStatusCode()) {
                    request.getPayload().setTablaAmort(load.readEntity(List.class));
                }

                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPrestamoService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }

}

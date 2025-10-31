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
 * @author juanf.barragan
 */
@Provider
public class ConsultarPrestamoExcelService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest>  {
    
    @Inject
    @RestClient
    private ConsultaTAClient client;
    
    @Inject
    @RestClient
    private PrestamoClient prestamoClient;
    
    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request) throws BusinessException {
    
        log.log(Level.INFO, ">>>simulacionFront|ConsultarPrestamoService|execute {0}", request.getPayload().getSolicitud());
        AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
        amortizacionInsumos.setCveSolicitud(request.getPayload().getSolicitud().getId());

        if (request.getPayload().getSolicitud().getCveOrigenSolicitud() != null) {
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 4) {
                Response catPromotor = client.obtenerCatPromotor(amortizacionInsumos);
                if (catPromotor.getStatus() == 200) {
                    AmortizacionInsumos cat = catPromotor.readEntity(AmortizacionInsumos.class);
                    request.getPayload().setCatPromotor(cat.getCat());
                }
            }
        }
        
        Response respuesta = prestamoClient.load(request.getPayload().getSolicitud().getId());
        
        if (respuesta.getStatus() == 200) {

            Prestamo prestamo = respuesta.readEntity(Prestamo.class);

            log.log(Level.INFO, "REQUEST JGV prestamo 4: {0}", prestamo);

            request.getPayload().setPrestamo(prestamo);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }
    
}

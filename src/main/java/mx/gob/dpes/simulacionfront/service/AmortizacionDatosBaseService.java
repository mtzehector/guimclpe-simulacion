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

import mx.gob.dpes.simulacionfront.assembler.AmortizacionDatosBaseAssembler;
import mx.gob.dpes.simulacionfront.exception.SimulacionException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.AmortizacionClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * @author edgar.arenas
 */
@Provider
public class AmortizacionDatosBaseService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private AmortizacionClient client;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        try {
            log.log(Level.INFO,
                    "PENSIONADO CURP: {0}, DESCUENTO MENSUAL: {1}, CAT: {2}, MONTO TOTAL PRESTAMO: {3}, PLAZO {4}",
                    new Object[]{
                            request.getPayload().getPensionado().getCurp(),
                            request.getPayload().getOfertaDatos().getDescuentoMensual(),
                            request.getPayload().getOfertaDatos().getCat(),
                            request.getPayload().getOfertaDatos().getMonto(),
                            request.getPayload().getOfertaDatos().getDescripcionNumPlazo()
                    });
            AmortizacionDatosBaseAssembler assembler = new AmortizacionDatosBaseAssembler();
            Response load = client.guardarDatosBaseAmortizacion(assembler.assemble(request.getPayload()));
            if (load != null && load.getStatus() == 200) {
                AmortizacionInsumos insumos = load.readEntity(AmortizacionInsumos.class);
                request.getPayload().setAmortizacionInsumos(insumos);
                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR AmortizacionDatosBaseService.execute = {0}", e);
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new SimulacionException(), null);
    }
}

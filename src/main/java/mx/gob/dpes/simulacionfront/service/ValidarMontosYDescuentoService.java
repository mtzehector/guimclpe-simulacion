package mx.gob.dpes.simulacionfront.service;

import mx.gob.dpes.simulacionfront.exception.ValidaSimulacionMontosNegativosException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.ConsultaTAClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoInsumoTaClient;
import mx.gob.imss.dpes.common.constants.Constantes;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionPorDescuento;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoInsumoTa;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

@Provider
public class ValidarMontosYDescuentoService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private ConsultaTAClient prestamoTAClient;

    @Inject
    @RestClient
    private PrestamoInsumoTaClient prestamoInsumoTAClient;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        try {
            Collection<AmortizacionPorDescuento> tablaAmortizacion = null;
            PrestamoInsumoTa prestamoInsumo = null;
            AmortizacionInsumos prestamoTA = new AmortizacionInsumos();
            prestamoTA.setCveSolicitud(request.getPayload().getSolicitud().getId());
            Response respuestaPrestamoTA = prestamoTAClient.load(prestamoTA);
            if (respuestaPrestamoTA != null && respuestaPrestamoTA.getStatus() == 200) {
                tablaAmortizacion = respuestaPrestamoTA.readEntity(new GenericType<Collection<AmortizacionPorDescuento>>() {});
            }
            Response respuestaPrestamoInsumoTA = prestamoInsumoTAClient.obtenerPrestamoInsumoPorCveSolicitud(request.getPayload().getSolicitud().getId());
            if (respuestaPrestamoInsumoTA != null && respuestaPrestamoInsumoTA.getStatus() == 200) {
                prestamoInsumo = respuestaPrestamoInsumoTA.readEntity(PrestamoInsumoTa.class);
            }
            request.getPayload().setValidacionTablaAmortizacionExitosa(this.isMontosNegativosTablaAmortizacion(new ArrayList<AmortizacionPorDescuento>(tablaAmortizacion), prestamoInsumo) ? false : true);
            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ValidaSimulacionMontosNegativosService = {0}", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new ValidaSimulacionMontosNegativosException(ValidaSimulacionMontosNegativosException.ERROR_AL_INVOCAR_SERVICIO_PRESTAMO_TA),
                null
        );
    }

    private boolean isMontosNegativosTablaAmortizacion(List<AmortizacionPorDescuento> tablaAmortizacion, PrestamoInsumoTa insumoTa) {
        if (tablaAmortizacion == null || tablaAmortizacion.size() == 0 || insumoTa == null)
            return false;

        boolean montoNegativoDescuentoDiferente = false;
        double descuentoMensual = insumoTa.getDescuentoMensual() != null ? insumoTa.getDescuentoMensual().doubleValue() : 0.0;
        for (AmortizacionPorDescuento amortizacion : tablaAmortizacion) {
            if (amortizacion.getPeriodo().equals(0))
                continue;
            if (amortizacion.getSaldo() < Constantes.CERO.doubleValue() || amortizacion.getCapital() < Constantes.CERO.doubleValue() || amortizacion.getIntereses() < Constantes.CERO.doubleValue() || amortizacion.getIva() < Constantes.CERO.doubleValue()) {
                log.log(Level.INFO, "SE ENCONTRARON MONTOS NEGATIVOS EN TABLA AMORTIZACION: {0}", amortizacion);
                montoNegativoDescuentoDiferente = true;
            }
            if (descuentoMensual != amortizacion.getDescuento()) {
                log.log(Level.INFO,
                        "SE ENCONTRO DESCUENTO DIFERENTE EN TABLA AMORTIZACION: {0}, DESCUENTO PRESTAMO_INSUMO_TA: {1}, DESCUENTO PRESTAMO_TA: {2}",
                        new Object[]{
                                amortizacion,
                                descuentoMensual,
                                amortizacion.getDescuento()
                        });
                montoNegativoDescuentoDiferente = true;
            }
            if (montoNegativoDescuentoDiferente)
                break;
        }
        return montoNegativoDescuentoDiferente;
    }


}

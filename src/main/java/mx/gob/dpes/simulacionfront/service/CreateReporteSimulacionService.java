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
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.PrestamoRecuperacionIdRq;
import mx.gob.dpes.simulacionfront.model.PrestamosEnRecuperacionRequest;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacion;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.basereport.service.ReporteService;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionPorDescuento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.dpes.simulacionfront.restclient.ConsultaTAClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoBackClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoFrontClient;
import mx.gob.imss.dpes.interfaces.prestamo.model.CapitalInsolutoRQ;
import mx.gob.imss.dpes.interfaces.prestamo.model.CapitalInsolutoRS;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author antonio
 */
@Provider
public class CreateReporteSimulacionService extends ServiceDefinition<ReporteResumenSimulacion, ReporteResumenSimulacion> {

    @Inject
    private ReporteService service;

    @Inject
    @RestClient
    private ConsultaTAClient client;

    @Inject
    @RestClient
    private PrestamoFrontClient prestamoFrontClient;

    @Inject
    @RestClient
    private PrestamoBackClient backClient;

    @Override
    public Message<ReporteResumenSimulacion> execute(Message<ReporteResumenSimulacion> request) throws BusinessException {

        log.log(Level.INFO, "Armando el reporte PDF: {0}", request.getPayload());

        Reporte<ResumenSimulacion> reporte = request.getPayload().getReporte();
        reporte.setRuta("/reports/Simulacion.jasper");

        AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
        amortizacionInsumos.setCveSolicitud(request.getPayload().getIdSolicitud());
        Response load = client.load(amortizacionInsumos);

        if (load.getStatus() == 200) {

            List<AmortizacionPorDescuento> tablaAmort = load.readEntity(List.class);
            JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(tablaAmort);

            // Obtiene los prestamos en Recuperacion
            List<PrestamoRecuperacion> prestamosPorLiquidar = obtenerPrestamos(request.getPayload().getIdSolicitud());
            JRBeanCollectionDataSource jrbcds2 = new JRBeanCollectionDataSource(prestamosPorLiquidar);

            request.getPayload().getResumenSimulacion().setPrestamosPorLiquidar(jrbcds2);

            request.getPayload().getResumenSimulacion().setTablaAmort(jrbcds);

            reporte.getBeans().add(request.getPayload().getResumenSimulacion());

            log.log(Level.INFO, "Antes de Generar Reporte JGV: {0}", request.getPayload().getResumenSimulacion());

            Message<Reporte> response = service.execute(new Message(reporte));

            log.log(Level.INFO, "Despues de Generar Reporte JGV: {0}", response);

            if (!Message.isException(response)) {
                reporte.setPdf(response.getPayload().getPdf());
                return request;
            } else {
                return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
            }
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

    public List<PrestamoRecuperacion> obtenerPrestamos(Long id) {

        PrestamosEnRecuperacionRequest rq = new PrestamosEnRecuperacionRequest();
        PrestamoRecuperacionIdRq rqIn = new PrestamoRecuperacionIdRq();
        rqIn.setId(id);
        rq.setRequest(rqIn);

        log.log(Level.INFO, "Prestamos en recuperacion para Reporte RQ: {0}", rq);
        Response load = prestamoFrontClient.prestamosEnRecuperacion(rq);

        PrestamosEnRecuperacionRequest rq2 = load.readEntity(PrestamosEnRecuperacionRequest.class);
        log.log(Level.INFO, "Prestamos en recuperacion para Reporte RS: {0}", rq2.getResponse().getPrestamosEnRecuperacion());

        for (PrestamoRecuperacion p : rq2.getResponse().getPrestamosEnRecuperacion()) {
            CapitalInsolutoRQ caprq = new CapitalInsolutoRQ();
            caprq.setFolioSipre(p.getNumSolicitudSipre());
            caprq.setNumMensualidad(p.getNumMesRecuperado());
            caprq.setNumFolioSolicitud(p.getNumFolioSolicitud());
            try {
                Response loadSalCap = backClient.capitalinsoluto(caprq);

                if (loadSalCap.getStatus() == 200) {
                    CapitalInsolutoRS rs = loadSalCap.readEntity(CapitalInsolutoRS.class);
                    p.setSaldoCapital(rs.getSaldoCapital());
                }
            } catch (Exception e) {
            }
        }

        if (!rq2.getResponse().getPrestamosEnRecuperacion().isEmpty()) {
            return rq2.getResponse().getPrestamosEnRecuperacion();
        } else {
            return null;
        }
    }

}

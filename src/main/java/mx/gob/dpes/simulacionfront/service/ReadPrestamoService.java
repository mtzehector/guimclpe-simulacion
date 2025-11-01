/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.DiaInhabilRequest;
import mx.gob.dpes.simulacionfront.model.FechasDocumentacionRS;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.restclient.PrestamoClient;
import mx.gob.dpes.simulacionfront.restclient.SolicitudBackClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class ReadPrestamoService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Inject
    @RestClient
    private SolicitudBackClient solicitudClient;

    @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {

        log.log(Level.INFO, "Request hacia PrestamoBack: {0}", request.getPayload().getSolicitud().getId());
        Response load = service.load(request.getPayload().getSolicitud().getId());
        if (load.getStatus() == 200) {
            Prestamo prestamo = load.readEntity(Prestamo.class);
            request.getPayload().setPrestamo(prestamo);
            log.log(Level.INFO, "Response hacia PrestamoBack prestamo: {0}", prestamo);

            DiaInhabilRequest dhr = new DiaInhabilRequest();
            dhr.setFecInicio(prestamo.getAltaRegistro());
            log.log(Level.INFO, "Response hacia PrestamoBack DiaInhabilRequest: {0}", dhr);
            Response fechasR = solicitudClient.fechasParaDocumento(dhr);
            FechasDocumentacionRS fechasDocumentacionRS = fechasR.readEntity(FechasDocumentacionRS.class);
            log.log(Level.INFO, "Request JGV fechasDocumentacionRS getPeriodoNominaDelPrestamo: {0}", fechasDocumentacionRS.getPeriodoNominaDelPrestamo());
            log.log(Level.INFO, "Request JGV fechasDocumentacionRS getPeriodoNominaPosteriorAlPrestamo: {0}", fechasDocumentacionRS.getPeriodoNominaPosteriorAlPrestamo());
            
            
            
            request.getPayload().setCalendarioNominaFecFinCapturaPeriodoPrestamo(
            new SimpleDateFormat("dd/MM/yyyy")
                .format(fechasDocumentacionRS.getPeriodoNominaDelPrestamo().getFecFin())
            );
            request.getPayload().setCalendarioNominaFecDescuentoPeriodoPosteriorPrestamo(
            new SimpleDateFormat("dd/MM/yyyy")
                .format(fechasDocumentacionRS.getPeriodoNominaPosteriorAlPrestamo().getFecDescuento())
            );
            
            // Incluier el assembler
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}

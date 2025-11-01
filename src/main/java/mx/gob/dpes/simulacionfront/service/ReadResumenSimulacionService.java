/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.text.DecimalFormat;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacion;
import mx.gob.dpes.simulacionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author antonio
 */
@Provider
public class ReadResumenSimulacionService extends ServiceDefinition<ReporteResumenSimulacion, ReporteResumenSimulacion> {

    @Inject
    private DocumentoClient client;

    @Override
    public Message<ReporteResumenSimulacion> execute(Message<ReporteResumenSimulacion> request) throws BusinessException {

        log.log(Level.INFO, "Buscando el resumen de la solicitud: {0}", request.getPayload().getIdSolicitud());

        Documento documento = new Documento();
        documento.setCveSolicitud(request.getPayload().getIdSolicitud());
        documento.setTipoDocumento(TipoDocumentoEnum.RESUMEN_SIMULACION);
        log.log(Level.INFO, "Buscando el resumen ddocto: {0}", documento);

        Response response = client.loadRefDocumento(documento);

        if (response.getStatus() == 200) {
            ResumenSimulacion resumenSimulacion = response.readEntity(ResumenSimulacion.class);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            resumenSimulacion.setMontoSolicitado(
                    formatter.format(Double.parseDouble(resumenSimulacion.getMontoSolicitado()))
            );
            resumenSimulacion.setImporteDescNomina(
                    formatter.format(Double.parseDouble(resumenSimulacion.getImporteDescNomina()))
            );
            resumenSimulacion.setTotalCredPagarInt(
                    formatter.format(Double.parseDouble(resumenSimulacion.getTotalCredPagarInt()))
            );

            resumenSimulacion.setTipoCredito(resumenSimulacion.getTipoCredito().toUpperCase());
            resumenSimulacion.setTipoPension(resumenSimulacion.getTipoPension().toUpperCase());
            resumenSimulacion.setTipoTrabajador(resumenSimulacion.getTipoTrabajador().toUpperCase());

            resumenSimulacion.setCiudad(WordUtils.capitalize(resumenSimulacion.getCiudad()));

            log.log(Level.INFO, "Resumen obtenido: {0}", resumenSimulacion);
            request.getPayload().setResumenSimulacion(resumenSimulacion);
            return request;
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}

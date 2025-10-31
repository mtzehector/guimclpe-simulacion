/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.dpes.simulacionfront.exception.CartaInstruccionException;
import mx.gob.dpes.simulacionfront.model.DocumentoRequest;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * @author salvador.pocteco
 */
@Provider
public class ConsultarDocumentosService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
            throws BusinessException {

        try {
            //log.log(Level.INFO, "Servicio de consulta de documentos de carta de instrucción");
            //log.log(Level.INFO, "El request de la solicitud es : {0}", request.getPayload().getSolicitud().getId());
            DocumentoRequest documentoRequest = new DocumentoRequest();
            documentoRequest.setCveSolicitud(request.getPayload().getSolicitud().getId().toString());
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == OrigenSolicitudEnum.REINSTALACION.getId())
                documentoRequest.setTiposDocumento(Arrays.asList(TipoDocumentoEnum.CARTA_REINSTALACION.getTipo()));
            else
                documentoRequest.setTiposDocumento(Arrays.asList(
                                TipoDocumentoEnum.CARTA_INSTRUCCION.getTipo(),
                                TipoDocumentoEnum.IDENTIFICACION_OFICIAL.getTipo(),
                                TipoDocumentoEnum.CONTRATO.getTipo(),
                                TipoDocumentoEnum.RESUMEN_SIMULACION.getTipo(),
                                TipoDocumentoEnum.CEP_PENSIONADO.getTipo(),
                                TipoDocumentoEnum.CEP_ENTIDAD_FINANCIERA.getTipo(),
                                TipoDocumentoEnum.CEP_PENSIONADO_XML.getTipo(),
                                TipoDocumentoEnum.CEP_ENTIDAD_FINANCIERA_XML.getTipo()
                ));

            Response respuesta = documentoClient.load(documentoRequest);
            if (respuesta.getStatus() == Response.Status.OK.getStatusCode()) {

                List<Documento> documentos = respuesta.readEntity(new GenericType<List<Documento>>() {
                });
                for (Documento documento : documentos) {
                    documento.setDescTipoDocumento(documento.getTipoDocumento().getDescripcion());
                }
                request.getPayload().setDocumentos(documentos);

                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarDocumentosService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }

}

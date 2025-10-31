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
import mx.gob.dpes.simulacionfront.assembler.ResumenSimulacionAssembler;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacion;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class CreateReporteSimulacionCompletoService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

  @Inject
  @RestClient
  DocumentoClient client;
  
  @Inject
  ResumenSimulacionAssembler assembler;
    
  @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {
        
        log.log( Level.INFO, "Armando el reporte: {0}", request.getPayload());
        
        Documento documento = assembler.assemble(request.getPayload());
        
        Response createResponse = client.create(documento);
        if( createResponse.getStatus() == 200 ){
          Documento persisted = createResponse.readEntity(Documento.class);
          request.getPayload().setDocumento(persisted);
          return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}

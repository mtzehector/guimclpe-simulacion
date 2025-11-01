/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.endpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacion;
import mx.gob.dpes.simulacionfront.service.CreateReporteSimulacionService;
import mx.gob.dpes.simulacionfront.service.ReadResumenSimulacionService;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author antonio
 */
@Path("/reporteResumenSimulacion")
@RequestScoped
public class ReporteResumenSimulacionEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    
    @Inject ReadResumenSimulacionService readResumenSimulacionService;
    @Inject CreateReporteSimulacionService createReporteSimulacionService;
  
    @GET
    @Path("/{idSolicitud}")
    @Produces("application/pdf")    
    @Operation(summary = "Generar el reporte de resumen de simulacion",
            description = "Generar el reporte de resumen de simulacion")
    public Response create(@PathParam("idSolicitud") Long idSolicitud) throws BusinessException {
      
      ReporteResumenSimulacion request = new ReporteResumenSimulacion();
      request.setIdSolicitud(idSolicitud);
      
      ServiceDefinition[] steps = {readResumenSimulacionService,createReporteSimulacionService};
      Message<ReporteResumenSimulacion> response = createReporteSimulacionService.executeSteps(steps, new Message<>(request));

      if( !Message.isException(response) ){
      return Response.ok(response.getPayload().getReporte().getPdf()).header("Content-Disposition",
				"attachment; filename=ReporteResumenSimulacion.pdf").build();
      }
      return toResponse(response);
      
      
    }

}

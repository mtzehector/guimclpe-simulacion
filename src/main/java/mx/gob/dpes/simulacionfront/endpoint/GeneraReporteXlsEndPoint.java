/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import mx.gob.dpes.simulacionfront.exception.GeneraReporteXLSException;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
//import mx.gob.dpes.simulacionfront.service.ConsultaPrestamosRecuperacionService;
import mx.gob.dpes.simulacionfront.service.ConsultarEntidadFinancieraExcelService;
import mx.gob.dpes.simulacionfront.service.ConsultarPensionadoService;
import mx.gob.dpes.simulacionfront.service.ConsultarPrestamoExcelService;
import mx.gob.dpes.simulacionfront.service.ConsultarSolicitudExcelService;
//import mx.gob.dpes.simulacionfront.service.PersonaService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.PartialContentFlowException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.BaseModel;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import mx.gob.dpes.simulacionfront.model.RequestCreacionExcel;
import mx.gob.dpes.simulacionfront.service.GeneraReporteXlsService;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.microprofile.openapi.annotations.Operation;


/**
 *
 * @author juanf.barragan
 */
@Path("/reporteExcel")
@RequestScoped
public class GeneraReporteXlsEndPoint extends BaseGUIEndPoint<BaseModel, ResumenSimulacionRequest, BaseModel>{
    
    @Inject
    private ConsultarSolicitudExcelService consultarSolicitudService;

    @Inject
    private ConsultarPensionadoService consultarPensionadoService;

    @Inject
    private ConsultarPrestamoExcelService consultarPrestamoService;

    @Inject
    private ConsultarEntidadFinancieraExcelService consultarEntidadFinancieraService;

    //@Inject
    //PersonaService consultarPersonaService;

    //@Inject
    //ConsultaPrestamosRecuperacionService consultaPrestamosRec;
    
    @Inject
    GeneraReporteXlsService generaReporte;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener el resumen de la simulacion del credito apartir del id de la solicitud",
            description = "Obtener el resumen de la simulacion del credito apartir del id de la solicitud")
    public Response generarExcel (RequestCreacionExcel request) {
        try {
            //log.log(Level.INFO, "Request - GenerarExcel: 1 {0}", request.toString());
            List<ResumenSimulacionRequest> datos = new ArrayList<ResumenSimulacionRequest>();
            for(String soli : request.getSolicitudes()){

                ResumenSimulacionRequest simulacionRequest = new ResumenSimulacionRequest();
                Solicitud solicitud = new Solicitud();
                solicitud.setId(Long.parseLong(soli));
                simulacionRequest.setSolicitud(solicitud);


                ServiceDefinition[] steps = {
                    consultarSolicitudService,
                    consultarPrestamoService,
                    consultarPensionadoService,
                    consultarEntidadFinancieraService};
                    //consultarEntidadFinancieraService,
                    //consultaPrestamosRec};

                Message<ResumenSimulacionRequest> response =
                        consultarSolicitudService.executeSteps(steps, new Message<>(simulacionRequest));
                datos.add(response.getPayload());
                //log.log(Level.INFO, "datos - GenerarExcel: 1 ");
            }
            request.setListaRequest(datos);

            /*Aqui van el service para crear el excel */

            ServiceDefinition[] stepsReporte = { generaReporte };
            //log.log(Level.INFO, "- GenerarExcel generaReporte");
            Message<RequestCreacionExcel> responseReporte =
                generaReporte.executeSteps(stepsReporte, new Message<>(request));

            // responseReporte.getPayload().getArchivoXLSX();
            String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            if (!Message.isException(responseReporte)) {
                String nombreArchivo = "Reporte_Busqueda_Prestamos.xlsx";

                byte [] archivo = Base64.decodeBase64(responseReporte.getPayload().getArchivoXLSX().getBytes());

                return Response.ok(archivo).header("Content-Disposition",
                        "attachment; filename=" + nombreArchivo).header("Content-Type", XLSX_MIME_TYPE).build();

            }

            return Response.noContent().build();
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                    new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraReporteXlsEndPoint.generarExcel = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new GeneraReporteXLSException(), null));
        }
    }
}
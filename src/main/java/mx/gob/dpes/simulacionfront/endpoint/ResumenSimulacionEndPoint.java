/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.service.ConsultaEntidadFinancieraLogo;
import mx.gob.dpes.simulacionfront.service.ConsultaPrestamosRecuperacionService;
import mx.gob.dpes.simulacionfront.service.ConsultarBitacoraService;
import mx.gob.dpes.simulacionfront.service.ConsultarDocumentosService;
//import mx.gob.dpes.simulacionfront.service.ConsultarEntidadFinancieraService;
import mx.gob.dpes.simulacionfront.service.ConsultarOfertaService;
import mx.gob.dpes.simulacionfront.service.ConsultarPensionadoService;
import mx.gob.dpes.simulacionfront.service.ConsultarPrestamoService;
import mx.gob.dpes.simulacionfront.service.ConsultarPrestamoSipreService;
import mx.gob.dpes.simulacionfront.service.ConsultarSolicitudService;
//import mx.gob.dpes.simulacionfront.service.PersonaService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PartialContentFlowException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 * @author salvador.pocteco
 */
@Path("/resumenSimulacion")
@RequestScoped
public class ResumenSimulacionEndPoint extends BaseGUIEndPoint<BaseModel, ResumenSimulacionRequest, BaseModel> {

    @Inject
    private ConsultarSolicitudService consultarSolicitudService;

    @Inject
    private ConsultarPensionadoService consultarPensionadoService;

    @Inject
    private ConsultarPrestamoService consultarPrestamoService;

    //@Inject
    //private ConsultarEntidadFinancieraService consultarEntidadFinancieraService;

    //@Inject
    //PersonaService consultarPersonaService;

    @Inject
    ConsultaPrestamosRecuperacionService consultaPrestamosRec;

    @Inject
    ConsultarDocumentosService consultarDocumentosService;

    @Inject
    private ConsultarPrestamoSipreService consultarPrestamoSipreService;

    @Inject
    private ConsultarBitacoraService consultarBitacoraService;

    @Inject
    private ConsultarOfertaService consultarOfertaService;
    @Inject
    private ConsultaEntidadFinancieraLogo consultaEntidadFinancieraLogo;

    @GET
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener el resumen de la simulacion del credito apartir del id de la solicitud",
            description = "Obtener el resumen de la simulacion del credito apartir del id de la solicitud")
    @Override
    public Response load(ResumenSimulacionRequest resumenSimulacionRequest) {
        try {
            //log.log(Level.INFO, ">>>>>FRONT ENDPOINT INICIA SIMULACION: {0}", resumenSimulacionRequest);
            ServiceDefinition[] steps = {
                consultarSolicitudService,
                consultarPrestamoService,
                consultarPensionadoService,
                //consultarEntidadFinancieraService,
                consultaPrestamosRec,
                consultarDocumentosService,
                consultarOfertaService,
                consultaEntidadFinancieraLogo,
                consultarBitacoraService};

            Message<ResumenSimulacionRequest> response =
                consultarSolicitudService.executeSteps(steps, new Message<>(resumenSimulacionRequest));

            //log.log(Level.INFO, ">>>>>FRONT ENDPOINT FINALIZA SIMULACION: {0}", response.getPayload());
            return toResponse(response);
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ResumenSimulacionEndPoint.load = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new ResumenSimulacionException(), null));
        }
    }

    @POST
    @Path("/prestamoSipre")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestamoSipre(ResumenSimulacionRequest resumenSimulacionRequest) throws BusinessException {
        try {
            //log.log(Level.INFO, ">>>>>FRONT ENDPOINT INICIA SIMULACION: {0}", resumenSimulacionRequest);
            ServiceDefinition[] steps = {
                    consultarPensionadoService,
                    consultarPrestamoSipreService
            };
            Message<ResumenSimulacionRequest> response =
                consultarPensionadoService.executeSteps(steps, new Message<>(resumenSimulacionRequest));
                //consultarSolicitudService.executeSteps(steps, new Message<>(resumenSimulacionRequest));
            //log.log(Level.INFO, ">>>>>FRONT ENDPOINT FINALIZA SIMULACION: {0}", response.getPayload());

            return toResponse(response);
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ResumenSimulacionEndPoint.load = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new ResumenSimulacionException(), null));
        }
    }
}

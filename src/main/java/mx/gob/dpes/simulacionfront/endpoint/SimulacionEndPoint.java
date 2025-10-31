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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.service.AmortizacionDatosBaseService;
import mx.gob.dpes.simulacionfront.service.CambiarEstatusService;
import mx.gob.dpes.simulacionfront.service.CapacidadCreditoService;
import mx.gob.dpes.simulacionfront.service.ControlService;
import mx.gob.dpes.simulacionfront.service.CreateCorreoReporteResumenSimulacionService;
import mx.gob.dpes.simulacionfront.service.CreateEventSolicitudSimulacionService;
import mx.gob.dpes.simulacionfront.service.CreateReporteSimulacionCompletoService;
import mx.gob.dpes.simulacionfront.service.CreateSelloElectronicoSimulacionService;
import mx.gob.dpes.simulacionfront.service.CronJobService;
import mx.gob.dpes.simulacionfront.service.EntidadFinancieraService;
import mx.gob.dpes.simulacionfront.service.PrestamoRecuperacionService;
import mx.gob.dpes.simulacionfront.service.PrestamoSIPREService;
import mx.gob.dpes.simulacionfront.service.PrestamoService;
import mx.gob.dpes.simulacionfront.service.ReadOfertaService;
import mx.gob.dpes.simulacionfront.service.ReadOfertaSimulacionService;
import mx.gob.dpes.simulacionfront.service.ReadPensionadoService;
import mx.gob.dpes.simulacionfront.service.ReadPersonaBdtuService;
import mx.gob.dpes.simulacionfront.service.ReadPrestamoService;
import mx.gob.dpes.simulacionfront.service.ReadReporteResumenSimulacionService;
import mx.gob.dpes.simulacionfront.service.ReadResumenSimulacionService;
import mx.gob.dpes.simulacionfront.service.ReadSolicitudService;
import mx.gob.dpes.simulacionfront.service.SolicitudFolioService;
import mx.gob.dpes.simulacionfront.service.SolicitudHabilitarService;
import mx.gob.dpes.simulacionfront.service.SolicitudService;
import mx.gob.dpes.simulacionfront.service.ValidateSimulacionService;
import mx.gob.dpes.simulacionfront.service.ValidarMontosYDescuentoService;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.IdentityBaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 * @author antonio
 */
@Path("/simulacion")
@RequestScoped
public class SimulacionEndPoint extends BaseGUIEndPoint<RequestCreacionPrestamoSimulacion, BaseModel, BaseModel> {

    @Inject
    ValidateSimulacionService validate; //Step 1
    @Inject
    SolicitudService solicitudService; //Step 2
    @Inject
    ReadOfertaService readOfertaService; //Step 3
    @Inject
    PrestamoService prestamoService; //Step 4

    @Inject
    CapacidadCreditoService capacidadCreditoService; //Step 4.1
    @Inject
    ControlService controlService; //Step 5
    @Inject
    SolicitudFolioService solicitudFolioService; //Step 6
    @Inject
    CreateEventSolicitudSimulacionService createEventService;  //Events

    @Inject
    ReadSolicitudService readSolicitudService;
    @Inject
    ReadPrestamoService readPrestamoService;
    @Inject
    ReadPersonaBdtuService readPersonaService;
    @Inject
    ReadPensionadoService readPensionadoService;
    @Inject
    CreateSelloElectronicoSimulacionService createSelloElectronicoSimulacionService;
    @Inject
    CreateReporteSimulacionCompletoService createReporteSimulacionService;

    @Inject
    ReadOfertaSimulacionService readOfertaSimulacionService;

    @Inject
    PrestamoRecuperacionService prestamoRecuperacionService;

    @Inject
    CreateCorreoReporteResumenSimulacionService createCorreoReporteResumenSimulacionService;

    @Inject
    ReadResumenSimulacionService readDocumentoService;
    @Inject
    ReadReporteResumenSimulacionService readReporteResumenSimulacionService;

    @Inject
    AmortizacionDatosBaseService amortizacionBaseService;

    @Inject
    CronJobService cronJobService;

    @Inject
    PrestamoSIPREService prestamoSIPREService;  //  Step 8

    @Inject
    EntidadFinancieraService entidadFinancieraService;

    @Inject
    SolicitudHabilitarService solicitudHabilitarService;

    @Inject
    private ValidarMontosYDescuentoService validarMontosYDescuentoService;

    @Inject
    private CambiarEstatusService cambiarEstatusService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generar un prestamo por simulacion",
            description = "Generar un prestamo por simulacion")
    @Override
    public Response create(RequestCreacionPrestamoSimulacion request) throws BusinessException {
        log.log(Level.INFO, ">>>SimulacionEndPoint request=" + request);
        log.log(Level.INFO, ">>>SimulacionEndPoint request.getPrestamo=" + request.getPrestamo());
        log.log(Level.INFO, ">>>SimulacionEndPoint request.getSolicitud=" + request.getSolicitud());
        log.log(Level.INFO, ">>>SimulacionEndPoint prestamo.getTipoCredito={0}", request.getPrestamo().getTipoCredito());

        ServiceDefinition[] steps = {
                validate,
                solicitudService,
                readOfertaService,
                prestamoService,
                capacidadCreditoService,
                controlService,
                solicitudFolioService,
                prestamoRecuperacionService,
                amortizacionBaseService,
                validarMontosYDescuentoService,
                cambiarEstatusService,
                cronJobService
        };

        Message<RequestCreacionPrestamoSimulacion> response = solicitudService.executeSteps(steps, new Message<>(request));

        if (!Message.isException(response)) {
            ReporteResumenSimulacionCompleto reporteSimulacion = new ReporteResumenSimulacionCompleto();
            //reporteSimulacion.getSolicitud().setId(response.getPayload().getSolicitud().getId());
            reporteSimulacion.setSolicitud(response.getPayload().getSolicitud());

            ServiceDefinition[] steps2 = {
                    readSolicitudService,
                    readPrestamoService,
                    readOfertaSimulacionService,
                    readPensionadoService,
                    //readPersonaService,
                    createSelloElectronicoSimulacionService,
                    createReporteSimulacionService,
                    createCorreoReporteResumenSimulacionService,
                    solicitudHabilitarService // Cambia estado "falla por servicios" al que le corresponda para seguir en el flujo normal.
            };

            Message<ReporteResumenSimulacionCompleto> response2 = createReporteSimulacionService.executeSteps(steps2, new Message<>(reporteSimulacion));
            if (!Message.isException(response2)) {
                IdentityBaseModel<Long> model = new IdentityBaseModel<>();
                model.setId(response.getPayload().getSolicitud().getId());
                createEventService.execute(new Message<>(model));
            }
        }

        return toResponse(response);
    }

    @POST
    @Path("/validarentidadfinanciera")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarEF(RequestCreacionPrestamoSimulacion request) throws BusinessException {
        ServiceDefinition[] step = {entidadFinancieraService};
        Message<RequestCreacionPrestamoSimulacion> response = entidadFinancieraService.executeSteps(step, new Message<>(request));
        return Response.ok(response.getPayload().getEntidadFinanciera()).build();
    }

}

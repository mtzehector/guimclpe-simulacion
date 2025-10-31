/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.simulacionfront.config;

/**
 *
 * @author antonio
 */
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.dpes.simulacionfront.assembler.RegistroPrestamoAssembler.class);
        resources.add(mx.gob.dpes.simulacionfront.assembler.ReporteResumenSimulacionAssembler.class);
        resources.add(mx.gob.dpes.simulacionfront.assembler.ResumenSimulacionAssembler.class);
        resources.add(mx.gob.dpes.simulacionfront.endpoint.GeneraReporteXlsEndPoint.class);
        resources.add(mx.gob.dpes.simulacionfront.endpoint.ReporteResumenSimulacionEndPoint.class);
        resources.add(mx.gob.dpes.simulacionfront.endpoint.ResumenSimulacionEndPoint.class);
        resources.add(mx.gob.dpes.simulacionfront.endpoint.SimulacionEndPoint.class);
        resources.add(mx.gob.dpes.simulacionfront.rules.CreateControlFolioRule.class);
        resources.add(mx.gob.dpes.simulacionfront.rules.CreateFolioSolicitudRule.class);
        resources.add(mx.gob.dpes.simulacionfront.rules.CreatePrestamoRule.class);
        resources.add(mx.gob.dpes.simulacionfront.rules.CreateSolicitudRule.class);
        resources.add(mx.gob.dpes.simulacionfront.service.AmortizacionDatosBaseService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CadenaOriginalRule.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultaPrestamosRecuperacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarBitacoraService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarDocumentosService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarEntidadFinancieraExcelService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarEntidadFinancieraService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarNombreComercialSipre.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarPensionadoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarPrestamoExcelService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarPrestamoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarPrestamoSipreService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarSolicitudExcelService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarSolicitudService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ControlService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CorreoComponentService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CreateCorreoReporteResumenSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CreateEventSolicitudSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CreateReporteSimulacionCompletoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CreateReporteSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CreateSelloElectronicoSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CronJobService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.EntidadFinancieraService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.GeneraReporteXlsService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.PersonaService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.PrestamoRecuperacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.PrestamoSIPREService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.PrestamoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadOfertaService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadOfertaSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPensionadoReporteService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPensionadoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPersonaBdtuService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPersonaService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPrestamoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadPromotorService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadReporteResumenSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadResumenSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadSolicitudService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ReadTemplateService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.SolicitudFolioService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.SolicitudHabilitarService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.SolicitudService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ValidateSimulacionService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultarOfertaService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ConsultaEntidadFinancieraLogo.class);
        resources.add(mx.gob.imss.dpes.basereport.service.ReporteService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.ValidarMontosYDescuentoService.class);
        resources.add(mx.gob.dpes.simulacionfront.service.CambiarEstatusService.class);
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);

    }

}

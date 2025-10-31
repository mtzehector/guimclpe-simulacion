package mx.gob.dpes.simulacionfront.model;

import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoResponse;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author antonio
 */
public class ReporteResumenSimulacionCompleto extends BaseModel {

    @Getter
    @Setter
    ResumenSimulacion resumenSimulacion = new ResumenSimulacion();
    @Getter
    @Setter
    Reporte reporte = new Reporte();
    @Getter
    @Setter
    Solicitud solicitud = new Solicitud();
    @Getter
    @Setter
    Prestamo prestamo = new Prestamo();
    @Getter
    @Setter
    Oferta oferta = new Oferta();
    @Getter
    @Setter
    Documento documento = new Documento();
    @Getter
    @Setter
    SelloElectronicoResponse sello = new SelloElectronicoResponse();
    @Getter
    @Setter
    Persona persona = new Persona();
    @Getter
    @Setter
    PromotorModel promotor = new PromotorModel();
    @Getter
    @Setter
    ResumenCartaInstruccion resumenCartaInstruccion = new ResumenCartaInstruccion();
    @Getter
    @Setter
    String calendarioNominaFecFinCapturaPeriodoPrestamo;
    @Getter
    @Setter
    String calendarioNominaFecDescuentoPeriodoPosteriorPrestamo;
}

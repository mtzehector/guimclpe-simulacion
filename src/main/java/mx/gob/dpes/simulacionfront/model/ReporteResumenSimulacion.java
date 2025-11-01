package mx.gob.dpes.simulacionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;

/**
 *
 * @author antonio
 */
public class ReporteResumenSimulacion extends BaseModel {

  @Getter @Setter ResumenSimulacion resumenSimulacion = new ResumenSimulacion();  
  @Getter @Setter Long idSolicitud;
  @Getter @Setter Reporte<ResumenSimulacion> reporte = new Reporte<>();
}

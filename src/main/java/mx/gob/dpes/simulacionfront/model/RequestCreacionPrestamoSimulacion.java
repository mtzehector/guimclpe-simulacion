/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import java.util.List;

import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.bitacora.model.CronTarea;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author eduardo.loyo
 */

public class RequestCreacionPrestamoSimulacion extends BaseModel {

  @Getter @Setter Pensionado pensionado = new Pensionado();  
  @Getter @Setter Prestamo prestamo = new Prestamo();
  @Getter @Setter Solicitud solicitud = new Solicitud();
  @Getter @Setter Oferta oferta = new Oferta();
  @Getter @Setter ControlFolioModel consecutivo = new ControlFolioModel();
  @Getter @Setter PrestamoRecuperacion prestamoRecuperacion = new PrestamoRecuperacion();
  @Getter @Setter OfertaPrestamoRecuperacion ofertaDatos = new OfertaPrestamoRecuperacion();
  @Getter @Setter AmortizacionInsumos amortizacionInsumos; 
  @Getter @Setter List<PrestamoRecuperacion> lstPrestamoRecuperacion;
  @Getter @Setter CronTarea cronTareaResponse = new CronTarea();
  @Getter @Setter String cveEntidadFinanciera;
  @Getter @Setter String cveEntidadFinSIPRE;
  @Getter @Setter EntidadFinanciera entidadFinanciera;
  @Getter @Setter CapacidadCredito capacidadCredito;
  @Getter @Setter Boolean validacionTablaAmortizacionExitosa;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author osiris.hernandez
 */
public class ConsultaCartaInstruccionModel extends BaseModel{
  @Getter @Setter private String claveSolicitud;  
  @Getter @Setter private Solicitud solicitud;
  @Getter @Setter private PrestamoModel prestamo;  
  @Getter @Setter private Pensionado pensionado;
  @Getter @Setter private List<Documento> documentos;
  @Getter @Setter private CapacidadCredito capacidadCredito;
  @Getter @Setter private PersonaModel persona;
  @Getter @Setter private PrestamoRecuperacion prestamoRecuperacion;
}

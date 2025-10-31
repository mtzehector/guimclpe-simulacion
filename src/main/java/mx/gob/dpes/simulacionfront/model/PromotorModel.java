/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;

/**
 *
 * @author osiris.hernandez
 */
public class PromotorModel extends Pensionado{
    @Getter @Setter private Long id;
    @Getter @Setter private String numEmpleado;    
    @Getter @Setter private Long cveEntidadFinanciera;
    @Getter @Setter private EntidadFinanciera entidadFinanciera;
}

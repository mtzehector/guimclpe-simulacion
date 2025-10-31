/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
@Data
public class OfertaPrestamoRecuperacion extends BaseModel{
    
    private Long id;
    private String monto;
    private String cat;
    private String descuentoMensual;
    private String idnumPlazo;
    private String numPlazo;
    private String descripcionNumPlazo;
    private String importeTotal;
    private String tasaAnual;
    private Long cveEntidadFinanciera;
}

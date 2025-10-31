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
public class PrestamoRecuperacionModel extends BaseModel {
    
    private Long id;
    private Long cvePrestamo;
    private Long cvesolicitud;
    private String numSolicitudSipre;
    private Double canMontoSol;
    private Double canDescuentoMensual;
    private Double impRealPrestamo;
    private Double canCatPrestamo;
    private Integer numPlazoPrestamo;
    private Integer numMesRecuperado;
    private String numEntidadFinanciera;
}

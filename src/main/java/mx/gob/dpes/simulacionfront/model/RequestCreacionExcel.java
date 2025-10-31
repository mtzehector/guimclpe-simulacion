/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import mx.gob.imss.dpes.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
/**
 *
 * @author juanf.barragan
 */
public class RequestCreacionExcel extends BaseModel{
    
    @Getter
    @Setter
    private List<String> solicitudes;
    
    @Getter
    @Setter
    private String archivoXLSX;
    
    @Getter
    @Setter
    private List<ResumenSimulacionRequest> listaRequest;
    
}

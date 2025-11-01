/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosPorSolicitudRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosPorSolicitudResponse;

/**
 *
 * @author juan.garfias
 */
public class ConsultaDescuentos extends BaseModel{

    @Getter
    @Setter
    ConsultaDescuentosPorSolicitudRequest request;
    @Getter
    @Setter
    ConsultaDescuentosPorSolicitudResponse response;
}

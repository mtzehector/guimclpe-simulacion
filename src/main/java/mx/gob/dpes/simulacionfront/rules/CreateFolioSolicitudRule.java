/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.rules;

import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CreateFolioSolicitudRule extends BaseRule<RequestCreacionPrestamoSimulacion, Solicitud> {

    @Override
    public Solicitud apply(RequestCreacionPrestamoSimulacion input) {
        
        Solicitud solicitudModel = new Solicitud();
        solicitudModel.setConsecutivo(input.getConsecutivo().getNumConsecutivo());
        solicitudModel.setDelegacion(input.getPensionado().getDelegacion().getCveDelegacion());
        solicitudModel.setId(input.getSolicitud().getId());
        solicitudModel.setAltaRegistro( input.getSolicitud().getAltaRegistro() );
        
        return solicitudModel;
    }
    
}

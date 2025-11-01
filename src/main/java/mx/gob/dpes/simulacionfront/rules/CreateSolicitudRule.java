/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.rules;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author antonio
 */
@Provider
public class CreateSolicitudRule extends BaseRule<RequestCreacionPrestamoSimulacion, Solicitud> {

    final String PENSIONADO = "pensionado";

    @Override
    public Solicitud apply(RequestCreacionPrestamoSimulacion input) {
        
        // TODO: Inicializar la solicitud con las reglas, estados etc.
        input.setSolicitud(new Solicitud());
        input.getSolicitud().setRefTrabajador(PENSIONADO);
        input.getSolicitud().setOrigenSolictud(OrigenSolicitudEnum.SIMULACION);
        input.getSolicitud().setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR);
        input.getSolicitud().setCurp(input.getPensionado().getCurp());
        input.getSolicitud().setNss(input.getPensionado().getNss());
        input.getSolicitud().setDelegacion(input.getPensionado().getDelegacion().getCveDelegacion());
        input.getSolicitud().setSubDelegacion(input.getPensionado().getSubDelegacion());
        input.getSolicitud().setGrupoFamiliar(input.getPensionado().getGrupoFamiliar());
        if(input.getPensionado().getEntidadFederativa().getCveEntidadFederativa() != null && !input.getPensionado().getEntidadFederativa().getCveEntidadFederativa().equals("")){
           input.getSolicitud().setEntidadFederativa(Long.valueOf(input.getPensionado().getEntidadFederativa().getCveEntidadFederativa())); 
        }  
        input.getSolicitud().setCveEntidadFinanciera(input.getOfertaDatos().getCveEntidadFinanciera());
        if (input.getLstPrestamoRecuperacion() != null && !input.getLstPrestamoRecuperacion().isEmpty()) {
            for(PrestamoRecuperacion p : input.getLstPrestamoRecuperacion()){
                if(p.getMejorOferta() == 1){
                    if(input.getEntidadFinanciera()!=null && input.getEntidadFinanciera().getId() != null){    
                        input.getSolicitud().setCveEntidadFinMejorOferta(input.getEntidadFinanciera().getId().toString());
                    }
                }
            }
            
        }
        
    
        return input.getSolicitud();
    }

}

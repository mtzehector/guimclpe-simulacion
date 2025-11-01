/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.assembler;

import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;

/**
 *
 * @author edgar.arenas
 */
public class AmortizacionDatosBaseAssembler extends BaseAssembler<RequestCreacionPrestamoSimulacion, AmortizacionInsumos>{
    
    @Override
    public AmortizacionInsumos assemble(RequestCreacionPrestamoSimulacion source){
        
        AmortizacionInsumos insumos = new AmortizacionInsumos();
        insumos.setCat(source.getOferta().getCat());
        insumos.setCveSolicitud(source.getSolicitud().getId());
        insumos.setImporteMontoSolicitado(Double.parseDouble(source.getOfertaDatos().getMonto()));
        insumos.setImporteDescuentoMensual(Double.parseDouble(source.getOfertaDatos().getDescuentoMensual()));
        insumos.setImporteTotalPagar(Double.parseDouble(source.getOfertaDatos().getImporteTotal()));
        insumos.setCveTipoSimulacion(source.getPrestamo().getTipoSimulacion().toValue());
        insumos.setPlazo(source.getOferta().getPlazo().getNumPlazo());
        insumos.setHistorico(0);
         
        log.log(Level.INFO, ">>> AmortizacionDatosBaseAssembler return : {0}", insumos);
        return insumos;
    }
}

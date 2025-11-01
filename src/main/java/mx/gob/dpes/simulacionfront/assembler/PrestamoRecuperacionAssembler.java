/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.assembler;

import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.TipoCreditoEnum;
import mx.gob.imss.dpes.common.enums.TipoSimulacionEnum;

/**
 *
 * @author edgar.arenas
 */
public class PrestamoRecuperacionAssembler extends BaseAssembler<RequestCreacionPrestamoSimulacion, Prestamo> {

    @Override
    public Prestamo assemble(RequestCreacionPrestamoSimulacion source) {
        log.log(Level.INFO, ">>> PrestamoRecuperacionAssembler request : {0}", source);

        Prestamo prestamo = new Prestamo();

        prestamo.setIdOferta(source.getOfertaDatos().getId());
        prestamo.setImpDescNomina(Double.parseDouble(source.getOfertaDatos().getDescuentoMensual()));
        prestamo.setImpMontoSol(Double.parseDouble(source.getOfertaDatos().getMonto()));
        prestamo.setMonto(Double.parseDouble(source.getOfertaDatos().getMonto()));
        prestamo.setImpTotalPagar(Double.parseDouble(source.getOfertaDatos().getImporteTotal()));
        prestamo.setSolicitud(source.getSolicitud().getId());
        prestamo.setPrimerDescuento(source.getPrestamo().getPrimerDescuento());

        if (source.getPrestamo().getTipoSimulacion().toValue() == 1) {
            prestamo.setTipoSimulacion(TipoSimulacionEnum.MONTO);
        } else if (source.getPrestamo().getTipoSimulacion().toValue() == 2) {
            prestamo.setTipoSimulacion(TipoSimulacionEnum.DESCUENTO_MENSUAL);
        }

        switch (source.getPrestamo().getTipoCredito().toValue().intValue()) {
            case 1:
                prestamo.setTipoCredito(TipoCreditoEnum.NUEVO);
                break;
            case 2:
                prestamo.setTipoCredito(TipoCreditoEnum.RENOVACION);
                break;
            case 3:
                prestamo.setTipoCredito(TipoCreditoEnum.COMPRA_CARTERA);
                break;
            case 6:
                prestamo.setTipoCredito(TipoCreditoEnum.MIXTO);
                break;

        }
        return prestamo;
    }
}

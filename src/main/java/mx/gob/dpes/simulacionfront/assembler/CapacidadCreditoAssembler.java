package mx.gob.dpes.simulacionfront.assembler;

import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;

import java.util.logging.Level;

public class CapacidadCreditoAssembler extends BaseAssembler<RequestCreacionPrestamoSimulacion, CapacidadCredito> {
    @Override
    public CapacidadCredito assemble(RequestCreacionPrestamoSimulacion source) {
        log.log(Level.INFO, ">>> CapacidadCreditoAssembler request : {0}", source);

        CapacidadCredito sourceCapacidadCredito = source.getCapacidadCredito();

        CapacidadCredito capacidadCredito = new CapacidadCredito();
        capacidadCredito.setImpCapacidadFija(sourceCapacidadCredito.getImpCapacidadFija());
        capacidadCredito.setImpCapacidadTotal(sourceCapacidadCredito.getImpCapacidadTotal());
        capacidadCredito.setImpCapacidadVariable(sourceCapacidadCredito.getImpCapacidadVariable());
        capacidadCredito.setTipoCredito(source.getPrestamo().getTipoCredito());
        capacidadCredito.setCveSolicitud(source.getSolicitud().getId());

        return capacidadCredito;
    }
}

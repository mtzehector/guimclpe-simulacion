/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.rules;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import lombok.Setter;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.enums.TipoCreditoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.common.rule.MontoTotalRule;
import mx.gob.imss.dpes.common.rule.PagoMensualRule;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CreatePrestamoRule extends BaseRule<RequestCreacionPrestamoSimulacion, Prestamo> {

  @Inject
  @Setter
  PagoMensualRule pagoMensualRule;

  @Inject
  @Setter
  MontoTotalRule montoTotalRule;

  @Override
  public Prestamo apply(RequestCreacionPrestamoSimulacion input) throws
          BusinessException {

    log.log(Level.INFO, "Calculando montos {0}", input.getPrestamo());
    Prestamo prestamo = input.getPrestamo();
    prestamo.setId(null);
    prestamo.setSolicitud(input.getSolicitud().getId());
    prestamo.setNumPeriodoNomina(input.getPrestamo().getNumPeriodoNomina());
    prestamo.setPrimerDescuento(input.getPrestamo().getPrimerDescuento());
    prestamo.setTipoCredito(TipoCreditoEnum.NUEVO);

    switch (input.getPrestamo().getTipoSimulacion()) {
      case MONTO:
        calcularMonto(prestamo, input);
        break;
      case DESCUENTO_MENSUAL:
        calcularDescuentoMensual(prestamo, input);
        break;
    }
    log.log(Level.INFO, "Montos calculados {0}", input.getPrestamo());
    return prestamo;
  }

  private void calcularMonto(Prestamo prestamo,
          RequestCreacionPrestamoSimulacion input) {
    prestamo.setMonto(input.getPrestamo().getMonto());

    PagoMensualRule.Input inputRegla =
            pagoMensualRule.new Input(
                    input.getPrestamo().getMonto(),
                    input.getOferta().getPlazo().getNumPlazo(),
                    input.getOferta().getCat() / 100d);

    PagoMensualRule.Output outputRegla = pagoMensualRule.apply(inputRegla);

    prestamo.setImpDescNomina(outputRegla.getPagoMensual());
    prestamo.setImpTotalPagar(prestamo.getImpDescNomina() * input.getOferta().
            getPlazo().getNumPlazo());

  }

  private void calcularDescuentoMensual(Prestamo prestamo,
          RequestCreacionPrestamoSimulacion input) {
    prestamo.setImpDescNomina(input.getPrestamo().getMonto());

    MontoTotalRule.Input inputReglaMonto =
            montoTotalRule.new Input(
                    input.getPrestamo().getMonto(),
                    input.getOferta().getPlazo().getNumPlazo(),
                    input.getOferta().getCat() / 100d);

    MontoTotalRule.Output outputReglaMonto = montoTotalRule.apply(
            inputReglaMonto);

    prestamo.setMonto(outputReglaMonto.getMonto());
    prestamo.setImpTotalPagar(prestamo.getImpDescNomina() * input.getOferta().
            getPlazo().getNumPlazo());

  }

}

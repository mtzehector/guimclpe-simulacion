/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.rules;
import java.util.logging.Level;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.ControlFolioModel;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.rule.BaseRule;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CreateControlFolioRule extends BaseRule<RequestCreacionPrestamoSimulacion, ControlFolioModel> {

    @Override
    public ControlFolioModel apply(RequestCreacionPrestamoSimulacion input) {
        log.log(Level.WARNING, "input para regla {0}", input );
        ControlFolioModel controlFolioModel = new ControlFolioModel();
        DateFormat formatoAnio = new SimpleDateFormat("yyyy");
        String anio = formatoAnio.format(input.getSolicitud().getAltaRegistro());
        controlFolioModel.setNumAnio(anio);
        controlFolioModel.setDelegacion(input.getPensionado().getDelegacion().getCveDelegacion());
        log.log(Level.WARNING, "output para regla {0}", controlFolioModel );
        return controlFolioModel;
    }

}

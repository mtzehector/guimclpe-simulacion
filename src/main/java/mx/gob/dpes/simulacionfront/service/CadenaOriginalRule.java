/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.enums.SelloElectronicoEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoRequest;

import java.text.SimpleDateFormat;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CadenaOriginalRule extends BaseRule<CadenaOriginalRule.Input, CadenaOriginalRule.Output> {

  public static final String SEPARATOR = "|";
  public static final String TIPOCREDITO = "Nuevo";
  public static final String COLON = ":";

  @Override
  public Output apply(Input input) {
    StringBuilder sb = new StringBuilder();

    sb.append(SEPARATOR);
    sb.append(SEPARATOR);
    sb.append(SelloElectronicoEnum.NSS);
    sb.append(COLON);
    sb.append(input.reporteResumenSimulacion.getSolicitud().getNss());
    sb.append(SEPARATOR);
    sb.append(SelloElectronicoEnum.FOLIOPRESTAMO);
    sb.append(COLON);
    sb.append(input.reporteResumenSimulacion.getSolicitud().getNumFolioSolicitud());
    sb.append(SEPARATOR);
    sb.append(SelloElectronicoEnum.FECSOLICITUDPRESTAMO);
    sb.append(COLON);
    sb.append(new SimpleDateFormat("dd/MM/yyyy")
        .format(input.reporteResumenSimulacion.getSolicitud().getAltaRegistro()));
    sb.append(SEPARATOR);
    sb.append(SelloElectronicoEnum.TIPOCREDITO);
    sb.append(COLON);
    sb.append(input.reporteResumenSimulacion.getPrestamo().getTipoCredito());
    sb.append(SEPARATOR);
    sb.append(SEPARATOR);
      
      SelloElectronicoRequest request = new SelloElectronicoRequest();
      request.setCadenaOriginal(sb.toString());        
      return new Output(request);
    }

    

    public class Input extends BaseModel {

        public ReporteResumenSimulacionCompleto reporteResumenSimulacion;

        public Input(ReporteResumenSimulacionCompleto reporteResumenSimulacion) {
            this.reporteResumenSimulacion = reporteResumenSimulacion;
        }
    }

    public class Output extends BaseModel {

        public SelloElectronicoRequest request;

        public Output(SelloElectronicoRequest request) {
            this.request = request;
        }
    }

}

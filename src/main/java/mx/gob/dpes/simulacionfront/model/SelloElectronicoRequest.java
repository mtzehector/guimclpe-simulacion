package mx.gob.dpes.simulacionfront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

@Data
public class SelloElectronicoRequest extends BaseModel {

  private String nss;
  private String folioNegocio;
  private String fecCreacion;
  private String tipoCredito;
  private String rfc;
}

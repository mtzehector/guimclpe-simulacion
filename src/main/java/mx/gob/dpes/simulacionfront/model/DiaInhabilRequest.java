package mx.gob.dpes.simulacionfront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

import java.util.Date;

public class DiaInhabilRequest extends BaseModel {

    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date fecInicio;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date fecFin;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date fecVigencia;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date fecDescNomina;
    @Getter
    @Setter
    private Integer numPeriodoNomina;

    public DiaInhabilRequest() {
    }

    @Override
    public String toString() {
        return "DiaInhabilRequest{" + "fecInicio=" + fecInicio + ", fecFin=" + fecFin + ", fecVigencia=" + fecVigencia + ", fecDescNomina=" + fecDescNomina + ", numPeriodoNomina=" + numPeriodoNomina + '}';
    }
    
    
}

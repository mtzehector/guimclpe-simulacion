/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.bitacora.model.Bitacora;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionPorDescuento;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.sipre.model.DescuentoAplicado;

/**
 *
 * @author salvador.pocteco
 */
public class ResumenSimulacionRequest extends BaseModel {

    @Getter
    @Setter
    private Solicitud solicitud;
    @Getter
    @Setter
    private Persona persona;
    @Getter
    @Setter
    private Prestamo prestamo;
    @Getter
    @Setter
    private Oferta oferta;
    @Getter
    @Setter
    private Pensionado pensionado;
    @Getter
    @Setter
    private PersonaModel personaModel;
    @Getter
    @Setter
    private PrestamoRecuperacion prestamoRecuperacion;
    @Getter
    @Setter
    private List<Documento> documentos;
    @Getter
    @Setter
    private List<AmortizacionPorDescuento> tablaAmort;
    @Getter
    @Setter
    private List<DescuentoAplicado> descuentosAplicados;
    @Getter
    @Setter
    private PrestamoEnRecuperacionRs listPrestamoRecuperacion;
    @Getter
    @Setter
    private RequestPromotorModel promotor;
    @Getter
    @Setter
    private Double catPromotor;
    @Getter
    @Setter
    private List<Bitacora> bitacoras;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;

/**
 *
 * @author salvador.pocteco
 */
public class PrestamoModel extends Prestamo {

    @Getter @Setter Oferta oferta;
 
}

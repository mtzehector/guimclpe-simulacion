/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.interfaces.prestamo.model.CalendarioNomina;

/**
 *
 * @author juan.garfias
 */
public class FechasDocumentacionRS {

    @Getter
    @Setter
    DiaInhabilRequest periodoNominaDelPrestamo;

    @Getter
    @Setter
    CalendarioNomina periodoNominaPosteriorAlPrestamo;

    public FechasDocumentacionRS() {
    }

    public FechasDocumentacionRS(DiaInhabilRequest periodoNominaDelPrestamo, CalendarioNomina periodoNominaPosteriorAlPrestamo) {
        this.periodoNominaDelPrestamo = periodoNominaDelPrestamo;
        this.periodoNominaPosteriorAlPrestamo = periodoNominaPosteriorAlPrestamo;
    }

    @Override
    public String toString() {
        return "FechasDocumentacionRS{" + "periodoNominaDelPrestamo=" + periodoNominaDelPrestamo + ", periodoNominaPosteriorAlPrestamo=" + periodoNominaPosteriorAlPrestamo + '}';
    }
    
}

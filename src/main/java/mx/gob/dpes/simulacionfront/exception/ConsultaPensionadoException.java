/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author juanf.barragan
 */
public class ConsultaPensionadoException extends BusinessException{
    
    private final static String KEY = "err002";

    public ConsultaPensionadoException() {
        super(KEY);
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author osiris.hernandez
 */
public class CartaInstruccionException extends BusinessException {

  private static final String KEY ="msg007";

  public CartaInstruccionException() {
    super(KEY);
  }
}

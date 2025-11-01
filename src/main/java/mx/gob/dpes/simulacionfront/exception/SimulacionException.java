/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author antonio
 */
public class SimulacionException extends BusinessException{
  private final static String KEY = "err001";
  public final static String TEST ="request";  
  private final String field;

  public SimulacionException(String causa,String field) {
    super(causa);
    this.field = field;
  }
  
  public SimulacionException() {
    super(KEY);
    this.field = "";
  }
  
  @Override
   public Object[] getParameters() {    
     return  new Object[]{ this.field };
   }
  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.Template;

/**
 * @author antonio
 */
@Provider
public class ReadTemplateService extends ServiceDefinition<Template, Template> {
  

  @Override
  public Message<Template> execute(Message<Template> request) throws BusinessException {

    try {
      InputStream inputStream = getClass().getResourceAsStream(request.getPayload().getName());
      
      request.getPayload().setContent( readFromInputStream(inputStream) );
          
      return request;
    } catch (IOException ex) {
      log.log(Level.SEVERE, null, ex);
      throw new RecursoNoExistenteException();
    }
  }
  
  private String readFromInputStream(InputStream inputStream)
    throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br
      = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }
    }
   return resultStringBuilder.toString();
  }
  
}

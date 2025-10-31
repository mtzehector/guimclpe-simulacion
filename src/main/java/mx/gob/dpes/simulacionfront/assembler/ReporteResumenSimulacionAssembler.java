/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.assembler;

import java.io.StringWriter;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import mx.gob.dpes.simulacionfront.model.NotificacionCorreo;
import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CartaCapacidadCredito;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 *
 * @author Diego Velazquez
 */
@Provider
public class ReporteResumenSimulacionAssembler extends BaseAssembler<NotificacionCorreo<ResumenSimulacion>
        , NotificacionCorreo<ResumenSimulacion>, Long, Long> {

  @Override
  public NotificacionCorreo<ResumenSimulacion> toEntity(
          NotificacionCorreo<ResumenSimulacion> source) {
    return source;
  }

  @Override
  public Long toPKEntity(Long source) {
    return source;
  }

  @Override
  public NotificacionCorreo<ResumenSimulacion> assemble(
          NotificacionCorreo<ResumenSimulacion> source) {
    
    String template = source.getTemplate().getContent();
    ResumenSimulacion resumenSimulacion = source.getTipo();
    
    template = template.replaceAll("NOMBRE_ENTIDAD_FINANCIERA", resumenSimulacion.getNombreComercial());
    template = template.replaceAll("FOLIO_NEGOCIO", resumenSimulacion.getFolio());
    template = template.replaceAll("FECHA_VIGENCIA", resumenSimulacion.getFechaVigFolio());
    StringBuilder nombre = new StringBuilder();
    nombre.append(resumenSimulacion.getNombre());
    nombre.append(" ");
    nombre.append(resumenSimulacion.getPrimerApe());
    nombre.append(" ");
    nombre.append(resumenSimulacion.getSegundoApe());
    template = template.replaceAll("NOMBRE_PENSIONADO", nombre.toString() );
    template = template.replaceAll("NSS", resumenSimulacion.getNss());
    template = template.replaceAll("CURP", resumenSimulacion.getCurp() );
    if( resumenSimulacion.getEmail() != null ){
      template = template.replaceAll("CORREO_ELECTRONICO", resumenSimulacion.getEmail());
    }
    
    source.getCorreo().setCuerpoCorreo(template);
    
    String xml =  obtenerXML(resumenSimulacion);
    
    Adjunto adjunto = new Adjunto();
    adjunto.setNombreAdjunto("ReporteResumenSimulacion.xml");
    adjunto.setAdjuntoBase64( xml.getBytes() );
    source.getCorreo().getAdjuntos().add(adjunto);
    
    return source;
  }

  @Override
  public Long assemblePK(Long source) {
    return source;
  }
  
  private String obtenerXML(ResumenSimulacion resumenSimulacion){
    String xml = "";
    try {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResumenSimulacion.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(resumenSimulacion, sw);        
        xml = sw.toString() ;            
        
      } catch (JAXBException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    return xml;
  }

   
}

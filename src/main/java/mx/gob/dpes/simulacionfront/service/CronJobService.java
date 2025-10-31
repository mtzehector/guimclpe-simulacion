/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.FechaVigenciaException;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.dpes.simulacionfront.restclient.CronTareaClient;
import mx.gob.dpes.simulacionfront.restclient.EntidadFinancieraBackClient;
import mx.gob.dpes.simulacionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.enums.MaxHorasFechaVigenciaEnum;
import mx.gob.imss.dpes.common.enums.TipoCronTareaEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.CronTarea;
import mx.gob.imss.dpes.interfaces.bitacora.model.TareaAccion;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class CronJobService extends ServiceDefinition<RequestCreacionPrestamoSimulacion, RequestCreacionPrestamoSimulacion> {

    @Inject
    @RestClient
    private CronTareaClient cronTareaClient;
    @Inject
    @RestClient
    private SolicitudClient solicitudClient;
    
    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFinancieraClient;
    
    @Inject
    CorreoComponentService correoComponentService;
    
    @Inject
    private Config config;

    @Override
    public Message<RequestCreacionPrestamoSimulacion> execute(Message<RequestCreacionPrestamoSimulacion> request) throws BusinessException {
        log.log(Level.INFO, ">>>CronJobService request.getPayload()= {0}", request.getPayload());
                
        boolean isMejorOferta = false;
        for(PrestamoRecuperacion pr: request.getPayload().getLstPrestamoRecuperacion()){
            if(pr.getMejorOferta() == 1){
                isMejorOferta = true;
                break;
            }
        }
        
        int tipoCreditoId = request.getPayload().getPrestamo().getTipoCreditoId();
        Long cveSolicitud = request.getPayload().getSolicitud().getId();
        Solicitud solicitud = request.getPayload().getSolicitud();
        int maxHours = 0;
        CronTarea cronTareaResponse = null;
    /*
        public enum TipoCreditoEnum {
        NUEVO(1L,"Nuevo"),
        RENOVACION(2L,"Renovación"),
        COMPRA_CARTERA(3L,"Compra de cartera"),
        MIXTO(6L,"Mixto");    
    SIMULACION_NUEVO(1L, "Simulacion pensionado"),    1
    SIMULACION_RENOVACION(3L, "Simulacion Renovacion Pensionado"),  1
    SIMULACION_RENOVACIONES_MEJOR_OPCION(5L, "Simulacion Misma EF(Renovaciones) Pensionado Mejor Opcion"),  >1
    SIMULACION_COMPRA_CARTERA_MIXTO_PEOR_OPCION(6L, "Simulacion Compra de Cartera o Mixto Pensionado Peor Opcion"),
    SIMULACION_RENOVACIONES_PEOR_OPCION(7L, "Simulación Misma EF(Renovaciones) Pensionado Peor Opcion"),
    PENDIENTE_CARGAR_CEP(11L, "Pendiente cargar CEP");*/
        if (request.getPayload().getLstPrestamoRecuperacion() != null && !request.getPayload().getLstPrestamoRecuperacion().isEmpty()) {
            if (!isMejorOferta) {
                switch (tipoCreditoId) {
                    //TipoCreditoEnum.COMPRA_CARTERA
                    //TipoCreditoEnum.MIXTO
                    case 3: 
                    case 6: 
                        cronTareaResponse = fireCron(TipoCronTareaEnum.SIMULACION_COMPRA_CARTERA_MIXTO_PEOR_OPCION.getTipo(),cveSolicitud);
                        this.sendEmailCompraCarteraAdminEF(request.getPayload());
                        maxHours = MaxHorasFechaVigenciaEnum.MAX_PEOR_OPCION.toValue().intValue();
                        break;
                    //TipoCreditoEnum.RENOVACION
                    case 2:
                        cronTareaResponse = fireCron(TipoCronTareaEnum.SIMULACION_RENOVACION.getTipo(),cveSolicitud);
                        maxHours = MaxHorasFechaVigenciaEnum.MAX_MEJOR_OPCION.toValue().intValue();
                        break;
                    default: break;
                }
            } else {
                switch (tipoCreditoId) {
                    //TipoCreditoEnum.COMPRA_CARTERA
                    //TipoCreditoEnum.MIXTO
                    case 3: 
                    case 6: 
                        cronTareaResponse = fireCron(TipoCronTareaEnum.SIMULACION_COMPRA_CARTERA_MIXTO_MEJOR_OPCION.getTipo(),cveSolicitud);
                        this.sendEmailCompraCarteraAdminEF(request.getPayload());
                        maxHours = MaxHorasFechaVigenciaEnum.MAX_MEJOR_OPCION.toValue().intValue();
                        break;
                    //TipoCreditoEnum.RENOVACION
                    case 2:
                        cronTareaResponse = fireCron(TipoCronTareaEnum.SIMULACION_RENOVACION.getTipo(),cveSolicitud);
                        maxHours = MaxHorasFechaVigenciaEnum.MAX_MEJOR_OPCION.toValue().intValue();
                        break;
                    default: break;
                }
            }
        } else {
            cronTareaResponse = fireCron(TipoCronTareaEnum.SIMULACION_NUEVO.getTipo(),cveSolicitud);
            maxHours = MaxHorasFechaVigenciaEnum.MAX_MEJOR_OPCION.toValue().intValue();
        }
        solicitud.setMaxHoursFechaVigencia(maxHours);
        
        solicitud = this.updateFechaVigencia(solicitud);
        
        //request.getPayload().setSolicitud(solicitud);
        request.getPayload().getSolicitud().setFecVigenciaFolio(
                solicitud.getFecVigenciaFolio()
        );
        
        request.getPayload().setCronTareaResponse(cronTareaResponse);
        return new Message<>(request.getPayload());
    }

    public CronTarea fireCron(Long cveTareaAccion,Long cveSolicitud) {
        CronTarea cronTarea = new CronTarea();
        cronTarea.setCveSolicitud(cveSolicitud);
        TareaAccion tareaAccion = new TareaAccion();
        tareaAccion.setId(cveTareaAccion);
        cronTarea.setTareaAccion(tareaAccion);
        Response load = cronTareaClient.add(cronTarea);
        CronTarea cronTareaResponse = null;
        if (load.getStatus() == 200) {
            cronTareaResponse = load.readEntity(CronTarea.class);
            log.log(Level.INFO, "   >>><<<CronJobService.fireCron cveTareaAccion="+cveTareaAccion+"  cveSolicitud="+cveSolicitud+" cronTareaResponse= {0}", cronTareaResponse);

        }
        return cronTareaResponse;
    }
    
    protected void sendEmailCompraCarteraAdminEF(RequestCreacionPrestamoSimulacion request){
        if (!request.getLstPrestamoRecuperacion().isEmpty()) {
            int i = 0;
            for (PrestamoRecuperacion prestamoRec : request.getLstPrestamoRecuperacion()) {
                String numEntidadFinanciera = prestamoRec.getNumEntidadFinanciera();
                log.log(Level.INFO, "   >>><<<CronJobService.sendEmailCompraCarteraAdminEF ["+(i++)+"]prestamoRec=="+prestamoRec);
                log.log(Level.INFO, "   >>><<<CronJobService.sendEmailCompraCarteraAdminEF ["+(i++)+"]numEntidadFinanciera="+numEntidadFinanciera);
                if (numEntidadFinanciera != null && numEntidadFinanciera.length() > 0) {
                    String asunto = "Compra de cartera";
                    String nombreComercial = prestamoRec.getNombreComercial();
                    String folioSIPRE = prestamoRec.getNumSolicitudSipre();
                    String correoAdminEF = "";
                    Response response = entidadFinancieraClient.load(numEntidadFinanciera);
                    EntidadFinanciera  entidadFinanciera  = response.readEntity(EntidadFinanciera.class);
                    if(entidadFinanciera.getCveEntidadFinancieraSipre().equals("0")){
                       log.log(Level.INFO, "   >>>CORREO ADMIN EF PEOR OFERTA {0}", prestamoRec.getCorreoAdminEF());
                       correoAdminEF = prestamoRec.getCorreoAdminEF(); 
                    }else{
                       correoAdminEF = entidadFinanciera.getCorreoAdminEF();
                    }                    
                    ArrayList<String> correos = new ArrayList();
                    correos.add(correoAdminEF);
                    String plantilla = String.format(config.getValue("plantillaParaAdminEF-Aviso_Compra_cartera", String.class),
                    nombreComercial, folioSIPRE);
                    correoComponentService.sendEmail(plantilla, asunto, correos);
                }
                else{
                    log.log(Level.SEVERE, "ERROR. CronJobService.sendEmailCompraCarteraAdminEF (Prestamo SIN Entidad Financiera, NO sincronizado) prestamoRec="+prestamoRec);
                
                }
                
            }
                
        }
    }
    
    protected Solicitud updateFechaVigencia(Solicitud solicitud)throws BusinessException {
        log.log(Level.INFO, ">>>simulacionFront CronJobService.updateFechaVigencia solicitud= {0}", solicitud);
        Response load = solicitudClient.updateFechaVigencia(solicitud);
        if (load.getStatus() == 200) {
            Solicitud sol = load.readEntity(Solicitud.class);
            return sol;
        }
        else{
            throw new FechaVigenciaException();
        }
    }
        

}
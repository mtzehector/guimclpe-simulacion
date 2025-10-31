/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
//import mx.gob.dpes.simulacionfront.exception.CartaInstruccionException;
import mx.gob.dpes.simulacionfront.model.PrestamoEnRecuperacionRs;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.AmortizacionClient;
import mx.gob.dpes.simulacionfront.restclient.PrestamoRecuperacionClient;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
//import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
//import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultaPrestamosRecuperacionService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private PrestamoRecuperacionClient consultaPrestamoRecClient;

    //@Inject
    //private ConsultarNombreComercialSipre consultarNombreComercialSipre;
    
    @Inject
    private AmortizacionClient client; 

    // @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        //log.log(Level.INFO, "Servicio de consulta de prestamo recuperacion de carta de instruccin  ERPE Recuperacion" + request.getPayload());

//        PrestamoRecuperacion recuperacion = new PrestamoRecuperacion();
//        recuperacion.setPrestamo(request.getPayload().getPrestamo().getId());
//        recuperacion.setSolicitud(request.getPayload().getSolicitud().getId());
        try {
//            Response respuesta = consultaPrestamoRecClient.obtenerPrestamoRecuperacion(recuperacion);
//            if (respuesta.getStatus() == 200) {
//
//                request.getPayload().setPrestamoRecuperacion(respuesta.readEntity(PrestamoRecuperacion.class));
//                String a = request.getPayload().getPrestamoRecuperacion().getNumEntidadFinanciera().replace(" ", "");
//                log.log(Level.INFO, ">>>ID EF SIPRE STR: {0} " + a);
//
//                EntidadFinanciera e = new EntidadFinanciera();
//                e.setIdSipre(Long.parseLong(a));
//                log.log(Level.INFO, ">>>ID EF SIPRE {0}: " + e.getIdSipre());
//                Message<EntidadFinanciera> es = consultarNombreComercialSipre.execute(new Message<>(e));
//                request.getPayload().getPrestamoRecuperacion().setNombreComercial(es.getPayload().getNombreComercial());
//
//                AmortizacionInsumos saldoCapital = new AmortizacionInsumos();
//                saldoCapital.setFolioSipre(request.getPayload().getPrestamoRecuperacion().getNumSolicitudSipre());
//                saldoCapital.setNumMensualidad(request.getPayload().getPrestamoRecuperacion().getNumMesRecuperado());
//                Response sc = client.obtenerSaldoCapital(saldoCapital);
//                AmortizacionInsumos res = sc.readEntity(AmortizacionInsumos.class);
//                request.getPayload().getPrestamoRecuperacion().setSaldoCapital(res.getSaldoCapital());
//                log.log(Level.INFO, ">>>SALDO CAPITAL {0}: " + request.getPayload().getPrestamoRecuperacion().getSaldoCapital());
//
//            }
            Response respuesta = consultaPrestamoRecClient.consultaPrestamosPorSolicitud(
                request.getPayload().getSolicitud().getId());

            if (respuesta.getStatus() == Response.Status.OK.getStatusCode()) {
                PrestamoEnRecuperacionRs rs = respuesta.readEntity(PrestamoEnRecuperacionRs.class);
                for (PrestamoRecuperacion p : rs.getPrestamosEnRecuperacion()) {
                    //Obtener Nombre Comercial EF
//                    String a = p.getNumEntidadFinanciera().replace(" ", "");
//                    EntidadFinanciera e = new EntidadFinanciera();
//                    e.setIdSipre(a);
//                    log.log(Level.INFO, ">>>ID EF SIPRE {0}: " + e.getIdSipre());
//                    Message<EntidadFinanciera> es = consultarNombreComercialSipre.execute(new Message<>(e));
//                    p.setNombreComercial(es.getPayload().getNombreComercial());
                    //Obtener Saldo Capital
                    AmortizacionInsumos saldoCapital = new AmortizacionInsumos();
                    saldoCapital.setFolioSipre(p.getNumSolicitudSipre());
                    saldoCapital.setNumMensualidad(p.getNumMesRecuperado());
                    saldoCapital.setNumFolioSolicitud(p.getNumFolioSolicitud());
                    Response sc = client.obtenerSaldoCapital(saldoCapital);
                    AmortizacionInsumos res = sc.readEntity(AmortizacionInsumos.class);
                    p.setSaldoCapital(res.getSaldoCapital());
//                    log.log(Level.INFO, ">>>SALDO CAPITAL {0}: " + p);
                }
                request.getPayload().setListPrestamoRecuperacion(rs);
            }
            
        } catch (Exception e) {
            request.getPayload().setPrestamoRecuperacion(null);
            log.log(Level.WARNING, "ConsultaPrestamosRecuperacionService.execute = {0}", e);
        } finally {
            return request;
        }
    }
}

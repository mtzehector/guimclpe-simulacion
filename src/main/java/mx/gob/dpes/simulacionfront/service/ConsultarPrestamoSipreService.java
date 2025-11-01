/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.ConsultaDescuentos;
import mx.gob.dpes.simulacionfront.model.PersonaModel;
import mx.gob.dpes.simulacionfront.model.PrestamoEnRecuperacionRs;
import mx.gob.dpes.simulacionfront.model.RequestPromotorModel;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.ConsultaSistrapClient;
import mx.gob.dpes.simulacionfront.restclient.ConsultaTAClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionPorDescuento;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosPorSolicitudRequest;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarPrestamoSipreService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private ConsultaSistrapClient clienteConsultaSistrap;

    @Inject
    @RestClient
    private ConsultaTAClient client;

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request) throws BusinessException {

        log.log(Level.INFO, "El request de la Solicitud es : {0}", request.getPayload().getSolicitud());

        ConsultaDescuentosPorSolicitudRequest rq = new ConsultaDescuentosPorSolicitudRequest(
                request.getPayload().getSolicitud().getIdSolPrFinanciero(),
                request.getPayload().getSolicitud().getNumFolioSolicitud());
        //ConsultaDescuentosPorSolicitudRequest rq = new ConsultaDescuentosPorSolicitudRequest("001220425", "");
        Response loadDesc = clienteConsultaSistrap.load(rq);

        log.log(Level.INFO, "Los datos del prestamo : {0}", loadDesc.getStatus());
        log.log(Level.INFO, "Los datos del prestamo : {0}", loadDesc);
        if (loadDesc.getStatus() == 200) {
            log.log(Level.INFO, "Antes de mapear");
            ConsultaDescuentos cd = loadDesc.readEntity(ConsultaDescuentos.class);
            log.log(Level.INFO, "Despues de mapear");
            request.getPayload().setDescuentosAplicados(cd.getResponse().getDescuentosAplicados());
            PrestamoEnRecuperacionRs listPrestamoRecuperacion = new PrestamoEnRecuperacionRs();
            List<PrestamoRecuperacion> prestamosEnRecuperacion = new ArrayList<>();
            listPrestamoRecuperacion.setPrestamosEnRecuperacion(prestamosEnRecuperacion);
            request.getPayload().setListPrestamoRecuperacion(listPrestamoRecuperacion);
            log.log(Level.INFO, "Los datos del prestamo son : {0}", request.getPayload());
            Oferta oferta = new Oferta();
            oferta.setEntidadFinanciera(new EntidadFinanciera());
            request.getPayload().setOferta(oferta);

            if (request.getPayload().getSolicitud() == null) {
                request.getPayload().setSolicitud(new Solicitud());
            }

            request.getPayload().setPromotor(new RequestPromotorModel());

            request.getPayload().setPersonaModel(new PersonaModel());

            AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
            amortizacionInsumos.setFolioSipre(request.getPayload().getSolicitud().getIdSolPrFinanciero());

            Response load = client.loadByFolioSipre(amortizacionInsumos);
            if (load.getStatus() == 200) {
                request.getPayload().setTablaAmort(load.readEntity(List.class));
            } else {
                List<AmortizacionPorDescuento> listAmort = new ArrayList<>();
                request.getPayload().setTablaAmort(listAmort);
            }

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }

}

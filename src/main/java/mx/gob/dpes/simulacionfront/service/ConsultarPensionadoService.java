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
import mx.gob.dpes.simulacionfront.exception.ConsultaPensionadoException;

import mx.gob.dpes.simulacionfront.exception.ResumenSimulacionException;
import mx.gob.dpes.simulacionfront.model.PersonaModel;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.dpes.simulacionfront.restclient.ConsultaPensionadoClient;
import mx.gob.dpes.simulacionfront.restclient.PensionadoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.ErrorInfo;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarPensionadoService extends ServiceDefinition<ResumenSimulacionRequest, ResumenSimulacionRequest> {

    @Inject
    @RestClient
    private PensionadoClient pensionadoSistrapClient;

    @Inject
    @RestClient
    private ConsultaPensionadoClient pensionadoClient;


    private boolean esEstatusValido(Message<ResumenSimulacionRequest> request) {
        Long idSolicitud = null;

        if(
            request != null &&
            request.getPayload() != null &&
            request.getPayload().getSolicitud() != null &&
            request.getPayload().getSolicitud().getCveEstadoSolicitud() != null &&
            (idSolicitud = request.getPayload().getSolicitud().getCveEstadoSolicitud().getId()) != null &&
            (
                idSolicitud == 1L ||
                idSolicitud == 2L ||
                idSolicitud == 4L ||
                idSolicitud == 5L ||
                idSolicitud == 14L ||
                idSolicitud == 15L
            )
        )
            return true;

        return false;
    }

    @Override
    public Message<ResumenSimulacionRequest> execute(Message<ResumenSimulacionRequest> request)
        throws BusinessException {

        //log.log(Level.INFO, "Servicio de consulta de pensionado");
        try {
            Pensionado pensionadoRequest = new Pensionado();
            pensionadoRequest.setNss(request.getPayload().getSolicitud().getNss());
            pensionadoRequest.setGrupoFamiliar(request.getPayload().getSolicitud().getGrupoFamiliar());

            Response respuesta = pensionadoSistrapClient.consultaPensionado(pensionadoRequest);
            if (respuesta.getStatus() == Response.Status.OK.getStatusCode()) {
                Pensionado pensionado = respuesta.readEntity(Pensionado.class);
                request.getPayload().setPensionado(pensionado);
            }

            if (respuesta.getStatus() == Response.Status.PARTIAL_CONTENT.getStatusCode())
                throw new VariableMessageException((respuesta.readEntity(ErrorInfo.class)).getMessage());

        } catch(VariableMessageException e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            if (esEstatusValido(request))
                throw e;
        } catch(Exception e) {
            //log.log(Level.INFO, "Error en la consulta del pensionado" + e.getMessage());
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            if (esEstatusValido(request))
                return response(null, ServiceStatusEnum.EXCEPCION, new ConsultaPensionadoException(), null);
        }

        try {
            Response response = pensionadoClient.load(request.getPayload().getSolicitud().getCurp());
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                PersonaModel pens = response.readEntity(PersonaModel.class);
                if (request.getPayload().getPensionado() == null) {
                    request.getPayload().setPensionado(new Pensionado());
                }
                request.getPayload().getPensionado().setPrimerApellido(pens.getPrimerApellido());
                request.getPayload().getPensionado().setSegundoApellido(pens.getSegundoApellido());
                request.getPayload().getPensionado().setNombre(pens.getNombre());
                request.getPayload().getPensionado().setNss(request.getPayload().getSolicitud().getNss());
                request.getPayload().getPensionado().setCurp(request.getPayload().getSolicitud().getCurp());
                request.getPayload().getPensionado().setGrupoFamiliar(request.getPayload().getSolicitud().
                    getGrupoFamiliar());
                //request.getPayload().getPensionado().setCuentaClabe(request.getPayload().getPrestamo().getRefCuentaClabe());
                request.getPayload().setPersonaModel(pens);
                return request;
            }
        } catch(Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenSimulacionException(), null);
    }
}

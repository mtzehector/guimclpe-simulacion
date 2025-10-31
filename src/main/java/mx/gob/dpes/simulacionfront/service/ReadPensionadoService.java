/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.PensionadoSimulacionRequest;
import mx.gob.dpes.simulacionfront.model.PersonaModel;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.restclient.ConsultaPensionadoClient;
import mx.gob.dpes.simulacionfront.restclient.ConsultaPersonaClient;
import mx.gob.dpes.simulacionfront.restclient.PensionadoClient;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;

/**
 * @author salvador.pocteco
 */
@Provider
public class ReadPensionadoService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

    @Inject
    @RestClient
    private PensionadoClient pensionadoSistrapClient;
   
    @Inject
    @RestClient
    private ConsultaPensionadoClient pensionadoClient;
    

    @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {

        log.log(Level.INFO, "El request general es : {0}", request.getPayload());
        PensionadoSimulacionRequest pensionadoRequest = new PensionadoSimulacionRequest();
        pensionadoRequest.setNss(request.getPayload().getSolicitud().getNss());
        pensionadoRequest.setGrupoFamiliar(request.getPayload().getSolicitud().getGrupoFamiliar());
        Response respuesta = pensionadoSistrapClient.load(pensionadoRequest);

        if (respuesta.getStatus() == 200) {
            Pensionado pensionado = respuesta.readEntity(Pensionado.class);
            request.getPayload().getResumenSimulacion().setTipoPension(pensionado.getTipoPension());
            request.getPayload().getResumenSimulacion().setCiudad(pensionado.getEntidadFederativa().getDescEntidadFederativa());

            Response response = pensionadoClient.load(request.getPayload().getSolicitud().getCurp());
            if (response.getStatus() == 200) {
                PersonaModel perResponse = response.readEntity(PersonaModel.class);
                Persona persona = new Persona();
                persona.setNombre(perResponse.getNombre());
                persona.setPrimerApellido(perResponse.getPrimerApellido());
                persona.setSegundoApellido(perResponse.getSegundoApellido());

                persona.setCurp(request.getPayload().getSolicitud().getCurp());
                persona.setNss(request.getPayload().getSolicitud().getNss());

                //persona.setFechaNacimiento(bdturequest.getBdtuOut().getFechaNacimiento());
                //persona.setCveIdPersona(bdturequest.getBdtuOut().getCveIdPersona());
                //persona.setSexo(bdturequest.getBdtuOut().getSexo());
                persona.setCorreoElectronico(perResponse.getCorreoElectronico());
                persona.setTelefono(perResponse.getTelCelular());

                request.getPayload().setPersona(persona);
                log.log(Level.INFO, ">>>ReadPensionadoService response: {0}", request.getPayload().getPersona());
            }
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}

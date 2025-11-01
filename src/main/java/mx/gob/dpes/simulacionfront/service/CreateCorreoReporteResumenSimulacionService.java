/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service;

import java.util.ArrayList;
import java.util.List;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.NotificacionCorreo;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;
import mx.gob.dpes.simulacionfront.model.Template;
import mx.gob.dpes.simulacionfront.restclient.CorreoClient;
import mx.gob.dpes.simulacionfront.restclient.ReporteResumenSimulacionClient;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 * @author salvador.pocteco
 */
@Provider
public class CreateCorreoReporteResumenSimulacionService extends ServiceDefinition<ReporteResumenSimulacionCompleto, ReporteResumenSimulacionCompleto> {

    @Inject
    @RestClient
    private CorreoClient client;

    @Inject
    @RestClient
    private ReporteResumenSimulacionClient clientr;

    @Override
    public Message<ReporteResumenSimulacionCompleto> execute(Message<ReporteResumenSimulacionCompleto> request) throws BusinessException {
        NotificacionCorreo<ResumenSimulacion> notificacion = new NotificacionCorreo<>();
        notificacion.setTipo(request.getPayload().getResumenSimulacion());
        notificacion.setIdSolicitud(request.getPayload().getSolicitud().getId());
        log.log(Level.INFO, "El request general es ERPE: {0}", request.getPayload());
        ResumenSimulacion tipo = request.getPayload().getResumenSimulacion();
        Template template = new Template();

        template.setContent("<html>\n"
                + "\n"
                + "<head>\n"
                + "    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\n"
                + "    <title></title>\n"
                + "    <style type='text/css'>\n"
                + "        a:hover {\n"
                + "            color: #7b9cf0;\n"
                + "        }\n"
                + "\n"
                + "        .header span {\n"
                + "            color: #ffffff;\n"
                + "            font: normal 16px Helvetica, Arial;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 16px;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            padding: 0px;\n"
                + "        }\n"
                + "\n"
                + "        .content h4 {\n"
                + "            color: #5A5A5A;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 30px;\n"
                + "            font-size: 24px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .content p {\n"
                + "            color: #5A5A5A;\n"
                + "            font-weight: normal;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 23px;\n"
                + "            font-size: 16px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .content .note p {\n"
                + "            color: #5A5A5A;\n"
                + "            font-weight: normal;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            font-style: italic;\n"
                + "            line-height: 20px;\n"
                + "            font-size: 12px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .footer a {\n"
                + "            color: #12c;\n"
                + "        }\n"
                + "\n"
                + "        .footer p {\n"
                + "            padding: 0px;\n"
                + "            font-size: 13px;\n"
                + "            color: #5A5A5A;\n"
                + "            margin: 0px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "            text-transform: uppercase;\n"
                + "            color: #5A5A5A;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body style='margin: 0; padding: 0; background: #F3F3F3;'>\n"
                + "    <table cellpadding='0' cellspacing='0' border='0' align='center' width='100%'>\n"
                + "        <tr>\n"
                + "            <td align='center' style='margin: 0; padding: 0; background: #F3F3F3; padding: 27px 0px'>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; font-size: 16px; color: #ffffff; background: #621132'\n"
                + "                    class='header'>\n"
                + "                    <tr>\n"
                + "                        <td height='76px' width='50%' valign='middle' style='padding-left: 27px;'> <img width='126'\n"
                + "                                height='39' alt='gob.mx'\n"
                + "                                src='http://serviciosdigitales.imss.gob.mx/delta/resources/imagenes/gobmx/logos/gobmxlogo_g.png' />\n"
                + "                        </td>\n"
                + "                        <td height='76px' width='50%' valign='middle' align='right'><img width='246' height='81'\n"
                + "                                style='float: right; margin: 0px -20px 0px 0px;'\n"
                + "                                src='https://www.gob.mx/cms/uploads/identity/image/5131/logo_274x90_LOGO_IMSS_SOMBRA_BLANCO.png' />\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; background: #ffffff;' bgcolor='#ffffff'>\n"
                + "                    <tr>\n"
                + "                        <td width='560px;' valign='top' align='left' bgcolor='#ffffff'\n"
                + "                            style='font-family: Helvetica, Arial; font-size: 16px; color: #5A5A5A; background: #fff; padding: 38px 27px 76px;'>\n"
                + "                            <table cellpadding='0' cellspacing='0' border='0'\n"
                + "                                style='color: #717171; font: normal 16px Helvetica, Arial; margin: 0px; padding: 0;'\n"
                + "                                width='100%' class='content'>\n"
                + "                                <tr>\n"
                + "                                    <td>\n"
                + "                                        <p> Estimado(a) usuario(a): NOMBRE_PENSIONADO</p>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Se ha realizado con tu nombre <b>una nueva solicitud de préstamo con la\n"
                + "                                                Entidad Financiera NOMBRE_ENTIDAD_FINANCIERA </b>a través del <a\n"
                + "                                                href='https:///mclprestamos.imss.gob.mx/mclpe/auth/login'>Sistema de\n"
                + "                                                Préstamos a pensionados con Entidades Financieras </a>. </p> <br>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Si tú lo solicitaste, la Entidad Financiera se pondrá en contacto contigo.\n"
                + "                                        </p> <br>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Si tú no realizaste esta solicitud, comunícate por favor al 800 623 2323\n"
                + "                                            para aclarar la situación.</p> <br>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Recuerda que en el IMSS estamos atentos a tus necesidades. </p> <br>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Atentamente<br> Instituto Mexicano del Seguro Social<br> Préstamos a\n"
                + "                                            pensionados con Entidades Financieras</p>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td align='center'>\n"
                + "                                        <table class='nssDataWrapper'\n"
                + "                                            style='clear: both; font-size: 12px !important; margin-bottom: 6px !important; max-width: none !important;'>\n"
                + "                                        </table>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                            </table>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; font-size: 16px; color: #ffffff; background: #221e1e'\n"
                + "                    class='mainFooter'>\n"
                + "                    <tr>\n"
                + "                        <td align='left' height='76px' width='50%' valign='middle' style='padding-left: 27px;'><img\n"
                + "                                width='63' height='19' alt='gob.mx'\n"
                + "                                src='http://serviciosdigitales.imss.gob.mx/delta/resources/imagenes/gobmx/logos/gobmxlogo_g.png' />\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "</body>\n"
                + "\n"
                + "</html>");
        template.setName("/template/correo1.html");
        log.log(Level.INFO, "El request general es ERPE: 2");
        log.log(Level.INFO, "El request general es ERPE: 3");
        String template2 = template.getContent();
        ResumenSimulacion resumenSimulacion = tipo;

        template2 = template2.replaceAll("NOMBRE_ENTIDAD_FINANCIERA", resumenSimulacion.getNombreComercial());
        template2 = template2.replaceAll("FOLIO_NEGOCIO", resumenSimulacion.getFolio());
        template2 = template2.replaceAll("FECHA_VIGENCIA", resumenSimulacion.getFechaVigFolio());
        StringBuilder nombre = new StringBuilder();
        nombre.append(resumenSimulacion.getNombre());
        nombre.append(" ");
        nombre.append(resumenSimulacion.getPrimerApe());
        nombre.append(" ");
        nombre.append(resumenSimulacion.getSegundoApe());
        template2 = template2.replaceAll("NOMBRE_PENSIONADO", nombre.toString());
        template2 = template2.replaceAll("NSS", resumenSimulacion.getNss());
        template2 = template2.replaceAll("CURP", resumenSimulacion.getCurp());
        if (resumenSimulacion.getEmail() != null) {
            template2 = template2.replaceAll("CORREO_ELECTRONICO", resumenSimulacion.getEmail());
        }

        log.log(Level.INFO, "El request general es ERPE: 4");

        notificacion.getCorreo().getCorreoPara().add(
                request.getPayload().getResumenSimulacion().getEmail()
        );
        log.log(Level.INFO, "El request general es ERPE: 5");

        notificacion.getCorreo().setAsunto("Simulación de préstamo ID " + request.getPayload().getResumenSimulacion().getFolio());
        List<Adjunto> adjuntos = new ArrayList<>();
        Adjunto adjunto = new Adjunto();

        Response respuestar = clientr.load(request.getPayload().getSolicitud().getId());

        if (respuestar.getStatus() == 200) {
            byte[] archivo = respuestar.readEntity(byte[].class);
            adjunto.setNombreAdjunto("ReporteResumenSimulacion.pdf");
            adjunto.setAdjuntoBase64(archivo);

            log.log(Level.INFO, "El tamaño del adjunto es {0}", archivo.length);
        }
        adjuntos.add(adjunto);
        log.log(Level.INFO, "El request general es ERPE: 7");

        notificacion.getCorreo().setAdjuntos(adjuntos);
        notificacion.getCorreo().setCuerpoCorreo(template2);
        log.log(Level.INFO, "El request general es ERPE: 8");

        Response respuesta = client.create(notificacion.getCorreo());
        log.log(Level.INFO, "El request general es ERPE: 9");

        if (respuesta.getStatus() == 200) {

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}

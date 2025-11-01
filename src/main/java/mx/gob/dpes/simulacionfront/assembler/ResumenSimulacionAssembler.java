/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;

import java.text.SimpleDateFormat;
import java.util.Locale;
import mx.gob.dpes.simulacionfront.model.ReporteResumenSimulacionCompleto;

/**
 *
 * @author antonio
 */
@Provider
public class ResumenSimulacionAssembler
        extends BaseAssembler<ReporteResumenSimulacionCompleto, Documento, Long, Long> {

    public static final String PENSIONADO = "Pensionado";

    @Override
    public ReporteResumenSimulacionCompleto toEntity(Documento source) {
        return new ReporteResumenSimulacionCompleto();
    }

    @Override
    public Long toPKEntity(Long source) {
        return source;
    }

    @Override
    public Documento assemble(ReporteResumenSimulacionCompleto source) {

        ResumenSimulacion resumen = source.getResumenSimulacion();
        if (resumen.getCiudad() == null) {
            resumen.setCiudad("");
        }
        resumen.setFecha(new SimpleDateFormat("dd' de 'MMMM' de 'yyyy", Locale.forLanguageTag("es-ES"))
                .format(source.getSolicitud().getAltaRegistro()));
        resumen.setFolio(source.getSolicitud().getNumFolioSolicitud());
        resumen.setNombre(source.getPersona().getNombre());
        resumen.setPrimerApe(source.getPersona().getPrimerApellido());
        resumen.setSegundoApe(source.getPersona().getSegundoApellido());
        resumen.setCurp(source.getSolicitud().getCurp());
        resumen.setNss(source.getSolicitud().getNss());
        resumen.setDelegacion(source.getSolicitud().getDelegacion());
        resumen.setEmail(source.getPersona().getCorreoElectronico());
        resumen.setTelefono(source.getPersona().getTelefono());
        resumen.setTipoCredito(source.getPrestamo().getTipoCredito() != null ? source.getPrestamo().getTipoCredito().getDescripcion() : "");
        resumen.setTipoTrabajador(source.getSolicitud().getRefTrabajador().substring(0, 1).toUpperCase()
                + source.getSolicitud().getRefTrabajador().substring(1));
        resumen.setNombreComercial(source.getOferta().getEntidadFinanciera().getNombreComercial());
        resumen.setRazonSocial(source.getOferta().getEntidadFinanciera().getRazonSocial());
        resumen.setTelefonoBanco(source.getOferta().getEntidadFinanciera().getNumTelefono());
        resumen.setWebBanco(source.getOferta().getEntidadFinanciera().getPaginaWeb());
        resumen.setTasaAnual(source.getOferta().getTasaAnual().toString());
        resumen.setCat(source.getOferta().getCat().toString());
        resumen.setMontoSolicitado(source.getPrestamo().getMonto().toString());
        resumen.setImporteDescNomina(source.getPrestamo().getImpDescNomina().toString());
        resumen.setTotalDescAplicar(source.getOferta().getPlazo().getDescripcion());
        resumen.setPlazo(source.getOferta().getPlazo().getDescripcion());
        resumen.setTotalCredPagarInt(source.getPrestamo().getImpTotalPagar().toString());
        resumen.setNominaPrimerDesc(new SimpleDateFormat("dd/MM/yyyy")
                .format(source.getPrestamo().getPrimerDescuento()));
        resumen.setFechaVigFolio(new SimpleDateFormat("dd/MM/yyyy")
                .format(source.getSolicitud().getFecVigenciaFolio()));
        resumen.setCodigoQR(source.getSolicitud().getNumFolioSolicitud());
        resumen.setSelloDigital(source.getSello().getSello());
        resumen.setPeriodoNominaDelPrestamo( source.getCalendarioNominaFecFinCapturaPeriodoPrestamo() );
        resumen.setPeriodoNominaPosteriorAlPrestamo( source.getCalendarioNominaFecDescuentoPeriodoPosteriorPrestamo() );
        
        source.getSello().setCadenaOriginal(resumen.getCadenaOriginal());

        Documento documento = new Documento();
        documento.setCveSolicitud(source.getSolicitud().getId());
        documento.setTipoDocumento(TipoDocumentoEnum.RESUMEN_SIMULACION);
        documento.setRefSello(source.getSello().toString());
        documento.setRefDocumento(source.getResumenSimulacion().toString());

        return documento;
    }

    @Override
    public Long assemblePK(Long source) {
        return source;
    }
}

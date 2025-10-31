/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.assembler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.dpes.simulacionfront.model.RequestCreacionPrestamoSimulacion;
import mx.gob.imss.dpes.common.enums.SipreUtil;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;

/**
 *
 * @author antonio
 */
@Provider
public class RegistroPrestamoAssembler
        extends BaseAssembler<RequestCreacionPrestamoSimulacion, RegistroPrestamoRequest, Long, Long> {

    public static final String PENSIONADO = "Pensionado";

    @Override
    public RequestCreacionPrestamoSimulacion toEntity(RegistroPrestamoRequest source) {
        return new RequestCreacionPrestamoSimulacion();
    }

    @Override
    public Long toPKEntity(Long source) {
        return source;
    }

    @Override
    public RegistroPrestamoRequest assemble(RequestCreacionPrestamoSimulacion source) {
        RegistroPrestamoRequest registroPrestamo = new RegistroPrestamoRequest();
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source={0}", source);
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getSolicitud()={0}", source.getSolicitud());
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler TipoEstadoSolicitudEnum.INICIADO.getTipo()={0}", TipoEstadoSolicitudEnum.INICIADO.getTipo());
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getOferta().getEntidadFinanciera()={0}", source.getOferta().getEntidadFinanciera());
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getOferta().getEntidadFinanciera().getId()={0}", source.getOferta().getEntidadFinanciera().getId());
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getOferta().getEntidadFinanciera().getIdSipre()={0}", source.getOferta().getEntidadFinanciera().getIdSipre());
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getPrestamo()={0}", source.getPrestamo());
        String tipoMovimiento = null;
        if (source.getSolicitud().getEstadoSolicitud() != null) {
            tipoMovimiento = SipreUtil.getSipreStatusByTipoEstadoSolicitud(source.getSolicitud().getEstadoSolicitud().getTipo());
        } else {
            log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getSolicitud().getCveEstadoSolicitud().getId()={0}", source.getSolicitud().getCveEstadoSolicitud().getId());
            log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler SipreUtil.getSipreStatusByTipoEstadoSolicitud(source.getSolicitud().getCveEstadoSolicitud().getId())={0}", SipreUtil.getSipreStatusByTipoEstadoSolicitud(source.getSolicitud().getCveEstadoSolicitud().getId()));
            tipoMovimiento = SipreUtil.getSipreStatusByTipoEstadoSolicitud(source.getSolicitud().getCveEstadoSolicitud().getId());
        }
        registroPrestamo.setTipoMovimiento(tipoMovimiento);
        registroPrestamo.setNumFolio(source.getSolicitud().getNumFolioSolicitud());
        //TODO: Deal with lack of cveEntidadFinancieraSIPRE(syncro)
        String idSipre = source.getOferta().getEntidadFinanciera().getIdSipre();
        //registroPrestamo.setIdInstFinanciera(idSipre);
        registroPrestamo.setNss(source.getPensionado().getNss());
        registroPrestamo.setGrupoFamiliar(source.getPensionado().getGrupoFamiliar());
        registroPrestamo.setCurp(source.getPensionado().getCurp());
        registroPrestamo.setNombre(source.getPensionado().getNombre());
        registroPrestamo.setApellidoPaterno(source.getPensionado().getPrimerApellido());
        registroPrestamo.setApellidoMaterno(source.getPensionado().getSegundoApellido());
        registroPrestamo.setClabe(source.getPensionado().getCuentaClabe());
        registroPrestamo.setImpPrestamo(source.getPrestamo().getImpTotalPagar());
        registroPrestamo.setImpMensual(source.getPrestamo().getImpDescNomina());
        int numPlazo = 0;
        if (source.getOferta().getPlazo() != null) {
            numPlazo = source.getOferta().getPlazo().getNumPlazo();
        }
        if (numPlazo > 0) {
            registroPrestamo.setNumPlazo(Integer.toString(numPlazo));
        }

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(source.getPrestamo().getAltaRegistro());
        registroPrestamo.setFecAlta(date);

        registroPrestamo.setImpMensual(source.getOferta().getDescuentoMensual());
        String nominaPrimerDescuento = "";
        if (source.getPrestamo().getNumPeriodoNomina() != null) {
            nominaPrimerDescuento = Integer.toString(source.getPrestamo().getNumPeriodoNomina());
        }
        registroPrestamo.setNominaPrimerDescuento(nominaPrimerDescuento);
        registroPrestamo.setCatPrestamo(source.getOferta().getCat());
        registroPrestamo.setImpRealPrestamo(source.getPrestamo().getImpTotalPagar());

        return registroPrestamo;
    }

    @Override
    public Long assemblePK(Long source) {
        return source;
    }
}

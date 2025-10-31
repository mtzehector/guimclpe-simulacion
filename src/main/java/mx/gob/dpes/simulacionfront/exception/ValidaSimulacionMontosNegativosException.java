package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ValidaSimulacionMontosNegativosException extends BusinessException {

    public final static String ERROR_AL_INVOCAR_SERVICIO_PRESTAMO_TA = "err005";

    public ValidaSimulacionMontosNegativosException(String messageKey) {
        super(messageKey);
    }
}

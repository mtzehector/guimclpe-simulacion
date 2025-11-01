package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CambiaEstatusException extends BusinessException {

    public final static String ERROR_MONTOS_NEGATIVOS_O_DECUENTO_DIFERENTE = "msg033";
    public final static String ERROR_EN_SOLICITUD_CLIENT = "msg032";

    public CambiaEstatusException(String messageKey) {
        super(messageKey);
    }
}

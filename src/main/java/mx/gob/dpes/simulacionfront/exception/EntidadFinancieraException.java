package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class EntidadFinancieraException extends BusinessException {
    private final static String KEY = "err004";

    public EntidadFinancieraException() {
        super(KEY);
    }
}

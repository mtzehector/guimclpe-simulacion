package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class OfertaException extends BusinessException {
    private final static String KEY = "err003";

    public OfertaException() {
        super(KEY);
    }
}

package mx.gob.dpes.simulacionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class GeneraReporteXLSException extends BusinessException {
    private final static String KEY = "err006";
    public GeneraReporteXLSException() {
        super(KEY);
    }
}


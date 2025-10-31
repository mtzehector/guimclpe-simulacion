package mx.gob.dpes.simulacionfront.service;

import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.dpes.simulacionfront.model.RequestCreacionExcel;
import mx.gob.dpes.simulacionfront.service.reporte.util.GeneraExcelPrestamos;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

@Provider
public class GeneraReporteXlsService extends ServiceDefinition<RequestCreacionExcel, RequestCreacionExcel> {
    
    @Override
    public Message<RequestCreacionExcel> execute(
            Message<RequestCreacionExcel> response) throws BusinessException {

        try {
            log.log(Level.INFO, "GeneraReporteXlsService: 1 ");
            GeneraExcelPrestamos reporte = new GeneraExcelPrestamos();
            response.getPayload().setArchivoXLSX(reporte.generaXLS(response.getPayload().getListaRequest()));
        } catch (IOException ex) {
            log.log(Level.INFO, "Reporte GeneraReporteXlsService: {0}", ex.getMessage());
        }
        return response;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.dpes.simulacionfront.service.reporte.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import mx.gob.dpes.simulacionfront.model.RequestCreacionExcel;
import mx.gob.dpes.simulacionfront.model.ResumenSimulacionRequest;
import mx.gob.imss.dpes.common.service.BaseService;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author alexis.corrales
 */
public class GeneraExcelPrestamos extends BaseService{
    
    Workbook workbook = new XSSFWorkbook();
    Font font_10;
    CreationHelper createHelper = workbook.getCreationHelper();
    CellStyle styleCurrency = workbook.createCellStyle();
    CellStyle styleMiles = workbook.createCellStyle();
    CellStyle styleString = workbook.createCellStyle();
    CellStyle style = workbook.createCellStyle();
    Font font_11 = workbook.createFont();
    Font font_12 = workbook.createFont();
    Font font_14 = workbook.createFont();
    Sheet pagina;
    Integer filaReporte = 0;
    Row fila = null;
    
    public GeneraExcelPrestamos() {

        font_10 = workbook.createFont();
        font_10.setFontHeightInPoints((short) 10);
        font_10.setFontName("Montserrat Regular");
        font_10.setItalic(false);
        font_10.setStrikeout(false);

        styleCurrency.setDataFormat(createHelper.createDataFormat().getFormat("$#,##0.00"));
        styleCurrency.setFont(font_10);

        styleMiles.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        styleMiles.setFont(font_10);

        styleString.setFont(font_10);

        styleString.setBorderBottom(BorderStyle.THIN);
        styleString.setBorderTop(BorderStyle.THIN);

        styleMiles.setBorderBottom(BorderStyle.THIN);
        styleMiles.setBorderTop(BorderStyle.THIN);

        styleCurrency.setBorderBottom(BorderStyle.THIN);
        styleCurrency.setBorderTop(BorderStyle.THIN);

        pagina = workbook.createSheet("Busqueda de folios");
        font_11.setFontHeightInPoints((short) 11);
        font_11.setFontName("Montserrat Regular");
        font_11.setItalic(false);
        font_11.setStrikeout(false);

        font_12.setFontHeightInPoints((short) 12);
        font_12.setFontName("Montserrat Regular");
        font_12.setItalic(false);
        font_12.setStrikeout(false);

        font_14.setFontHeightInPoints((short) 14);
        font_14.setFontName("Montserrat Regular");
        font_14.setItalic(false);
        font_14.setStrikeout(false);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setFont(font_11);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }
    
    public String generaXLS(
            List<ResumenSimulacionRequest> lista) throws FileNotFoundException, IOException {
        log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS");

        String stringXLS = "";

        Date date = new Date();
        //Caso 2: obtener la fecha y salida por pantalla con formato:
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println("Fecha: " + dateFormat.format(date));

        // Creamos el archivo donde almacenaremos la hoja
        // de calculo, recuerde usar la extension correcta,
        // en este caso .xlsx
        log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 2: {0}", lista.size());

        //File archivo = new File("C:\\opt\\work\\reporte_test.xlsx");
        // Creamos el libro de trabajo de Excel formato OOXML
        // Applying font to the style  
        //style.setFont(font);  
        // La hoja donde pondremos los datos
        // Creamos el estilo paga las celdas del encabezado
        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
        CellStyle styleH0 = workbook.createCellStyle();
        styleH0.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH0.setAlignment(HorizontalAlignment.RIGHT);
        styleH0.setWrapText(true);
        styleH0.setFont(font_10);

        CellStyle styleH1 = workbook.createCellStyle();
        styleH1.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH1.setAlignment(HorizontalAlignment.CENTER);
        styleH1.setWrapText(true);
        styleH1.setFont(font_14);

        CellStyle styleH2 = workbook.createCellStyle();
        styleH2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH2.setAlignment(HorizontalAlignment.CENTER);
        styleH2.setWrapText(true);
        styleH2.setFont(font_12);

        // Header de reporte para Dirección de Prestaciones Económicas y Sociales, etc
        pagina.addMergedRegion(CellRangeAddress.valueOf("A1:P1"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A3:P3"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("M4:P4"));
//        pagina.addMergedRegion(CellRangeAddress.valueOf("G4:J4"));

        // Creamos una fila en la hoja en la posicion filaReporte
        Row filaH0 = pagina.createRow(filaReporte);

        Cell celdaH0 = filaH0.createCell(0);

        // Indicamos el estilo que deseamos 
        // usar en la celda, en este caso el unico 
        // que hemos creado
        celdaH0.setCellStyle(styleH0);
        celdaH0.setCellValue("Dirección de Prestaciones Económicas y Sociales\n"
                + "Coordinación de Prestaciones Económicas\n"
                + "División de Pensiones\n");

        Integer w0 = 1600;
        filaH0.setHeight(w0.shortValue());
        filaReporte++;
        Row filaH1 = pagina.createRow(filaReporte);
        filaReporte++;
        Row filaH2 = pagina.createRow(filaReporte);
        Integer w3 = 750;
        filaH2.setHeight(w3.shortValue());

        Cell celdaH2_1 = filaH2.createCell(0);
        celdaH2_1.setCellStyle(styleH1);

        celdaH2_1.setCellValue("Reporte Busqueda folios Prestamos");

        filaReporte++;
        Row filaH3 = pagina.createRow(filaReporte);
        Integer w2 = 750;
        filaH3.setHeight(w2.shortValue());

//        Cell celdaH3_1 = filaH3.createCell(0);
//        celdaH3_1.setCellStyle(styleH2);
////        celdaH3_1.setCellValue("Periodo de : " + dateFormat.format(rc.getFechaDesde())
////                + " hasta: " + dateFormat.format(rc.getFechaHasta()));

        Cell celdaH3_2 = filaH3.createCell(12);
        celdaH3_2.setCellStyle(styleH2);
        celdaH3_2.setCellValue("Fecha de emisión: " + dateFormat.format(date));

        filaReporte++;
        fila = pagina.createRow(filaReporte);

        filaReporte++;
        Row fila2 = pagina.createRow(filaReporte);
        String[] titulos2 = getTitulos();

        // Creamos el encabezado
        for (int i = 0; i < titulos2.length; i++) {
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila2.createCell(i);

            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(style);
            celda.setCellValue(titulos2[i]);
        }
        Integer w = 4600;
        for (int i = 1; i < titulos2.length; i++) {
            pagina.setColumnWidth(i, w);
        }

        filaReporte++;

        // revisar que objeto se pasara
        fillReport(lista);

        // Ahora guardaremos el archivo
        try {
            // Creamos el flujo de salida de datos,
            // apuntando al archivo donde queremos 
            // almacenar el libro de Excel
            log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 4 ");

            //FileOutputStream salida = new FileOutputStream(archivo);
            log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 5 ");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 6 ");

            // Almacenamos el libro de 
            // Excel via ese 
            // flujo de datos
            workbook.write(out);
            log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 7 ");

            stringXLS = Base64.encodeBase64String(out.toByteArray());

            // Cerramos el libro para concluir operaciones
            workbook.close();

        } catch (FileNotFoundException ex) {
            log.log(Level.INFO, "File Not Found generaXLS: {0}", ex);
        } catch (IOException ex) {
            log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS 6: {0}", ex);
        }
        log.log(Level.INFO, "GeneraReporteExcelPrestamos generaXLS stringXLS: ");

        return stringXLS;
    }
    
    private String[] getTitulos() {
        String[] titulos = null;
        titulos = new String[]{
                    "Folio",
                    "NSS",
                    "CURP",
                    "Nombre del pensionado",
                    "Delegación",
                    "Fecha de inicio del folio",
                    "Tipo de crédito",
                    "Entidad Financiera",
                    "Nombre del personal operativo",
                    "CAT con IVA",
                    "Monto solicitado",
                    "Descuento mensual",
                    "Plazo",
                    "Importe total a pagar",
                    "Fecha de vigencia del folio",
                    "Estado"
                };      
        return titulos;
    }
    
    private void fillReport(List<ResumenSimulacionRequest> lista) {
        Integer contador = 1;
        for (ResumenSimulacionRequest c : lista) {
            // Ahora creamos una fila en la posicion 1
            fila = pagina.createRow(filaReporte);

            Integer col = 0;
            Cell celda1 = fila.createCell(col, CellType.STRING);
            celda1.setCellValue(
                    (c.getSolicitud().getNumFolioSolicitud() == null ) ? "" : c.getSolicitud().getNumFolioSolicitud());
            celda1.setCellStyle(styleString);

            col++;
            Cell celda2 = fila.createCell(col, CellType.STRING);
            celda2.setCellValue((c.getSolicitud().getNss() == null ) ? "" : c.getSolicitud().getNss());
            celda2.setCellStyle(styleString);

            col++;
            Cell celda3 = fila.createCell(col, CellType.STRING);
            celda3.setCellValue((c.getSolicitud().getCurp() == null ) ? "" : c.getSolicitud().getCurp());
            celda3.setCellStyle(styleString);

            col++;
            Cell celda4 = fila.createCell(col, CellType.STRING);
            celda4.setCellValue((c.getPensionado().getNombre() == null) ? "": c.getPensionado().getNombre()+ " " 
                    + ((c.getPensionado().getPrimerApellido() == null) ? "": c.getPensionado().getPrimerApellido() + " ")
                    + ((c.getPensionado().getPrimerApellido() == null) ? "": c.getPensionado().getSegundoApellido()));
            celda4.setCellStyle(styleMiles);

            col++;
            Cell celdax = fila.createCell(col, CellType.STRING);
            celdax.setCellValue((c.getPensionado().getDelegacion()) == null ? "" : c.getPensionado().getDelegacion().getDescDelegacion());
            celdax.setCellStyle(styleString);
            
            col++;
            Cell celda5 = fila.createCell(col, CellType.STRING);
            SimpleDateFormat sdf = new SimpleDateFormat();
            celda5.setCellValue((c.getSolicitud().getAltaRegistro() == null) 
                    ? "" : sdf.format(c.getSolicitud().getAltaRegistro()));
            celda5.setCellStyle(styleString);

            col++;
            Cell celda6 = fila.createCell(col, CellType.STRING);
            celda6.setCellValue((c.getPrestamo().getTipoCredito().getDescripcion()==null) 
                    ? "" : c.getPrestamo().getTipoCredito().getDescripcion());
            celda6.setCellStyle(styleString);

            col++;
            Cell celda7 = fila.createCell(col, CellType.STRING);
            celda7.setCellValue((c.getSolicitud().getEntidadFinanciera() == null) 
                    ? "" : c.getSolicitud().getEntidadFinanciera().getNombreComercial());
            celda7.setCellStyle(styleString);

            col++;
            Cell celda8 = fila.createCell(col, CellType.STRING);
            celda8.setCellValue((c.getPromotor().getNombre() == null) ? "" : c.getPromotor().getNombre()+ " "
                + ((c.getPromotor().getPrimerApellido() == null) ? "" : c.getPromotor().getPrimerApellido()) + " "
                + ((c.getPromotor().getSegundoApellido() == null) ? "" : c.getPromotor().getSegundoApellido()));
            celda8.setCellStyle(styleString);
            
            col++;
            Cell celda9 = fila.createCell(col, CellType.STRING);
            celda9.setCellValue(c.getCatPromotor() == null ? (c.getOferta().getCat() == null ? ""  : c.getOferta().getCat().toString()) : c.getCatPromotor().toString()); // CAT IVA
            celda9.setCellStyle(styleString);
            
            col++;
            Cell celda10 = fila.createCell(col, CellType.STRING);
            celda10.setCellValue((c.getPrestamo().getImpMontoSol()== null) 
                    ? "0.0" : c.getPrestamo().getMonto().toString()); // MONTO SOLICITADO
            celda10.setCellStyle(styleMiles);
            
            col++;
            Cell celda11 = fila.createCell(col, CellType.STRING);
            celda11.setCellValue((c.getPrestamo().getImpDescNomina() == null) 
                    ? "0.0" : c.getPrestamo().getImpDescNomina().toString()); // DESCUENTO MENSUAL
            celda11.setCellStyle(styleMiles);
            
            col++;
            Cell celda12 = fila.createCell(col, CellType.STRING);
            celda12.setCellValue((c.getOferta().getPlazo() .getDescripcion() == null) ? "" :
                    c.getOferta().getPlazo() .getDescripcion()); // PLAZO
            celda12.setCellStyle(styleMiles);
            
            col++;
            Cell celda13 = fila.createCell(col, CellType.STRING);
            celda13.setCellValue((c.getPrestamo().getImpTotalPagar() == null) 
                    ? 0.0 : c.getPrestamo().getImpTotalPagar()); // imp total a pagar
            celda13.setCellStyle(styleString);
            
            col++;
            Cell celda14 = fila.createCell(col, CellType.STRING);
            celda14.setCellValue((c.getSolicitud().getFecVigenciaFolio() == null) ? "" :
                    sdf.format(c.getSolicitud().getFecVigenciaFolio())); // fecha vigencia folio
            celda14.setCellStyle(styleString);
            
            col++;
            Cell celda15 = fila.createCell(col, CellType.STRING);
            celda15.setCellValue((c.getSolicitud().getCveEstadoSolicitud().getDesEstadoSolicitud() == null) 
                    ? "" : c.getSolicitud().getCveEstadoSolicitud().getDesEstadoSolicitud()); 
            celda15.setCellStyle(styleString);

            filaReporte++;
        }
    }
    
}

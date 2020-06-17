package cn.airesearch.aimarkserver.support.ocr;

import cn.airesearch.aimarkserver.support.ocr.ai.BaseRectWords;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.InvoiceModel;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.Item;
import cn.airesearch.aimarkserver.support.ocr.ai.waybill.WayBillModel;
import cn.airesearch.aimarkserver.tool.JsonUtils;
import cn.asr.appframework.utility.file.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OCRExporter {
    private static ArrayList<String> wayBillHeaderList;
    private static ArrayList<String> invoiceHeaderList;

    static {
        initWayBillHeaderList();
        initInvoiceHeaderList();
    }

    public static boolean export2Json(String exportFilePath, Object obj) {

        FileUtils.checkPath(exportFilePath);
        //输出excel
        FileOutputStream outputStream = null;
        try {
            File jsonFile = new File(exportFilePath); // 创建文件对象


            outputStream = new FileOutputStream(jsonFile); // 文件流
            String json = JsonUtils.toJsonStringPrettyFormat(obj);
            outputStream.write(json.getBytes("utf-8"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean export2Excel(String exportFilePath, OCRResponse ocrResponse) {
        FileUtils.checkPath(exportFilePath);
        Workbook workbook = createWorkBook();

        workbook.createSheet("运单");
        workbook.createSheet("invoice");

        //运单处理
        HandleWayBillList(ocrResponse.waybill, workbook, wayBillHeaderList);

//invoice
        HandleInvoiceList(ocrResponse.invoice, workbook, invoiceHeaderList);


        //输出excel
        FileOutputStream outputStream = null;
        try {
            File excelFile = new File(exportFilePath); // 创建文件对象

            if (excelFile.exists()) {
                excelFile.delete();
            }
            outputStream = new FileOutputStream(excelFile); // 文件流

            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private static void HandleWayBillList(List<WayBillModel> wayBillList, Workbook workbook, List<String> wayBillHeaderList) {
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = wayBillList.size() + 1;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Row newRow = sheet.createRow(rowIndex);
            for (int cellIndex = 0; cellIndex < wayBillHeaderList.size(); cellIndex++) {
                newRow.createCell(cellIndex);
            }
        }
        //header
        Row headerRow = sheet.getRow(0);
        CellStyle headerCellStyle = createHeadCellStyle(workbook);
        for (int cellIndex = 0; cellIndex < wayBillHeaderList.size(); cellIndex++) {
            headerRow.getCell(cellIndex).setCellStyle(headerCellStyle);
            headerRow.getCell(cellIndex).setCellValue(wayBillHeaderList.get(cellIndex));
        }

        //data
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);

            WayBillModel wayBillModel = wayBillList.get(rowIndex - 1);

            for (int cellIndex = 0; cellIndex < wayBillHeaderList.size(); cellIndex++) {
                String cellValue = getPropertyValue(wayBillHeaderList.get(cellIndex), wayBillModel);
                dataRow.getCell(cellIndex).setCellValue(cellValue);
            }
        }
    }

    private static void HandleInvoiceList(List<InvoiceModel> invoiceList, Workbook workbook, List<String> invoiceHeaderList) {
        int cellSize = invoiceHeaderList.size();
        Sheet sheet = workbook.getSheetAt(1);

        //header
        int currentRowIndex = 0;
        createRowAndCell(sheet, currentRowIndex, cellSize, null);
        Row headerRow = sheet.getRow(currentRowIndex);
        CellStyle headerCellStyle = createHeadCellStyle(workbook);
        for (int cellIndex = 0; cellIndex < invoiceHeaderList.size(); cellIndex++) {
            headerRow.getCell(cellIndex).setCellStyle(headerCellStyle);
            headerRow.getCell(cellIndex).setCellValue(invoiceHeaderList.get(cellIndex));
        }

        //data
        currentRowIndex++;
        int invoiceIndex = 0;
        for (InvoiceModel d :
                invoiceList) {
            CellStyle dataRowCellStyle = createDateRowCellStyle(workbook, invoiceIndex++);
            for (Item item :
                    d.Items) {

                createRowAndCell(sheet, currentRowIndex, cellSize, dataRowCellStyle);
                Row dataRow = sheet.getRow(currentRowIndex);

                for (int cellIndex = 0; cellIndex < cellSize; cellIndex++) {
                    String propertyName = invoiceHeaderList.get(cellIndex);
                    Object valueObj = d;
                    if (propertyName.startsWith("Items_")) {
                        String newPropertyName = propertyName.replace("Items_", "");
                        valueObj = item;

                        if (propertyName.equals("Items_Net_weight") || propertyName.equals("Items_Gross_weight")) {
                            String cellValue = getPropertyValue(newPropertyName, valueObj);
                            cellValue = getWeightValue(cellValue);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        } else if (propertyName.equals("Items_Net_weight_unit") || propertyName.equals("Items_Gross_weight_unit")) {
                            newPropertyName = propertyName.replace("Items_", "").replace("_unit", "");
                            String cellValue = getPropertyValue(newPropertyName, valueObj);
                            cellValue = getWeightUnit(cellValue);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        } else {

                            String cellValue = getPropertyValue(newPropertyName, valueObj);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        }

                    } else {

                        if (propertyName.equals("Net_weight") || propertyName.equals("Gross_weight")) {
                            String cellValue = getPropertyValue(propertyName, valueObj);
                            cellValue = getWeightValue(cellValue);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        } else if (propertyName.equals("Net_weight_unit") || propertyName.equals("Gross_weight_unit")) {
                            String newPropertyName = propertyName.replace("_unit", "");
                            String cellValue = getPropertyValue(newPropertyName, valueObj);
                            cellValue = getWeightUnit(cellValue);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        } else {
                            String cellValue = getPropertyValue(propertyName, valueObj);
                            dataRow.getCell(cellIndex).setCellValue(cellValue);
                        }

                    }

                }
                currentRowIndex++;
            }
        }

    }

    private static String getWeightValue(String cellValue) {
        if (StringUtils.isEmpty(cellValue)) {
            return "";
        }
        return cellValue.replace("KG", "").replace("kg", "");
    }

    private static String getWeightUnit(String cellValue) {
        if (StringUtils.isEmpty(cellValue)) {
            return "";
        }
        if (cellValue.toUpperCase().endsWith("KG")) {
            return "KG";
        }
        return "";
    }

    private static void createRowAndCell(Sheet sheet, int currentRowIndex, int cellSize, CellStyle cellStyle) {
        Row newRow = sheet.createRow(currentRowIndex);

        for (int cellIndex = 0; cellIndex < cellSize; cellIndex++) {
            Cell cell = newRow.createCell(cellIndex);
            if (cellStyle != null) {
                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * 创建头部样式
     *
     * @param wb
     * @return
     */
    private static CellStyle createHeadCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * 创建数据行样式
     *
     * @param wb
     * @return
     */
    private static CellStyle createDateRowCellStyle(Workbook wb, int rowIndex) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (rowIndex % 2 == 0) {
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        } else {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static void initWayBillHeaderList() {
        wayBillHeaderList = new ArrayList<>();
        wayBillHeaderList.add("pageNum");
        wayBillHeaderList.add("HBL_HABW");
        wayBillHeaderList.add("MBL_MAWB");
        wayBillHeaderList.add("Incoterms");
        wayBillHeaderList.add("Origin_Country");
        wayBillHeaderList.add("Transportation_Method");
        wayBillHeaderList.add("Gross_Weight");
        wayBillHeaderList.add("Volume");
        wayBillHeaderList.add("Chargeable_Weight");
        wayBillHeaderList.add("Pieces");
        wayBillHeaderList.add("Packaging_Type");
        wayBillHeaderList.add("Freight");
        wayBillHeaderList.add("Freight_Currency");
        wayBillHeaderList.add("Location_To");
        wayBillHeaderList.add("District_Code");
        wayBillHeaderList.add("Destination_District");
        wayBillHeaderList.add("Port_of_Destination");
        wayBillHeaderList.add("H");
        wayBillHeaderList.add("W");
        wayBillHeaderList.add("L");
        wayBillHeaderList.add("CUBIC_CONTENT");
        wayBillHeaderList.add("Unit");
    }

    private static void initInvoiceHeaderList() {
        invoiceHeaderList = new ArrayList<>();
        invoiceHeaderList.add("pageNum");
        invoiceHeaderList.add("Invoice_No");
        invoiceHeaderList.add("supplier_code");

        invoiceHeaderList.add("Items_Customer_Partnumber");
        invoiceHeaderList.add("Items_Quantity");
        invoiceHeaderList.add("Items_price");
        invoiceHeaderList.add("Items_Amount");
        invoiceHeaderList.add("Items_Currency");
        invoiceHeaderList.add("Items_Net_weight");
        invoiceHeaderList.add("Items_Net_weight_unit");
        invoiceHeaderList.add("Items_Ctry_origin");
        invoiceHeaderList.add("Items_Gross_weight");
        invoiceHeaderList.add("Items_Gross_weight_unit");
        invoiceHeaderList.add("Items_your_order_number");

        invoiceHeaderList.add("Net_weight");
        invoiceHeaderList.add("Net_weight_unit");
        invoiceHeaderList.add("Gross_weight");
        invoiceHeaderList.add("Gross_weight_unit");
    }


    /**
     * 创建Workbook
     *
     * @return
     * @throws IOException
     */
    private static Workbook createWorkBook() {
        Workbook wb = new XSSFWorkbook();
        return wb;
    }

    private static <T> String getPropertyValue(String propertyName, T obj) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().equals(propertyName)) {
                try {
                    if (propertyName.equals("pageNum")) {
                        List<Integer> pageNumList = (List<Integer>) f.get(obj);
                        return String.join(",", pageNumList.stream().sorted().map(String::valueOf).collect(Collectors.toList()));
                    }

                    Object pObj = f.get(obj);
                    if (pObj == null) return "";
                    if ((pObj instanceof BaseRectWords) == false) return "";

                    BaseRectWords pValue = (BaseRectWords) pObj;
                    String valueStr = pValue.words;
                    if (valueStr == null) return "";

                    valueStr = valueStr.replace("\n", " ");
                    valueStr = valueStr.replace("\u2014", "-");
                    return valueStr;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";

    }

}

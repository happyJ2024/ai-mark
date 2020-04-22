package cn.airesearch.aimarkserver.support.ocr;

import cn.airesearch.aimarkserver.tool.JsonUtils;
import cn.asr.appframework.utility.file.FileUtils;
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

    public static boolean export2Json(String exportFilePath, OCRResponse ocrResponse) {

        FileUtils.checkPath(exportFilePath);
        //输出excel
        FileOutputStream outputStream = null;
        try {
            File jsonFile = new File(exportFilePath); // 创建文件对象


            outputStream = new FileOutputStream(jsonFile); // 文件流
            String json = JsonUtils.toJsonStringPrettyFormat(ocrResponse);
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
        HandleWayBillList(ocrResponse.getWayBillList(), workbook, wayBillHeaderList);

//invoice
        HandleInvoiceList(ocrResponse.getInvoiceList(), workbook, invoiceHeaderList);


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
        Sheet sheet = workbook.getSheetAt(1);
        int rowCount = invoiceList.size() + 1;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Row newRow = sheet.createRow(rowIndex);
            for (int cellIndex = 0; cellIndex < invoiceHeaderList.size(); cellIndex++) {
                newRow.createCell(cellIndex);
            }
        }
        //header
        Row headerRow = sheet.getRow(0);
        CellStyle headerCellStyle = createHeadCellStyle(workbook);
        for (int cellIndex = 0; cellIndex < invoiceHeaderList.size(); cellIndex++) {
            headerRow.getCell(cellIndex).setCellStyle(headerCellStyle);
            headerRow.getCell(cellIndex).setCellValue(invoiceHeaderList.get(cellIndex));
        }

        //data
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);

            InvoiceModel invoiceModel = invoiceList.get(rowIndex - 1);

            for (int cellIndex = 0; cellIndex < invoiceHeaderList.size(); cellIndex++) {
                String cellValue = getPropertyValue(invoiceHeaderList.get(cellIndex), invoiceModel);
                dataRow.getCell(cellIndex).setCellValue(cellValue);
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
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }


    private static void initWayBillHeaderList() {
        wayBillHeaderList = new ArrayList<>();
        wayBillHeaderList.add("pageNum");
        wayBillHeaderList.add("Airport_Code");
        wayBillHeaderList.add("UXM");
        wayBillHeaderList.add("AIR_WAYBILL");
        wayBillHeaderList.add("CONSIGNEE");
        wayBillHeaderList.add("SHIPPER");
        wayBillHeaderList.add("PACKING");
        wayBillHeaderList.add("WEIGHT");
        wayBillHeaderList.add("GROSS");
        wayBillHeaderList.add("INCOTERM");
        wayBillHeaderList.add("INVOICE_NUMBER");
        wayBillHeaderList.add("TOTAL");
        wayBillHeaderList.add("METHOD");
        wayBillHeaderList.add("CUR");

    }

    private static void initInvoiceHeaderList() {
        invoiceHeaderList = new ArrayList<>();
        invoiceHeaderList.add("pageNum");
        invoiceHeaderList.add("Invoice_No");
        invoiceHeaderList.add("Supplier_code");
        invoiceHeaderList.add("Dispatch_address");
        invoiceHeaderList.add("Customer_Partnumber");
        invoiceHeaderList.add("Quantity");
        invoiceHeaderList.add("Price");
        invoiceHeaderList.add("Amount");
        invoiceHeaderList.add("Currency");
        invoiceHeaderList.add("Ctry_Origin");
        invoiceHeaderList.add("Your_order_number");
        invoiceHeaderList.add("Invoice_amount");
        invoiceHeaderList.add("cardboard_pallet");
        invoiceHeaderList.add("Net_weight");
        invoiceHeaderList.add("Gross_weight");
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

    private static String getPropertyValue(String propertyName, Object obj) {


        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().equals(propertyName)) {
                try {
                    if (propertyName.equals("pageNum")) {
                        List<Integer> pageNumList = (List<Integer>) f.get(obj);
                        return String.join(",", pageNumList.stream().sorted().map(String::valueOf).collect(Collectors.toList()));
                    }

                    String valueStr = f.get(obj).toString();
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

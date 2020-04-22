package cn.airesearch.aimarkserver.tool;

import cn.airesearch.aimarkserver.support.ocr.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class OCRTest {


    @Test
    public void testOCR() {

        String imagePathPrefix = "/data/testData/DGF_(S)ROBERT BOSCH GMBH(C)BOSCH AUTOMOTIVE PRODUCTS_1QK0062/";

        OCRRequest request = new OCRRequest();

        for (int i = 1; i <= 10; i++) {
            ImageInfo newImageInfo = new ImageInfo();
            newImageInfo.setId(String.valueOf(i));
            newImageInfo.setPath(imagePathPrefix + i + ".jpg");
            request.getImages().add(newImageInfo);
        }

        OCRResponse response = OCRWrapper.callOCRService(request);

        System.out.println(JsonUtils.toJsonString(response));
    }

    @Test
    public void testExport2Excel() throws FileNotFoundException, IOException {

        initWayBillHeaderList();

        File file = new File("/home/byj/Downloads/resp.json");
        String exportFilePath = "/home/byj/Downloads/simpleResponse.xlsx";


        FileReader reader = new FileReader(file);
        char[] buff = new char[(int) file.length()];
        reader.read(buff);
        String jsonStr = new String(buff);
        System.out.println(jsonStr);

        OCRResponse response = JsonUtils.jsonToObject(jsonStr, OCRResponse.class);


        Workbook workbook = createWorkBook();

        workbook.createSheet("运单");
        workbook.createSheet("invoice");

        //运单处理

        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = response.getWayBillList().size() + 1;
        for (int i = 0; i < rowCount; i++) {
            sheet.createRow(i);
        }

        //header
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < wayBillHeaderList.size(); i++) {
            headerRow.createCell(i);
        }

        for (int cellIndex = 0; cellIndex < wayBillHeaderList.size(); cellIndex++) {
            headerRow.getCell(cellIndex++).setCellValue(wayBillHeaderList.get(cellIndex));
        }
        //data
        for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);

            WayBillModel wayBillModel = response.getWayBillList().get(rowIndex - 1);

            for (int cellIndex = 0; cellIndex < wayBillHeaderList.size(); cellIndex++) {
                String cellValue = getPropertyValue(wayBillHeaderList.get(cellIndex), wayBillModel);
                dataRow.getCell(cellIndex++).setCellValue(cellValue);
            }
        }

        FileOutputStream outputStream = null;
        try {
            File excelFile = new File(exportFilePath); // 创建文件对象
            if (excelFile.exists()) {
                excelFile.delete();
            }
            outputStream = new FileOutputStream(excelFile); // 文件流

            outputStream = new FileOutputStream(file);
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
    }

    private String getPropertyValue(String propertyName, WayBillModel wayBillModel) {
        switch (propertyName) {
            case "pageNum":
                return String.join(",", wayBillModel.getPageNum().stream().sorted().map(String::valueOf).collect(Collectors.toList()));

            default:
                Field[] fields = wayBillModel.getClass().getDeclaredFields();
                for (Field f : fields) {
                    if (f.getName().equals(propertyName)) {
                        try {
                            return f.get(wayBillModel).toString();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return "";
        }
    }

    private static List<String> wayBillHeaderList;

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

    @Test
    public void testExcel() throws IOException {
        String exportFileTemplatePath = "/home/byj/Project/ai-mark/thirdParty/箱单导入模板v1.1.xlsx";
        String exportFilePath = "/home/byj/Downloads/respExport.xlsx";
        File excelFile = new File(exportFilePath); // 创建文件对象
        if (excelFile.exists()) {
            excelFile.delete();
        }
        FileCopyUtils.copy(new File(exportFileTemplatePath), new File(exportFilePath));

        FileInputStream in = new FileInputStream(excelFile); // 文件流
        Workbook workbook = getWorkbok(in, excelFile);
        int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
        System.out.println("getNumberOfSheets=" + sheetCount);

        for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            //获取总行数
            System.out.println("getLastRowNum=" + sheet.getLastRowNum());
            System.out.println("getSheetName=" + sheet.getSheetName());

            for (Row row : sheet) {
                try {

                    //如果当前行没有数据，跳出循环
                    if (row.getLastCellNum() == 0 || row.getCell(0) == null || row.getCell(0).toString().equals("")) {
                        continue;
                    }

                    //获取总列数(空格的不计算)
                    int columnTotalNum = row.getPhysicalNumberOfCells();
                    System.out.println("总列数：" + columnTotalNum);

                    System.out.println("最大列数：" + row.getLastCellNum());


                    int end = row.getLastCellNum();
                    for (int i = 0; i < end; i++) {
                        Cell cell = row.getCell(i);
                        if (cell == null) {
                            System.out.print("null" + "\t");
                            continue;
                        }

                        Object obj = getValue(cell);
                        System.out.print(obj.toString().replace("\n", "") + "\t");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    private static Object getValue(Cell cell) {
        Object obj = "";
        switch (cell.getCellType()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 创建Workbook
     *
     * @return
     * @throws IOException
     */
    public static Workbook createWorkBook() throws IOException {
        Workbook wb = new XSSFWorkbook();
        return wb;
    }
}

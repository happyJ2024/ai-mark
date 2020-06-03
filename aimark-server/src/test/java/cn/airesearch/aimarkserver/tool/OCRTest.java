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

        File file = new File("/home/byj/Downloads/resp.json");
        String exportFilePath = "/home/byj/Downloads/simpleResponse.xlsx";


        FileReader reader = new FileReader(file);
        char[] buff = new char[(int) file.length()];
        reader.read(buff);
        String jsonStr = new String(buff);
        System.out.println(jsonStr);

        OCRResponse ocrResponse = JsonUtils.jsonToObject(jsonStr, OCRResponse.class);

        OCRExporter.export2Excel(exportFilePath, ocrResponse);


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
    private static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

}

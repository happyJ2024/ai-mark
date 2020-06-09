package cn.airesearch.aimarkserver.service.impl;

import cn.airesearch.aimarkserver.constant.ExportConst;
import cn.airesearch.aimarkserver.constant.OcrConst;
import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.service.OCRService;
import cn.airesearch.aimarkserver.service.OcrresultService;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.support.ocr.*;
import cn.airesearch.aimarkserver.support.ocr.ai.invoice.InvoiceModel;
import cn.airesearch.aimarkserver.support.ocr.ai.waybill.WayBillModel;
import cn.airesearch.aimarkserver.tool.IoTool;
import cn.airesearch.aimarkserver.tool.PdfTool;
import cn.airesearch.aimarkserver.tool.SplitPDFPage;
import cn.airesearch.aimarkserver.tool.ZipTool;
import cn.asr.appframework.utility.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZhangXi
 */
@Slf4j
@Service
public class OCRServiceImpl implements OCRService {

    @Autowired
    OcrresultService ocrresultService;

    @Override
    public BaseResponse<OCRResponse> ocr(Integer projectId) {
        BaseResponse<OCRResponse> response = new BaseResponse<>();
        String projectDirPath = IoTool.buildFilePath(ResourceConst.ROOT_PATH, ResourceConst.PROJECT + projectId);


        OCRRequest ocrRequest = new OCRRequest();
        List<ImageInfo> images = ocrRequest.getImages();
        File dir = new File(projectDirPath);
        File[] files = dir.listFiles();

        List<String> subDirList = new ArrayList<>();
        for (File f : files
        ) {
            if (f.isDirectory() == false) continue;
            if (f.getName().equals(ExportConst.EXPORT_DIR_NAME)) continue;

            subDirList.add(f.getAbsolutePath());
        }
        subDirList.sort((s1, s2) -> s1.compareTo(s2));

        HashMap<String, String> idFileMap = new HashMap<>();
        Integer currentId = 1;
        for (String file : subDirList
        ) {
            File subDir = new File(file);
            File[] subDirFiles = subDir.listFiles();
            List<String> subImageFilesList = new ArrayList<>();
            for (File f : subDirFiles
            ) {
                if (f.isDirectory()) continue;
                if (f.getAbsolutePath().contains(OcrConst.BACKUP_EXTENDS)) {
                    continue;
                }
                if (f.getName().equals(ExportConst.DIGITAL_WAYBILL_JSON_DATA)) {
                    ocrRequest.setWaybillJsonPath(f.getAbsolutePath());
                    continue;
                }
                subImageFilesList.add(f.getAbsolutePath());
            }
            subImageFilesList.sort((s1, s2) -> {
                String s1Convert = s1.substring(s1.lastIndexOf(File.separator) + 1, s1.lastIndexOf("."));
                String s2Convert = s2.substring(s2.lastIndexOf(File.separator) + 1, s2.lastIndexOf("."));
                Integer intS1 = Integer.parseInt(s1Convert);
                Integer intS2 = Integer.parseInt(s2Convert);
                if (intS1 > intS2) return 1;
                if (intS1 < intS2) return -1;
                return 0;
            });

            for (String subImageFilePath : subImageFilesList
            ) {
                idFileMap.put(currentId.toString(), subImageFilePath);
                currentId++;
            }
        }

        for (int i = 1; i < currentId; i++) {
            ImageInfo newImageInfo = new ImageInfo();
            String id = String.valueOf(i);
            newImageInfo.setId(id);
            newImageInfo.setPath(idFileMap.get(String.valueOf(i)));
            images.add(newImageInfo);
        }
        images.sort(new Comparator<ImageInfo>() {
            @Override
            public int compare(ImageInfo t1, ImageInfo t2) {
                int id1 = Integer.parseInt(t1.getId());
                int id2 = Integer.parseInt(t1.getId());
                if (id1 > id2) return 1;
                if (id1 == id2) return 0;
                return -1;
            }
        });

        OCRResponse ocrResponse = OCRWrapper.callOCRService(ocrRequest);
        if (ocrResponse == null) {
            response.fail("ocr识别失败");
            return response;
        }

        //save ocr result into db
        saveOCRResultIntoDb(projectId, ocrResponse, idFileMap);

        response.success("导出ocr结果成功");
        response.setData(ocrResponse);
        return response;
    }

    private void saveOCRResultIntoDb(Integer projectId, OCRResponse ocrResponse, HashMap<String, String> idFileMap) {

        ocrresultService.saveOCRResultIntoDb(projectId, ocrResponse, idFileMap);
    }


    private void addSplitPDFPageList(String pdfFilePath, int splitPageNumber, List<SplitPDFPage> splitPDFPageList) {

        for (SplitPDFPage d : splitPDFPageList
        ) {
            if (d.getPdfPath().equals(pdfFilePath)) {
                d.getPages().add(splitPageNumber);
                return;
            }
        }
        SplitPDFPage newSplitPDFPage = new SplitPDFPage();
        newSplitPDFPage.setPdfPath(pdfFilePath);
        newSplitPDFPage.getPages().add(splitPageNumber);
        splitPDFPageList.add(newSplitPDFPage);
    }

    public void export(String projectId, OCRResponse ocrResponse, HashMap<String, String> idFileMap) {
        //准备Export的数据
        String projectDirPath = IoTool.buildFilePath(ResourceConst.ROOT_PATH, ResourceConst.PROJECT + projectId);
        String exportDir = IoTool.buildFilePath(ResourceConst.ROOT_PATH, ResourceConst.PROJECT + projectId, ExportConst.EXPORT_DIR_NAME);
//        FileUtils.deleteFolder(exportDir);

        String exportExcelFilePath = IoTool.buildFilePath(exportDir, ExportConst.EXPORT_EXCEL_FILE_NAME);
        String exportJsonFilePath = IoTool.buildFilePath(exportDir, ExportConst.EXPORT_JSON_FILE_NAME);

        boolean exportResult = OCRExporter.export2Json(exportJsonFilePath, ocrResponse);
        exportResult &= OCRExporter.export2Excel(exportExcelFilePath, ocrResponse);

        //复制原始文件
        File dir = new File(projectDirPath);
        File[] files = dir.listFiles();
        for (File f : files
        ) {
            if (f.isDirectory()) continue;
            String destFile = IoTool.buildFilePath(exportDir, ExportConst.EXPORT_ORIGIN_DIR_NAME, f.getName());
            try {
                FileUtils.checkPath(destFile);
                FileCopyUtils.copy(f, new File(destFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //重新生成运单和invoice的pdf文件
        int shipPdfIndex = 1;
        for (WayBillModel wayBillModel :
                ocrResponse.waybill) {
            List<Integer> pageNum = wayBillModel.pageNum;
            List<SplitPDFPage> splitPDFPageList = new ArrayList<>();
            for (Integer page : pageNum) {

                String imageFile = idFileMap.get(String.valueOf(page));
                String pdfFileDirName = (new File(imageFile)).getParentFile().getAbsolutePath();
                String pdfFilePath = pdfFileDirName + ".pdf";
                Integer splitPageNumber = Integer.parseInt(imageFile.substring(imageFile.lastIndexOf(File.separator) + 1, imageFile.lastIndexOf(".")));

                addSplitPDFPageList(pdfFilePath, splitPageNumber, splitPDFPageList);
            }

            String destFile = IoTool.buildFilePath(exportDir, ExportConst.FILE_NAME_WAYBILL + shipPdfIndex + ".pdf");
            try {
                PdfTool.generatePDF(destFile, splitPDFPageList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            shipPdfIndex++;

        }

        int invoicePdfIndex = 1;
        for (InvoiceModel invoiceModel :
                ocrResponse.invoice) {
            List<Integer> pageNum = invoiceModel.pageNum;
            List<SplitPDFPage> splitPDFPageList = new ArrayList<>();
            for (Integer page : pageNum) {

                String imageFile = idFileMap.get(String.valueOf(page));
                String pdfFileDirName = (new File(imageFile)).getParentFile().getAbsolutePath();
                String pdfFilePath = pdfFileDirName + ".pdf";
                Integer splitPageNumber = Integer.parseInt(imageFile.substring(imageFile.lastIndexOf(File.separator) + 1, imageFile.lastIndexOf(".")));

                addSplitPDFPageList(pdfFilePath, splitPageNumber, splitPDFPageList);
            }
            String destFile = IoTool.buildFilePath(exportDir, ExportConst.FILE_NAME_INVOICE + invoicePdfIndex + ".pdf");
            try {
                PdfTool.generatePDF(destFile, splitPDFPageList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            invoicePdfIndex++;
        }

        //zip压缩包
        String targetZipFile = IoTool.buildFilePath(ResourceConst.ROOT_PATH, ResourceConst.PROJECT + projectId, projectId + ".zip");
        new ZipTool(new File(targetZipFile)).zipFiles(new File(exportDir));

    }
}

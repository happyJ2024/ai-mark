package cn.airesearch.aimarkserver.support.ocr;

import lombok.Data;

import java.util.List;

@Data
public class OCRResponse {
    private List<WayBillList> wayBillList;
    private List<InvoiceList> invoiceList;

}

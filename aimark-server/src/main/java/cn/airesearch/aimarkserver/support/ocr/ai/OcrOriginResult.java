package cn.airesearch.aimarkserver.support.ocr.ai;

import cn.airesearch.aimarkserver.support.ocr.ai.invoice.InvoiceModel;
import cn.airesearch.aimarkserver.support.ocr.ai.waybill.WayBillModel;
import lombok.Data;

import java.util.List;

public class OcrOriginResult {
    public List<WayBillModel> waybill;
    public List<InvoiceModel> invoice;

    /**
     * 运单页码
     * 只有一个运单
     * 运单的页码可能有多个
     */
    public List<Integer> waybillPages;

    /**
     * 发票页码
     * 可能有多个发票
     * 每张发票的页码可能有多个
     */
    public List<List<Integer>> invoicePages;

}

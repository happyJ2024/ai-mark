package cn.airesearch.aimarkserver.support.ocr.ai.invoice;

import lombok.Data;

import java.util.List;


public class InvoiceModel {
    public Gross_weight Gross_weight;
    public Invoice_No Invoice_No;

    public List<Item> Items;
    public Net_weight Net_weight;
    public supplier_code supplier_code;


    public List<Integer> pageNum;
}

package cn.ar.aimark.server.support.ocr;

import lombok.Data;

import java.util.List;

@Data
public class WayBillList {

    private List<Integer> pageNum;
    private String Airport_Code;
    private String UXM;
    private String AIR_WAYBILL;
    private String CONSIGNEE;
    private String SHIPPER;
    private String PACKING;
    private String WEIGHT;
    private String GROSS;
    private String INCOTERM;
    private String INVOICE_NUMBER;
    private String TOTAL;
    private String METHOD;
    private String CUR;


}
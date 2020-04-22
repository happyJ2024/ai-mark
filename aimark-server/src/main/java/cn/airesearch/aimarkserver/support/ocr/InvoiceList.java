package cn.airesearch.aimarkserver.support.ocr;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceList {
    private List<Integer> pageNum;
    private String Invoice_No;
    private String Supplier_code;
    private String Dispatch_address;
    private String Customer_Partnumber;
    private String Quantity;
    private String Price;
    private String Amount;
    private String Currency;
    private String Ctry_Origin;
    private String Your_order_number;
    private String Invoice_amount;
    private String cardboard_pallet;
    private String Net_weight;
    private String Gross_weight;
}

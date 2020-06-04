package cn.airesearch.aimarkserver.support.ocr.ai.invoice;

public class Item {
    public Amount Amount;

    public Ctry_origin Ctry_origin;

    public Currency Currency;

    public Customer_Partnumber Customer_Partnumber;
    public Gross_weight Gross_weight;

    public Net_weight Net_weight;
    public Quantity Quantity;
    public price price;

    public your_order_number your_order_number;

    /**
     * 发票条目顺序号
     */
    public int ItemSeq;
}

export class LabelPreDefine {
    public static readonly WAYBILL_KEYWORDS: string[] = [
        "CUBIC_CONTENT",
        "Chargeable_Weight",
        "Destination_District",
        "District_Code",
        "Freight",
        "Freight_Currency",
        "Gross_Weight",
        "H",
        "HBL_HABW",
        "Incoterms",
        "L",
        "Location_To",
        "MBL_MAWB",
        "Origin_Country",
        "Packaging_Type",
        "Pieces",
        "Port_of_Destination",
        "Transportation_Method",
        "Unit",
        "Volume",
        "W",
    ];

    public static readonly INVOICE_KEYWORDS: string[] = [
        "Gross_weight",
        "Invoice_No",
        "Net_weight",
        "supplier_code",
 
    ];
    public static readonly INVOICE_ITEMS_KEYWORDS: string[] = [
         
        "Ctry_origin",
        "Currency",
        "Customer_Partnumber",
        "Gross_weight",
        "Net_weight",
        "Quantity",
        "price",
        "your_order_number",
    ];


    public static readonly WAYBILL_KEYWORDS_PREFIX:string="运单";

    public static readonly INVOICE_KEYWORDS_PREFIX:string="发票";

    public static readonly INVOICE_ITEMS_KEYWORDS_PREFIX:string="发票条目";
 
}
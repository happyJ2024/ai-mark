import {ExportFormatType} from "../enums/ExportFormatType";
import {IExportFormat} from "../../interfaces/IExportFormat";

export const RectExportFormatData: IExportFormat[] = [
    {
        type: ExportFormatType.AR_JSON,
        label: "一个简单的json格式文件，仅包含标注数据."
    },
    {
        type: ExportFormatType.AR_JSON_FILE_BUNDLE,
        label: "一个压缩包（.zip），包含标注数据（json）和关联的图片文件. 文件体积可能很大，请谨慎选择该项！"
    },
    // {
    //     type: ExportFormatType.YOLO,
    //     label: "A .zip package containing files in YOLO format."
    // },
    // {
    //     type: ExportFormatType.VOC,
    //     label: "A .zip package containing files in VOC XML format."
    // },
    // {
    //     type: ExportFormatType.CSV,
    //     label: "Single CSV file."
    // }
];
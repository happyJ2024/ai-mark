package cn.airesearch.aimarkserver.model;

import java.util.Date;
import lombok.Data;

@Data
public class Ocrresult {
    private Integer itemId;

    private String originJson;

    private Date ocrDatetime;

    private String updateJson;

    private Date updateDatetime;

    private String difference;

    private String idfilemap;
}
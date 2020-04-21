package cn.airesearch.aimarkserver.support;

import lombok.Data;

/**
 * @author ZhangXi
 */
@Data
public class ItemConvert {

    private Integer itemId;

    private Double completePercent;

    private Integer completeNumber;

    private Integer totalNumber;


    public void resetPercent() {
        this.completePercent = this.completeNumber.doubleValue() / this.totalNumber;
    }

}

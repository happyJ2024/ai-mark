package cn.airesearch.aimarkserver.support.ocr.ai;

import java.util.ArrayList;
import java.util.List;


public class BaseRectWords {

    /**
     * 矩形框所在的页码
     */
    public int pageNum;


    /**
     * 矩形框的对角坐标点列表, [TopLeft-x,TopLeft-y, BottomRight-x,BottomRight-y]
     */
    public List<Integer> rect;

    /**
     * 矩形框里面的文本
     */
    public String words;

    public BaseRectWords() {
        rect = new ArrayList<>();
        rect.add(0);
        rect.add(0);
        rect.add(0);
        rect.add(0);

        words = "";
        pageNum = 0;

    }
}

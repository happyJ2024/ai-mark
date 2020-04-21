package cn.airesearch.aimarkserver.support;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangXi
 */
public class ItemConvertManager {

    private static final ConcurrentHashMap<Integer, ItemConvert> PROGRESS_MAP = new ConcurrentHashMap<>();


    public static void register(Integer itemId, Integer totalNum) {
        ItemConvert convert = new ItemConvert();
        convert.setItemId(itemId);
        convert.setCompleteNumber(0);
        convert.setTotalNumber(totalNum);
        convert.setCompletePercent(0.0d);
        PROGRESS_MAP.put(itemId, convert);
    }

    public static void update(Integer itemId, ItemConvert convert) {
        PROGRESS_MAP.put(itemId, convert);
    }

    public static ItemConvert get(Integer itemId) {
        return PROGRESS_MAP.get(itemId);
    }

    public static void cancel(Integer itemId) {
        PROGRESS_MAP.remove(itemId);
    }

}

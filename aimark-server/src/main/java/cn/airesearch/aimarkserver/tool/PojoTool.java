package cn.airesearch.aimarkserver.tool;

import org.springframework.beans.BeanUtils;

/**
 * @author ZhangXi
 */
public final class PojoTool {

    /**
     * 将持久化数据拷贝至VO对象
     * @param model Object
     * @param vo Object
     */
    public static void copyModelToVo(Object model, Object vo) {
        BeanUtils.copyProperties(model, vo);
    }

}

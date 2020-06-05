package cn.airesearch.aimarkserver.pojo.requestvo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateOCRResultVO {

    @NotNull
    @Schema(description = "项目ID", example = "1", format = "整数")
    private Integer id;

    @NotNull
    @Schema(description = "更新的标签列表")
    private List<Label> labelList;

}

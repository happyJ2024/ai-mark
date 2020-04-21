package cn.airesearch.aimarkserver.pojo.modelvo;

import cn.airesearch.aimarkserver.model.Item;
import cn.airesearch.aimarkserver.modelenum.ItemStatus;
import cn.airesearch.aimarkserver.tool.PojoTool;
import cn.airesearch.aimarkserver.validator.GAdd;
import cn.airesearch.aimarkserver.validator.GUpd;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author ZhangXi
 */
@Schema(name = "ItemVO", description = "项目数据")
public class ItemVO extends Item {

    public ItemVO() {
    }

    public ItemVO(Item item) {
        PojoTool.copyModelToVo(item, this);
        this.setStatus(item.getStatus());
    }

    @NotNull(groups = {GUpd.class})
    @Null(groups = {GAdd.class})
    @Schema(description = "项目ID", example = "1", format = "null或整数")
    @Override
    public Integer getId() {
        return super.getId();
    }

    @NotBlank
    @Schema(description = "项目名称")
    @Override
    public String getName() {
        return super.getName();
    }

    @Null
    @Schema(description = "项目状态")
    @Override
    public ItemStatus getStatus() {
        return super.getStatus();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @JsonIgnore
    @Override
    public Boolean getIsDeleted() {
        return super.getIsDeleted();
    }

    @JsonIgnore
    @Override
    public Integer getRowVersion() {
        return super.getRowVersion();
    }

    @JsonIgnore
    @Override
    public void setIsDeleted(Boolean isDeleted) {
        super.setIsDeleted(isDeleted);
    }

    @JsonIgnore
    @Override
    public void setRowVersion(Integer rowVersion) {
        super.setRowVersion(rowVersion);
    }

}

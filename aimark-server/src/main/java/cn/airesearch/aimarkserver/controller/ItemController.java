package cn.airesearch.aimarkserver.controller;

import cn.airesearch.aimarkserver.pojo.modelvo.ItemVO;
import cn.airesearch.aimarkserver.service.ItemService;
import cn.airesearch.aimarkserver.support.base.BaseResponse;
import cn.airesearch.aimarkserver.validator.GAdd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 标记项目控制器
 * @author ZhangXi
 */
@Tag(name = "/api", description = "项目接口")
@Slf4j
@Validated
@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(
            summary = "获取项目列表"
    )
    @GetMapping(value = "/api/getProjectList")
    public BaseResponse<List> getItems() {
        List<ItemVO> data = itemService.getEnabledItems();
        BaseResponse<List> response = new BaseResponse<>();
        response.success("项目列表获取成功", data);
        return response;
    }

    @Operation(
            summary = "新增项目"
    )
    @PostMapping(value = "/api/createProject")
    public BaseResponse<ItemVO> addItem(@RequestBody @Validated({GAdd.class, Default.class}) ItemVO form) {
        ItemVO data = itemService.addItem(form);
        BaseResponse<ItemVO> response = new BaseResponse<>();
        response.success("新增项目成功", data);
        return response;
    }

    @Operation(
            summary = "删除项目"
    )
    @PostMapping(value = "/api/deleteProject")
    public BaseResponse deleteItem(@RequestParam("id") @NotNull Integer id) {
        itemService.deleteItem(id);
        BaseResponse response = new BaseResponse();
        response.success("删除项目成功");
        return response;
    }

}

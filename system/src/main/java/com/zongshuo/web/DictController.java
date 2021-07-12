package com.zongshuo.web;

import com.zongshuo.Contains;
import com.zongshuo.annotations.validators.Delete;
import com.zongshuo.annotations.validators.Insert;
import com.zongshuo.annotations.validators.Update;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.model.DictModel;
import com.zongshuo.service.DictDataService;
import com.zongshuo.service.DictService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Api(tags = "系统功能-字典项控制器")
@RestController
@RequestMapping("/sys/dict")
public class DictController extends BaseController{
    @Autowired
    private DictService dictService;
    @Autowired
    private DictDataService dictDataService;

    @ApiOperation("查询所有字典项")
    @GetMapping("/list")
    public ResponseJsonMsg dictList(){
        log.info("查询全部字典项");
        return ResponseJsonMsg.ok().setData(dictService.getAllDict());
    }

    @ApiOperation("分页查询字典项的字值")
    @GetMapping("/data/list")
    public PageResult<DictDataModel> dictDataPage(HttpServletRequest request){
        log.info("分页查询字典值:{}", request);
        PageParam<DictDataModel> pageParam = new PageParam<>(request);
        return dictDataService.getDictDataPage(pageParam);
    }

    @ApiOperation("添加字典")
    @PutMapping("/add")
    public ResponseJsonMsg addDict(@RequestBody @Valid DictModel dict){
        log.info("新增字典：{}", dict);
        try {
            dictService.saveDict(dict);
        } catch (IllegalAccessException e) {
            log.error("新增字典异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }

        return ResponseJsonMsg.ok();
    }

    @ApiOperation("添加字典值")
    @PutMapping("/data/add")
    public ResponseJsonMsg addDictData(@RequestBody @Validated(Insert.class) DictDataModel dictData){
        log.info("新增字典值:", dictData);
        try {
            dictDataService.addDictData(dictData);
        } catch (IllegalAccessException e) {
            log.error("新增字典值异常:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("编辑字典项")
    @PutMapping("/edit")
    public ResponseJsonMsg editDict(@RequestBody @Validated(Update.class) DictModel dict){
        log.info("编辑字典:{}", dict);
        try {
            dictService.editDict(dict);
        } catch (IllegalAccessException e) {
            log.error("更新字典项异常:{}", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("编辑字典值")
    @PutMapping("/data/edit")
    public ResponseJsonMsg editDictData(@RequestBody @Validated(Update.class) DictDataModel dictData){
        log.info("编辑字典值:", dictData);
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("删除字典项")
    @DeleteMapping
    public ResponseJsonMsg deleteDict(@RequestBody @Validated(Delete.class) DictModel dict){
        log.info("删除字典项:{}", dict);
        try {
            dictService.removeDict(dict);
        } catch (IllegalAccessException e) {
            log.error("删除字典异常:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "删除字典及字典项失败！");
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("删除字典值")
    @DeleteMapping("/data")
    public ResponseJsonMsg deleteDictData(@RequestBody @Validated(Delete.class) DictDataModel dictData){
        log.info("删除字典值:{}", dictData);
        try {
            dictDataService.deleteDictData(dictData);
        } catch (IllegalAccessException e) {
            log.error("删除字典值失败:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }
}

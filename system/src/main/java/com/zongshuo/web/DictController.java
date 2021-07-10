package com.zongshuo.web;

import com.zongshuo.Contains;
import com.zongshuo.model.DictModel;
import com.zongshuo.service.DictService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "系统功能-字典项控制器")
@RestController
@RequestMapping("/sys/dict")
public class DictController extends BaseController{
    @Autowired
    private DictService dictService;

    @ApiOperation("查询所有字典项")
    @GetMapping("/list")
    public ResponseJsonMsg dictList(){
        log.info("查询全部字典项");
        return ResponseJsonMsg.ok().setData(dictService.getAllDict());
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
}

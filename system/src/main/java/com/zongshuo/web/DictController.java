package com.zongshuo.web;

import com.zongshuo.service.DictService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

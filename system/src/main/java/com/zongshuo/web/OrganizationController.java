package com.zongshuo.web;

import com.zongshuo.Contains;
import com.zongshuo.annotations.validators.Insert;
import com.zongshuo.model.OrganizationModel;
import com.zongshuo.service.OrganizationService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sys/org")
public class OrganizationController extends BaseController{
    @Autowired
    private OrganizationService orgService;

    @ApiOperation("根据机构编号获取机构树")
    @ApiImplicitParam(name = "orgCode", value = "机构编号", dataType = "String", paramType = "query")
    @PostMapping("/tree")
    public ResponseJsonMsg getOrgTree(@RequestBody OrganizationModel org){
        log.info("查询组织机构树:{}", org);
        return ResponseJsonMsg.ok().setData(orgService.getOrgTree(org.getId()));
    }

    @ApiOperation("新增组织")
    @PutMapping()
    public ResponseJsonMsg addOrg(@RequestBody @Validated(Insert.class) OrganizationModel org){
        log.info("新增组织:", org);
        try {
            orgService.addOrg(org);
        } catch (IllegalAccessException e) {
            log.error("新增组织一异常:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }
}

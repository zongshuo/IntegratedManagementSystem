package com.zongshuo;

import com.zongshuo.annotation.AuthDefinition;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;

import java.util.List;
import java.util.Map;

/**
 * ClassName: System_info
 * date: 2021/7/20 11:08
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@AuthDefinition(name = "系统管理", authority = "sys", type = AccessType.MODULE, path = "/system")
class SystemInfo {
}

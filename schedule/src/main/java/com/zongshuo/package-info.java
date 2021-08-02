/**
 * ClassName: package-info
 * date: 2021/8/2 17:38
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
package com.zongshuo;

import com.zongshuo.annotation.AuthDefinition;
import com.zongshuo.authorization.model.AccessType;

@AuthDefinition(name = "任务管理", authority = "sch", type = AccessType.MODULE, path = "/schedule")
class schedule{ }
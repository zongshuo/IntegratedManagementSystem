package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.AuthDefinition;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName: AuthServiceBaseTest
 * date: 2021/7/28 15:05
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@AuthDefinition(name = "测试权限收集", authority = "test:access:collect:type", type = AccessType.MENU, path = "/test/access/collect")
class AuthServiceBaseTest {

    @Test
    @AuthDefinition(name = "测试权限收集", authority = "test:access:collect:method", type = AccessType.API, path = "/test/access/collect")
    void collectAccessPoint() throws IOException {
        AuthService service = AuthService.fromBase(AuthDefinition.class);
        Map<AccessType, List<AccessPoint>> accessPoint = service.collectAccessPoint("/com/zongshuo/**/");
        assertNotNull(accessPoint);
        assertEquals(2, accessPoint.size());
        assertNotNull(accessPoint.get(AccessType.MENU));
        assertEquals("test:access:collect:type", accessPoint.get(AccessType.MENU).get(0).getAuthority());
        assertNotNull(accessPoint.get(AccessType.API));
        assertEquals("test:access:collect:method", accessPoint.get(AccessType.API).get(0).getAuthority());
    }
}
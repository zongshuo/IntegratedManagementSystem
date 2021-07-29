package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ClassName: AuthServiceImplInitAuthTest
 * date: 2021/7/29 9:49
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@AuthDefinition(name = "测试权限点收集并创建鉴权注解-类", authority = "test:access:collection:build:class", type = AccessType.MENU)
class AuthServiceImplInitAuthTest {


    @Test
    @AuthDefinition(name = "测试权限点收集并创建鉴权注解-方法", authority = "test:access:collection:build:method", type = AccessType.MENU)
    void collectAccessPoint() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AuthService service = AuthService.fromInitAuth(AuthDefinition.class);
        Map<AccessType, List<AccessPoint>> accessMap = service.collectAccessPoint("/com/zongshuo/**");
        assertNotNull(accessMap);
        AuthDefinition authDefinition = AuthServiceImplInitAuthTest.class.getDeclaredAnnotation(AuthDefinition.class);
        for (Method method : authDefinition.annotationType().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MapToAuths.class)) {
                MapToAuth[] mapToAuths = method.getAnnotation(MapToAuths.class).value();
                for (MapToAuth mapToAuth : mapToAuths) {
                    Annotation annotation = AuthServiceImplInitAuthTest.class.getAnnotation(mapToAuth.authAnnotation());
                    assertNotNull(annotation);
                    Method field = annotation.annotationType().getDeclaredMethod(mapToAuth.name());
                    assertEquals(method.invoke(authDefinition), field.invoke(annotation));
                }
            }
        }
    }
}

package com.zongshuo.annotation.handler;

import com.zongshuo.annotation.annotations.AuthDefinition;
import com.zongshuo.annotation.util.AnnotationUtil;
import org.junit.jupiter.api.Test;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName: AnnotationServiceMethodTest
 * date: 2021/7/27 18:12
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
class AnnotationServiceMethodTest {

    @Test
    void addAnnotation() throws NoSuchMethodException, InvalidTargetObjectTypeException {
        Map<String, Object> memberValues = new LinkedHashMap<>();
        String name = "test method add annotation";
        memberValues.put("name", name);
        AuthDefinition authDefinition = AnnotationUtil.newAnnotation(AuthDefinition.class, memberValues);
        assertNotNull(authDefinition);

        Method method = AnnotationServiceMethodTest.class.getDeclaredMethod("addAnnotation");
        assertNotNull(method);
        AnnotationService service = AnnotationService.from(method);
        service.addAnnotation(authDefinition);

        authDefinition = service.getAnnotation(AuthDefinition.class);
        assertNotNull(authDefinition);
        assertEquals(name, authDefinition.name());
    }
}
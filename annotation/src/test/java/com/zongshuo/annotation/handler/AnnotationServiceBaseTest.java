package com.zongshuo.annotation.handler;

import com.zongshuo.annotation.annotations.AuthDefinition;
import com.zongshuo.annotation.annotations.ValidateAuthCode;
import com.zongshuo.annotation.util.AnnotationUtil;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName: AnnotationServiceBaseTest
 * date: 2021/7/27 14:11
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@AuthDefinition(name = "testName", path = "path", authority = "authority", authType = AuthDefinition.AuthType.MENU)
class AnnotationServiceBaseTest {
    private AnnotationService service;
    private Class clazz;
    private Method method;
    private BeforeEach annotation;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        if (clazz == null) clazz = AnnotationServiceBaseTest.class;
        if (method == null) {
            method = AnnotationServiceBaseTest.class.getDeclaredMethod("setUp");
            method.setAccessible(true);
        }
        if (annotation == null) {
            annotation = method.getAnnotation(BeforeEach.class);
        }
    }

    @Test
    void annotations() {
        Map<Class<? extends Annotation>, List<Annotation>> annotations;
        service = new AnnotationServiceBase(clazz) {
        };
        annotations = service.annotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertTrue(annotations.containsKey(AuthDefinition.class));
        assertEquals("testName", ((AuthDefinition) annotations.get(AuthDefinition.class).get(0)).name());

        service = new AnnotationServiceBase(method) {
        };
        annotations = service.annotations();
        assertNotNull(annotations);
        assertEquals(2, annotations.size());
        assertTrue(annotations.containsKey(BeforeEach.class));
        assertTrue(annotations.containsKey(API.class));

        service = new AnnotationServiceBase(annotation.annotationType()) {
        };
        annotations = service.annotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertTrue(annotations.containsKey(API.class));
    }

    @Test
    void addAnnotation() throws InvalidTargetObjectTypeException {
        LinkedHashMap<String, Object> memberValues = new LinkedHashMap<>();
        String message = "test annotation add";
        memberValues.put("name", message);
        AuthDefinition authDefinition = AnnotationUtil.newAnnotation(AuthDefinition.class, memberValues);
        assertNotNull(authDefinition);
        assertEquals(message, authDefinition.name());

        service = new AnnotationServiceBase(clazz) {};
        service.addAnnotation(authDefinition);
        authDefinition = (AuthDefinition) service.getAnnotation(authDefinition.annotationType());
        assertNotNull(authDefinition);
        assertEquals(message, authDefinition.name());

        Inherited inherited = AnnotationUtil.newAnnotation(Inherited.class, null);
        assertNotNull(inherited);

        service = new AnnotationServiceBase(authDefinition.annotationType()) {};
        assertNotNull(service);
        service.addAnnotation(inherited);
        inherited = (Inherited) authDefinition.annotationType().getAnnotation(Inherited.class);
        assertNotNull(inherited);
    }
}
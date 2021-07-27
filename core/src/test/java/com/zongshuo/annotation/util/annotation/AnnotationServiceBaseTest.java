package com.zongshuo.annotation.util.annotation;

import com.zongshuo.annotation.annotations.AuthDefinition;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    private Class clazz ;
    private Method method ;
    private Annotation annotation;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        if (clazz == null) clazz = AnnotationServiceBaseTest.class;
        if (method == null) {
            method = AnnotationServiceBaseTest.class.getDeclaredMethod("setUp");
            method.setAccessible(true);
        }
        if (annotation == null){
            annotation = AnnotationServiceBaseTest.class.getDeclaredAnnotation(SpringBootTest.class);
        }
    }

    @Test
    void annotations() {
        Map<Class<? extends Annotation>, List<Annotation>> annotations ;
        service = new AnnotationServiceBase(clazz) {};
        annotations = service.annotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertTrue(annotations.containsKey(AuthDefinition.class));
        assertEquals("testName", ((AuthDefinition)annotations.get(AuthDefinition.class).get(0)).name());

        service = new AnnotationServiceBase(method) {};
        annotations = service.annotations();
        assertNotNull(annotations);
        assertEquals(2, annotations.size());
        assertTrue(annotations.containsKey(BeforeEach.class));
        assertTrue(annotations.containsKey(API.class));
    }
}
package com.zongshuo.util;

import com.sun.org.apache.xerces.internal.xs.StringList;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.annotation.AnnotationDescription;
import sun.misc.Unsafe;
import sun.reflect.annotation.AnnotationType;
import sun.reflect.annotation.ExceptionProxy;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: AnnotationUtil
 * date: 2021/7/23 8:54
 *
 * @author zongshuo
 * version: 1.0
 * Description: 处理注解的工具类
 */
@Slf4j
public final class AnnotationUtil {
    private AnnotationUtil(){}

    /**
     * 获取注解的属性值
     * 以map格式返回：key-属性名、value-属性值
     * @param annotation
     * @return
     */
    public static Map<String, Object> getMemberValues(final Annotation annotation){
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = handler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            return (Map<String, Object>) field.get(handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Get annotation member values failed") ;
        }
    }

    /**
     * 慎用
     * 获取类的注解存储对象
     * 该对象为Class的内部私有静态类
     * 该对象内部包含当前类拥有的注解的所有实例
     * 通过操作该对象内注解的实例可以动态操作类的注解
     * 该对象(AnnotationData)有三个属性：
     *    Map<Class<? extends Annotation>, Annotation> annotations:当前类及其父类拥有的所有注解，包含declaredAnnotations
     *    Map<Class<? extends Annotation>, Annotation> declaredAnnotations:当前类拥有的注解
     *    int redefinedCount: 当调用Class.annotationData方法时会判断该属性与Class.classRedefinedCount是否相等，
     *    如果不相等会重新创建AnnotationData对象，如果相等就直接返回Class.annotationData属性
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static Object getAnnotationData(final Class<? extends AnnotatedElement> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Class.class.getDeclaredMethod("annotationData");
        method.setAccessible(true);
        return method.invoke(clazz);
    }

    /**
     * 慎用
     * 返回Class.AnnotationData.declaredAnnotations对象
     * 该LinkedHashMap内存储当前类拥有的注解
     * 通过修改该对象内的key、value，可以动态修改类的注解或者注解属性
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotations(final Class<? extends AnnotatedElement> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Object obj = getAnnotationData(clazz);
        Field declaredAnnotations = obj.getClass().getDeclaredField("declaredAnnotations");
        declaredAnnotations.setAccessible(true);
        return (Map<Class<? extends Annotation>, Annotation>) declaredAnnotations.get(obj);
    }


    /**
     * 判断当前值是否为属性默认值
     * @param method
     * @param value
     * @return
     */
    public static boolean isDefaultValue(final Method method, final Object value){
        final Object defaultValue = method.getDefaultValue();
        if (method.getReturnType().isArray()){
            return Arrays.equals((Object []) defaultValue, (Object []) value);
        }
        return defaultValue.equals(value);
    }
    public static boolean isDefaultValue(final Method method, final Map<String, Object> memberValues){
        return isDefaultValue(method, memberValues.get(method.getName()));
    }

    /**
     * 判断注解类型是否JDK注解
     * @param type
     * @return
     */
    public static boolean isJdkAnnotation(final Class<? extends Annotation> type){
        return type == Documented.class ||
                type == Target.class ||
                type == Retention.class ||
                type == Inherited.class ||
                type == Repeatable.class ||
                type == Native.class ;
    }

    /**
     * 生成新的动态代理
     * @param type
     * @param values
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A newAnnotation(final Class<A> type, final Map<String, Object> values){
        return (A) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new AnnotationInvocationHandler(type, values));
    }



    /**
     * Class.AnnotationData外观类，拥有相同的属性，修改本对象的属性后会传导到Class.AnnotationData中
     */
    public static class AnnotationData {
        private final Map<Class<? extends Annotation>, Annotation> annotations;
        private final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
        private final Field redefinedCountField;
        private final Object annotationDataFromClass ;

        private AnnotationData(Object annotationDataFromClass) throws NoSuchFieldException, IllegalAccessException {
            Field annotationsField = annotationDataFromClass.getClass().getDeclaredField("annotations");
            Field declaredAnnotationsField = annotationDataFromClass.getClass().getDeclaredField("declaredAnnotations");
            this.redefinedCountField = annotationDataFromClass.getClass().getDeclaredField("redefinedCount");
            annotationsField.setAccessible(true);
            declaredAnnotationsField.setAccessible(true);
            this.annotations = (Map<Class<? extends Annotation>, Annotation>) annotationsField.get(annotationDataFromClass);
            this.declaredAnnotations = (Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(annotationDataFromClass);
            this.annotationDataFromClass = annotationDataFromClass;
        }
        public static AnnotationData getInstance(final Class<? extends AnnotatedElement> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            return new AnnotationData(AnnotationUtil.getAnnotationData(clazz));
        }

        public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
            return annotations;
        }

        public Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotations() {
            return declaredAnnotations;
        }

        public int getRedefinedCount() throws IllegalAccessException {
            this.redefinedCountField.setAccessible(true);
            return (int) this.redefinedCountField.get(annotationDataFromClass);
        }

        public void setRedefinedCount(int value) throws IllegalAccessException {
            this.redefinedCountField.setAccessible(true);
            this.redefinedCountField.set(annotationDataFromClass, value);
        }
    }

    /**
     * annotation动态代理创建新代理实例时使用的InvocationHandler实例
     */
    private static class AnnotationInvocationHandler implements InvocationHandler, Serializable {
        private static final long serialVersionUID = 6182022883658399397L;

        private final Class<? extends Annotation> type;

        private final Map<String, Object> memberValues;

        AnnotationInvocationHandler(Class<? extends Annotation> paramClass, Map<String, Object> paramMap) {
            Class[] arrayOfClass = paramClass.getInterfaces();
            if (!paramClass.isAnnotation() || arrayOfClass.length != 1 || arrayOfClass[0] != Annotation.class)
                throw new AnnotationFormatError("Attempt to create proxy for a non-annotation type.");
            this.type = paramClass;
            this.memberValues = paramMap;
        }

        public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) {
            String str = paramMethod.getName();
            Class[] arrayOfClass = paramMethod.getParameterTypes();
            if (str.equals("equals") && arrayOfClass.length == 1 && arrayOfClass[0] == Object.class)
                return equalsImpl(paramArrayOfObject[0]);
            if (arrayOfClass.length != 0)
                throw new AssertionError("Too many parameters for an annotation method");
            switch (str) {
                case "toString":
                    return toStringImpl();
                case "hashCode":
                    return Integer.valueOf(hashCodeImpl());
                case "annotationType":
                    return this.type;
            }
            Object object = this.memberValues.get(str);
            if (object == null)
                throw new IncompleteAnnotationException(this.type, str);
            if (object instanceof ExceptionProxy)
                throw new RuntimeException((Throwable) object);
            if (object.getClass().isArray() && Array.getLength(object) != 0)
                object = cloneArray(object);
            return object;
        }

        private Object cloneArray(Object paramObject) {
            Class<?> clazz = paramObject.getClass();
            if (clazz == byte[].class) {
                byte[] arrayOfByte = (byte[])paramObject;
                return arrayOfByte.clone();
            }
            if (clazz == char[].class) {
                char[] arrayOfChar = (char[])paramObject;
                return arrayOfChar.clone();
            }
            if (clazz == double[].class) {
                double[] arrayOfDouble = (double[])paramObject;
                return arrayOfDouble.clone();
            }
            if (clazz == float[].class) {
                float[] arrayOfFloat = (float[])paramObject;
                return arrayOfFloat.clone();
            }
            if (clazz == int[].class) {
                int[] arrayOfInt = (int[])paramObject;
                return arrayOfInt.clone();
            }
            if (clazz == long[].class) {
                long[] arrayOfLong = (long[])paramObject;
                return arrayOfLong.clone();
            }
            if (clazz == short[].class) {
                short[] arrayOfShort = (short[])paramObject;
                return arrayOfShort.clone();
            }
            if (clazz == boolean[].class) {
                boolean[] arrayOfBoolean = (boolean[])paramObject;
                return arrayOfBoolean.clone();
            }
            Object[] arrayOfObject = (Object[])paramObject;
            return arrayOfObject.clone();
        }

        private String toStringImpl() {
            StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append('@');
            stringBuilder.append(this.type.getName());
            stringBuilder.append('(');
            boolean bool = true;
            for (Map.Entry<String, Object> entry : this.memberValues.entrySet()) {
                if (bool) {
                    bool = false;
                } else {
                    stringBuilder.append(", ");
                }
                stringBuilder.append((String)entry.getKey());
                stringBuilder.append('=');
                stringBuilder.append(memberValueToString(entry.getValue()));
            }
            stringBuilder.append(')');
            return stringBuilder.toString();
        }

        private static String memberValueToString(Object paramObject) {
            Class<?> clazz = paramObject.getClass();
            if (!clazz.isArray())
                return paramObject.toString();
            if (clazz == byte[].class)
                return Arrays.toString((byte[])paramObject);
            if (clazz == char[].class)
                return Arrays.toString((char[])paramObject);
            if (clazz == double[].class)
                return Arrays.toString((double[])paramObject);
            if (clazz == float[].class)
                return Arrays.toString((float[])paramObject);
            if (clazz == int[].class)
                return Arrays.toString((int[])paramObject);
            if (clazz == long[].class)
                return Arrays.toString((long[])paramObject);
            if (clazz == short[].class)
                return Arrays.toString((short[])paramObject);
            if (clazz == boolean[].class)
                return Arrays.toString((boolean[])paramObject);
            return Arrays.toString((Object[])paramObject);
        }

        private Boolean equalsImpl(Object paramObject) {
            if (paramObject == this)
                return Boolean.valueOf(true);
            if (!this.type.isInstance(paramObject))
                return Boolean.valueOf(false);
            for (Method method : getMemberMethods()) {
                String str = method.getName();
                Object object1 = this.memberValues.get(str);
                Object object2 = null;
                AnnotationInvocationHandler annotationInvocationHandler = asOneOfUs(paramObject);
                if (annotationInvocationHandler != null) {
                    object2 = annotationInvocationHandler.memberValues.get(str);
                } else {
                    try {
                        object2 = method.invoke(paramObject, new Object[0]);
                    } catch (InvocationTargetException invocationTargetException) {
                        return Boolean.valueOf(false);
                    } catch (IllegalAccessException illegalAccessException) {
                        throw new AssertionError(illegalAccessException);
                    }
                }
                if (!memberValueEquals(object1, object2))
                    return Boolean.valueOf(false);
            }
            return Boolean.valueOf(true);
        }

        private AnnotationInvocationHandler asOneOfUs(Object paramObject) {
            if (Proxy.isProxyClass(paramObject.getClass())) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(paramObject);
                if (invocationHandler instanceof AnnotationInvocationHandler)
                    return (AnnotationInvocationHandler)invocationHandler;
            }
            return null;
        }

        private static boolean memberValueEquals(Object paramObject1, Object paramObject2) {
            Class<?> clazz = paramObject1.getClass();
            if (!clazz.isArray())
                return paramObject1.equals(paramObject2);
            if (paramObject1 instanceof Object[] && paramObject2 instanceof Object[])
                return Arrays.equals((Object[])paramObject1, (Object[])paramObject2);
            if (paramObject2.getClass() != clazz)
                return false;
            if (clazz == byte[].class)
                return Arrays.equals((byte[])paramObject1, (byte[])paramObject2);
            if (clazz == char[].class)
                return Arrays.equals((char[])paramObject1, (char[])paramObject2);
            if (clazz == double[].class)
                return Arrays.equals((double[])paramObject1, (double[])paramObject2);
            if (clazz == float[].class)
                return Arrays.equals((float[])paramObject1, (float[])paramObject2);
            if (clazz == int[].class)
                return Arrays.equals((int[])paramObject1, (int[])paramObject2);
            if (clazz == long[].class)
                return Arrays.equals((long[])paramObject1, (long[])paramObject2);
            if (clazz == short[].class)
                return Arrays.equals((short[])paramObject1, (short[])paramObject2);
            assert clazz == boolean[].class;
            return Arrays.equals((boolean[])paramObject1, (boolean[])paramObject2);
        }

        private Method[] getMemberMethods() {
            if (this.memberMethods == null)
                this.memberMethods = AccessController.<Method[]>doPrivileged((PrivilegedAction)new PrivilegedAction<Method[]>() {
                    public Method[] run() {
                        Method[] arrayOfMethod = AnnotationInvocationHandler.this.type.getDeclaredMethods();
                        AnnotationInvocationHandler.this.validateAnnotationMethods(arrayOfMethod);
                        AccessibleObject.setAccessible((AccessibleObject[])arrayOfMethod, true);
                        return arrayOfMethod;
                    }
                });
            return this.memberMethods;
        }

        private volatile transient Method[] memberMethods = null;

        private void validateAnnotationMethods(Method[] paramArrayOfMethod) {
            boolean bool = true;
            for (Method method : paramArrayOfMethod) {
                if (method.getModifiers() != 1025 || method
                        .isDefault() || method
                        .getParameterCount() != 0 || (method
                        .getExceptionTypes()).length != 0) {
                    bool = false;
                    break;
                }
                Class<?> clazz = method.getReturnType();
                if (clazz.isArray()) {
                    clazz = clazz.getComponentType();
                    if (clazz.isArray()) {
                        bool = false;
                        break;
                    }
                }
                if ((!clazz.isPrimitive() || clazz == void.class) && clazz != String.class && clazz != Class.class &&

                        !clazz.isEnum() &&
                        !clazz.isAnnotation()) {
                    bool = false;
                    break;
                }
                String str = method.getName();
                if ((str.equals("toString") && clazz == String.class) || (str
                        .equals("hashCode") && clazz == int.class) || (str
                        .equals("annotationType") && clazz == Class.class)) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                return;
            throw new AnnotationFormatError("Malformed method on an annotation type");
        }

        private int hashCodeImpl() {
            int i = 0;
            for (Map.Entry<String, Object> entry : this.memberValues.entrySet())
                i += 127 * ((String)entry.getKey()).hashCode() ^
                        memberValueHashCode(entry.getValue());
            return i;
        }

        private static int memberValueHashCode(Object paramObject) {
            Class<?> clazz = paramObject.getClass();
            if (!clazz.isArray())
                return paramObject.hashCode();
            if (clazz == byte[].class)
                return Arrays.hashCode((byte[])paramObject);
            if (clazz == char[].class)
                return Arrays.hashCode((char[])paramObject);
            if (clazz == double[].class)
                return Arrays.hashCode((double[])paramObject);
            if (clazz == float[].class)
                return Arrays.hashCode((float[])paramObject);
            if (clazz == int[].class)
                return Arrays.hashCode((int[])paramObject);
            if (clazz == long[].class)
                return Arrays.hashCode((long[])paramObject);
            if (clazz == short[].class)
                return Arrays.hashCode((short[])paramObject);
            if (clazz == boolean[].class)
                return Arrays.hashCode((boolean[])paramObject);
            return Arrays.hashCode((Object[])paramObject);
        }

        private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
            ObjectInputStream.GetField getField = paramObjectInputStream.readFields();
            Class<? extends Annotation> clazz = (Class)getField.get("type", (Object)null);
            Map map = (Map)getField.get("memberValues", (Object)null);
            AnnotationType annotationType = null;
            try {
                annotationType = AnnotationType.getInstance(clazz);
            } catch (IllegalArgumentException illegalArgumentException) {
                throw new InvalidObjectException("Non-annotation type in annotation serial stream");
            }
            Map<String, Class<?>> map1 = annotationType.memberTypes();
            LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
            for (Map.Entry entry : (Set<Map.Entry>)map.entrySet()) {
                String str = (String)entry.getKey();
                Object object = null;
                Class clazz1 = map1.get(str);
                if (clazz1 != null) {
                    object = entry.getValue();
                    if (!clazz1.isInstance(object) && !(object instanceof ExceptionProxy))
                        object = (new AnnotationTypeMismatchExceptionProxy(object.getClass() + "[" + object + "]")).setMember(annotationType
                                .members().get(str));
                }
                linkedHashMap.put(str, object);
            }
            UnsafeAccessor.setType(this, clazz);
            UnsafeAccessor.setMemberValues(this, (Map)linkedHashMap);
        }

        private static class UnsafeAccessor {
            private static final Unsafe unsafe;

            private static final long typeOffset;

            private static final long memberValuesOffset;

            static {
                try {
                    unsafe = Unsafe.getUnsafe();
                    typeOffset = unsafe.objectFieldOffset(AnnotationInvocationHandler.class.getDeclaredField("type"));
                    memberValuesOffset = unsafe.objectFieldOffset(AnnotationInvocationHandler.class.getDeclaredField("memberValues"));
                } catch (Exception exception) {
                    throw new ExceptionInInitializerError(exception);
                }
            }

            static void setType(AnnotationInvocationHandler param1AnnotationInvocationHandler, Class<? extends Annotation> param1Class) {
                unsafe.putObject(param1AnnotationInvocationHandler, typeOffset, param1Class);
            }

            static void setMemberValues(AnnotationInvocationHandler param1AnnotationInvocationHandler, Map<String, Object> param1Map) {
                unsafe.putObject(param1AnnotationInvocationHandler, memberValuesOffset, param1Map);
            }
        }
    }

}

class AnnotationTypeMismatchExceptionProxy extends ExceptionProxy {
    private static final long serialVersionUID = 7844069490309503934L;

    private Method member;

    private String foundType;

    AnnotationTypeMismatchExceptionProxy(String paramString) {
        this.foundType = paramString;
    }

    AnnotationTypeMismatchExceptionProxy setMember(Method paramMethod) {
        this.member = paramMethod;
        return this;
    }

    protected RuntimeException generateException() {
        return new AnnotationTypeMismatchException(this.member, this.foundType);
    }
}


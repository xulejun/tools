package com.xlj.tools.clickhouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

/**
 * @author legend xu
 * @date 2022/9/22
 */
public class ClickHouseColInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickHouseColInfo.class);

    private static HashMap<Class<?>, Class<?>> unboxingMap = new HashMap<>();
    private static HashMap<Class<?>, Object> defaultMap = new HashMap<>();

    private static final Class<?>[] UNBOXED_CLASS = {boolean.class,
            byte.class, char.class, short.class, int.class, long.class,
            float.class, double.class,};

    private static final Class<?>[] BOXED_CLASS = {Boolean.class, Byte.class,
            Character.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class,};

    private static final Object[] DEFAULT_VALUE = {Boolean.FALSE,
            Byte.valueOf((byte) 0), Character.valueOf((char) 0),
            Short.valueOf((short) 0), Integer.valueOf(0), Long.valueOf(0),
            Float.valueOf(0), Double.valueOf(0),};

    private static final int CLASS_NUM = UNBOXED_CLASS.length;

    static {
        for (int i = 0; i < CLASS_NUM; i++) {
            unboxingMap.put(BOXED_CLASS[i], UNBOXED_CLASS[i]);
            defaultMap.put(UNBOXED_CLASS[i], DEFAULT_VALUE[i]);
            defaultMap.put(BOXED_CLASS[i], DEFAULT_VALUE[i]);
        }
        defaultMap.put(String.class, "");
    }

    private static Object numberValue(Class<?> type, Object value) throws ReflectiveOperationException {
        return value.getClass().getMethod(type.getName() + "Value").invoke(value);
    }

    private Field field;
    private ClickHouseColumn annot;
    private String name;
    private Object defaultValue;
    private Constructor<?> constr;
    private boolean id;

    public ClickHouseColInfo(Field field, ClickHouseColumn annot) {
        field.setAccessible(true);
        this.field = field;
        this.annot = annot;
        name = annot.columnName();
        if (name.isEmpty()) {
            name = field.getName();
        }
        defaultValue = defaultMap.get(field.getType());
        if (annot.wrapperType().equals(Void.class)) {
            constr = null;
        } else {
            try {
                constr = annot.wrapperType().getConstructor(field.getType());
                constr.setAccessible(true);
            } catch (ReflectiveOperationException e) {
                LOGGER.warn("Error Wrapping " + this, e);
            }
        }
        id = (field.getAnnotation(ClickHouseId.class) != null);
    }

    public Object save(Object entity, Long now) throws ReflectiveOperationException {
        Object value;
        if (annot.isTimeStamp()) {
            field.set(entity, now);
            value = now;
        } else {
            value = field.get(entity);
        }
        if (value == null) {
            if (constr == null) {
                return defaultValue;
            }
            return constr.newInstance(defaultValue);
        }
        if (constr != null) {
            return constr.newInstance(value);
        }

        return value;
    }

    public Object load(Object value) throws ReflectiveOperationException {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            if (value == null) {
                return defaultValue;
            }
            if (type.equals(unboxingMap.get(value.getClass()))) {
                return value;
            }
            if (value instanceof Number) {
                if (type.equals(boolean.class) || type.equals(char.class)) {
                    return defaultValue;
                }
                return numberValue(type, value);
            }
            if (value instanceof Date && type.equals(long.class)) {
                return ((Date) value).getTime();
            }
            return defaultValue;
        }
        if (value == null || value.getClass().equals(type)) {
            return value;
        }
        if (Number.class.isAssignableFrom(type) && value instanceof Number) {
            if (type.equals(BigInteger.class)) {
                return BigInteger.valueOf(((Number) value).longValue());
            }
            if (type.equals(BigDecimal.class)) {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            }
            Class<?> primitiveType = unboxingMap.get(type);
            if (primitiveType == null) {
                return null;
            }
            return type.getDeclaredMethod("valueOf", primitiveType).invoke(null, numberValue(primitiveType, value));
        }
        return null;
    }

    public void set(Object entity, Object value) {
        try {
            field.set(entity, value);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("Error Setting " + this, e);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isId() {
        return id;
    }

    public Field getField() {
        return field;
    }

    public ClickHouseColumn getAnnot() {
        return annot;
    }

    @Override
    public String toString() {
        return field.getClass().getName() + "." + field.getName() + "[" + name + "]";
    }
}

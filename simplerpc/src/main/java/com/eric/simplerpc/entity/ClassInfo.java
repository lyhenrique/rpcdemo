package com.eric.simplerpc.entity;

import java.io.Serializable;

/**
 * 消息传递的实体类
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 9138514188533114098L;

    //类名
    private String className;
    //方法名
    private String methodName;
    //参数类型
    private Class<?>[] types;
    //参数列表
    private Object[] objects;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}

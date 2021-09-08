package com.kosuke.utils;

/**
 * The TaskCategories Enum class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
public enum TaskCategories {
    MEETING("MEETING"),
    PROJECT("PROJECT"),
    SHOPPING("SHOPPING"),
    LESSON("LESSON"),
    OUTDOOR("OUTDOOR");
    private String value;

    TaskCategories(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

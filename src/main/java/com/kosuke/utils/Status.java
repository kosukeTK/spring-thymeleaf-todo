package com.kosuke.utils;

/**
 * The Status Enum class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
public enum Status {
    ACTIVE("ACTIVE"), PASSIVE("PASSIVE");
    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

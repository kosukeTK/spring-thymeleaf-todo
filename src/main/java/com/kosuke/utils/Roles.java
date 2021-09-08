package com.kosuke.utils;

/**
 * The Roles Enum class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
public enum Roles {
    ROLE_ADMIN(1), ROLE_USER(2);
    private int value;

    Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

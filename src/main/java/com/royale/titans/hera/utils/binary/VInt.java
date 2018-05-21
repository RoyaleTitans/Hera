package com.royale.titans.hera.utils.binary;

public class VInt {
    private final int value;
    private final int length;

    public VInt(int value, int length) {
        this.value = value;
        this.length = length;
    }

    public int getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }
}

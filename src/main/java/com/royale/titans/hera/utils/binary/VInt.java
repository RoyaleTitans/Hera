package com.royale.titans.hera.utils.binary;

public class VInt {
    private final int mValue;
    private final int mLength;

    public VInt(int value, int length) {
        mValue = value;
        mLength = length;
    }

    public int getValue() {
        return mValue;
    }

    public int getLength() {
        return mLength;
    }
}

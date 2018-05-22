package com.royale.titans.hera.definitions;

public class Asset {
    private final String mHash;
    private final String mName;

    protected Asset(String hash, String name) {
        mHash = hash;
        mName = name;
    }

    public String getHash() {
        return mHash;
    }

    public String getName() {
        return mName;
    }
}

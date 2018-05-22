package com.royale.titans.hera.logic;

import com.royale.titans.hera.utils.TagUtils;

public class Account {
    private long mHighId;
    private long mLowId;

    private long mAccountId;

    private String mToken;

    public Account() {
        int[] ids = TagUtils.tag2HighLow("#2PP");
        setAccountId(TagUtils.tag2Id("#2PP"));

        setHighId(ids[0]);
        setLowId(ids[1]);
        setToken("");
    }

    public long getHighId() {
        return mHighId;
    }

    public void setHighId(long highId) {
        mHighId = highId;
    }

    public long getLowId() {
        return mLowId;
    }

    public void setLowId(long lowId) {
        mLowId = lowId;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }
}

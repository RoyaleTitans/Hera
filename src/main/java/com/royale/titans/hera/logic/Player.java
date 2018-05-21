package com.royale.titans.hera.logic;

import com.royale.titans.hera.logic.slots.LogicClientAvatar;
import com.royale.titans.hera.logic.slots.LogicClientHome;
import com.royale.titans.hera.utils.TagUtils;

public class Player {

    public long highId;
    public long lowId;

    public long accountId;

    public String token;

    public String name;

    public int level;
    public int exp;

    public int gold;
    public int gems;

    public LogicClientAvatar avatar;
    public LogicClientHome home;

    public Player() {
        int[] ids = TagUtils.tag2HighLow("PUC28JQ");

        this.accountId = TagUtils.tag2Id("PUC28JQ");

        this.highId = ids[0];
        this.lowId = ids[1];
        this.token = "";
    }

    public void init() {
        this.avatar = new LogicClientAvatar();
        this.home = new LogicClientHome();
    }
}

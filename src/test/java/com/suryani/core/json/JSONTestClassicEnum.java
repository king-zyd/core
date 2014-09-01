package com.zyd.core.json;

/**
 * User: gary.zeng
 * Date: 2014/6/23
 */
public enum JSONTestClassicEnum {
    TestEnumOne(1, "Test Enum One"),
    TestEnumTwo(2, "Test Enum Two");

    private int level;
    private String name;

    private JSONTestClassicEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}

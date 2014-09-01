package com.zyd.core.json;

/**
 * User: gary.zeng
 * Date: 2014/6/23
 */
public enum JSONTestEnum {
    TestEnumOne(1, "Test Enum One"),
    TestEnumTwo(2, "Test Enum Two");

    private int level;
    private String name;

    private JSONTestEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return name;
    }

    public static JSONTestEnum parse(String value) {
        JSONTestEnum[] values = JSONTestEnum.values();
        for (JSONTestEnum ele : values) {
            if (ele.name.equalsIgnoreCase(value)) {
                return ele;
            }
        }
        return null;
    }
}

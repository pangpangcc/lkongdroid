package org.cryse.lkong.logic;

public class TimelineListType {
    public static final int TYPE_TIMELINE = 34;
    public static final int TYPE_MENTIONS = 35;
    public static String typeToRequestParam(int typeCode) {
        switch (typeCode) {
            case TYPE_TIMELINE:
                return "?mod=data&sars=index/";
            case TYPE_MENTIONS:
                return "?mod=data&sars=my/atme";
            default:
                throw new IllegalArgumentException("Unknown type.");
        }
    }

    public static String typeToTypeName(int type) {
        switch (type) {
            case TYPE_TIMELINE:
                return "Timeline";
            case TYPE_MENTIONS:
                return "Mentions";
            default:
                throw new IllegalArgumentException("Unknown type.");
        }
    }
}

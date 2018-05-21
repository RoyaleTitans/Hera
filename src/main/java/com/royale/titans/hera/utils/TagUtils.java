package com.royale.titans.hera.utils;

public class TagUtils {

    public static String id2Tag(long id) {
        String hashtag = "";

        long highInt = id >> 32;
        if (highInt > 255) return hashtag;

        long lowInt = id & 0xFFFFFFFF;

        id = (lowInt << 8) + highInt;
        while (id != 0) {
            long index = id % 14;
            hashtag = "0289PYLQGRJCUV".toCharArray()[(int) index] + hashtag;

            id /= 14;
        }

        hashtag = "#" + hashtag;

        return hashtag;
    }

    public static int[] tag2HighLow(String tag) {
        char[] tagArray = tag.replace("#", "").toUpperCase().toCharArray();

        long id = 0;

        for (char c : tagArray) {
            id *= 14;
            id += "0289PYLQGRJCUV".indexOf(c);
        }

        int high = (int) id % 256;
        int low = (int) (id - high) >> 8;

        return new int[]{high, low};
    }

    public static long tag2Id(String tag) {
        char[] tagArray = tag.replace("#", "").toUpperCase().toCharArray();

        long id = 0;

        for (char c : tagArray) {
            id *= 14;
            id += "0289PYLQGRJCUV".indexOf(c);
        }

        long high = (int) id % 256;
        long low = (int) (id - high) >> 8;

        return (high << 32) | (low & 0xFFFFFFFFL);
    }
}

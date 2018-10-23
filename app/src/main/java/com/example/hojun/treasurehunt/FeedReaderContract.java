package com.example.hojun.treasurehunt;

import android.provider.BaseColumns;

/**
 * Created by hojun on 2016-11-07.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        // new stuff
        public static final String COLUMN_NAME_SCORE = "score";
        /*public static final String COLUMN_NAME_TREASURE_ONE_LAT = "treasure one latitude";
        public static final String COLUMN_NAME_TREASURE_ONE_LONG = "treasure one longitude";
        public static final String COLUMN_NAME_TREASURE_TWO_LAT = "treasure two latitude";
        public static final String COLUMN_NAME_TREASURE_TWO_LONG = "treasure two longitude";
        public static final String COLUMN_NAME_TREASURE_THREE_LAT = "treasure three latitude";
        public static final String COLUMN_NAME_TREASURE_THREE_LONG = "treasure three longitude";
        public static final String COLUMN_NAME_TREASURE_FOUR_LAT = "treasure four latitude";
        public static final String COLUMN_NAME_TREASURE_FOUR_LONG = "treasure four longitude";
        public static final String COLUMN_NAME_TREASURE_FIVE_LAT = "treasure two latitude";
        public static final String COLUMN_NAME_TREASURE_FIVE_LONG = "treasure five longitude";
        public static final String COLUMN_NAME_TREASURE_SIX_LAT = "treasure two latitude";
        public static final String COLUMN_NAME_TREASURE_SIX_LONG = "treasure six longitude";
        public static final String COLUMN_NAME_TREASURE_SEVEN_LAT = "treasure two latitude";
        public static final String COLUMN_NAME_TREASURE_SEVEN_LONG = "treasure seven longitude";*/
    }
}
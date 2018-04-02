package com.cpxiao.puzzle2248.mode.extra;

/**
 * @author cpxiao on 2017/09/25.
 */

public final class Extra {


    public static final class Key {
        private static final String BEST_SCORE_FORMAT = "BEST_SCORE_FORMAT_%s";

        public static String getBestScoreKey(int mode) {
            return String.format(BEST_SCORE_FORMAT, mode);
        }
    }
}

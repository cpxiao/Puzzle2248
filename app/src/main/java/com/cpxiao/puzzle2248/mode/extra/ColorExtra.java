package com.cpxiao.puzzle2248.mode.extra;

import android.content.Context;
import android.graphics.Color;

import com.cpxiao.R;

/**
 * @author cpxiao on 2017/08/22.
 */

public final class ColorExtra {

    public static long getRandomNumber(final long maxNumber) {
        long result;
        double random = Math.random();
        if (random <= 0.5F) {
            result = 2;
        } else if (random <= 0.6F) {
            result = 4;
        } else if (random <= 0.7F) {
            result = 8;
        } else if (random <= 0.8F) {
            result = 16;
        } else if (random <= 0.9F) {
            result = 32;
        } else if (random <= 0.92F) {
            result = 64;
        } else if (random <= 0.94F) {
            result = 128;
        } else if (random <= 0.96F) {
            result = 256;
        } else if (random <= 0.98F) {
            result = 512;
        } else {
            result = 1024;
        }
        if (result > maxNumber / 2) {
            result = maxNumber / 2;
        }
        return result;
    }

    public static int getRandomColor(Context context, long number) {
        int index = 0;
        while (number > 2) {
            number = number >> 1;//右移
            index++;
        }
        String[] array = context.getResources().getStringArray(R.array.colorArray);
        return Color.parseColor(array[index % array.length]);
    }

    /**
     * 处理数值，将给定数转化为2的幂次方
     * 举例：
     * 4->4
     * 5->4
     * 6->4
     *
     * @param number number
     * @return 2^n
     */
    public static long manageNumber(long number) {
        long result = 2;
        while (result < number) {
            result = result << 1;//左移
        }
        if (result > number) {
            result = result >> 1;//右移
        }
        return result;
    }
}

package com.cpxiao.puzzle2248.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/08/22.
 */

public class Dot extends Sprite {

    private static final long K = 1024L;
    private static final long M = 1024L * 1024L;
    private static final long G = 1024L * 1024L * 1024L;
    private static final long T = 1024L * 1024L * 1024L * 1024L;

    private int indexX;
    private int indexY;

    private boolean isSelected = false;
    private long mNumber;
    private int mColor;

    private RectF mDrawRectF = new RectF();

    protected Dot(Build build) {
        super(build);
        this.indexX = build.indexX;
        this.indexY = build.indexY;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void reset(long number, int color) {
        setFrame(0);
        setNumber(number);
        setColor(color);
    }

    public void setNumber(long number) {
        mNumber = number;
    }

    public long getNumber() {
        return mNumber;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private float getR() {
        RectF rectF = getSpriteRectF();
        float maxR = 0.5F * Math.min(rectF.width(), rectF.height());
        long frame = getFrame();
        if (frame < 10) {
            return maxR * frame / 10;
        } else {
            return maxR;
        }
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        float r = getR();
        if (isSelected) {
            drawSelectedCircle(canvas, paint, 1.25F * r);
        }
        drawSmallCircle(canvas, paint, r);
        drawNumber(canvas, paint);
    }

    private void drawSelectedCircle(Canvas canvas, Paint paint, float r) {
        paint.setColor(mColor);
        paint.setAlpha(100);
        drawRoundCircle(canvas, paint, r);
    }

    private void drawSmallCircle(Canvas canvas, Paint paint, float r) {
        paint.setColor(mColor);
        paint.setAlpha(255);
        drawRoundCircle(canvas, paint, r);
    }

    private void drawRoundCircle(Canvas canvas, Paint paint, float r) {
        float cX = getCenterX();
        float cY = getCenterY();
        float rXY = 0.38F * getWidth();
        mDrawRectF.set(cX - r, cY - r, cX + r, cY + r);
        canvas.drawRoundRect(mDrawRectF, rXY, rXY, paint);
    }


    private void drawNumber(Canvas canvas, Paint paint) {
        String msg;
        if (mNumber < K) {
            msg = mNumber + "";
        } else if (mNumber < M) {
            msg = mNumber / K + "K";
        } else if (mNumber < G) {
            msg = mNumber / M + "M";
        } else if (mNumber < T) {
            msg = mNumber / G + "G";
        } else {
            msg = mNumber / T + "T";
        }
        float textSize;
        if (msg.length() == 1) {
            textSize = 0.6F * getWidth();
        } else if (msg.length() == 2) {
            textSize = 0.5F * getWidth();
        } else if (msg.length() == 3) {
            textSize = 0.4F * getWidth();
        } else {
            textSize = 0.35F * getWidth();
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(msg, getCenterX(), getCenterY() + 0.35F * textSize, paint);
    }


    public static class Build extends Sprite.Build {
        private int indexX;
        private int indexY;

        public Build setIndexX(int indexX) {
            this.indexX = indexX;
            return this;
        }

        public Build setIndexY(int indexY) {
            this.indexY = indexY;
            return this;
        }

        public Dot build() {
            return new Dot(this);
        }
    }
}

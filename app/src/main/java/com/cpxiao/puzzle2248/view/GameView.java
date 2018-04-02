package com.cpxiao.puzzle2248.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.ThreadUtils;
import com.cpxiao.gamelib.mode.common.SpriteControl;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
import com.cpxiao.puzzle2248.mode.Dot;
import com.cpxiao.puzzle2248.mode.extra.ColorExtra;
import com.cpxiao.puzzle2248.mode.extra.Extra;
import com.cpxiao.puzzle2248.mode.extra.GameMode;

import java.util.concurrent.CopyOnWriteArrayList;

import hugo.weaving.DebugLog;

import static com.cpxiao.puzzle2248.mode.extra.ColorExtra.getRandomColor;

/**
 * @author cpxiao on 2017/08/22.
 */

public class GameView extends BaseSurfaceViewFPS {

    private long mScore = 0;
    private long mBestScore = 0;
    private int mMode = GameMode.DEFAULT[0];
    private int mCountX = GameMode.DEFAULT[1];
    private int mCountY = GameMode.DEFAULT[2];


    private Dot[][] mDotArray;

    private CopyOnWriteArrayList<Dot> mSelectedDotList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Dot> mdddSelectedDotList = new CopyOnWriteArrayList<>();

    private Dot mLastDot = null;

    private boolean isGameOver = false;

    public GameView(Context context, int mode, int countX, int countY) {
        super(context);
        mMode = mode;
        mCountX = countX;
        mCountY = countY;
    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void timingLogic() {

    }

    private void showGameOverDialog() {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Dialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.game_over)
                        .setMessage(R.string.click_to_restart)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                initWidget();
                            }
                        })
                        .create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }

    /**
     * 判断是否game over
     *
     * @return true: game over; false: not game over
     */
    private boolean checkGameOver() {
        if (!isDotArrayLegal()) {
            return false;
        }
        for (int y = 0; y < mCountY; y++) {
            for (int x = 0; x < mCountX; x++) {
                Dot dot = mDotArray[y][x];
                //重新开始的时候，初始化可能还未赋值
                if (dot == null) {
                    return false;
                }
                //判断右边
                int xRight = x + 1;
                if (xRight < mCountX) {
                    Dot dotR = mDotArray[y][xRight];
                    if (dotR == null || dot.getNumber() == dotR.getNumber()) {
                        return false;
                    }
                }
                //判断下边
                int yBottom = y + 1;
                if (yBottom < mCountY) {
                    Dot dotB = mDotArray[yBottom][x];
                    if (dotB == null || dot.getNumber() == dotB.getNumber()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isDotArrayLegal() {
        return mDotArray != null && mDotArray.length == mCountY && mDotArray[0].length == mCountX;
    }

    @Override
    protected void initWidget() {
        isGameOver = false;
        mScore = 0;
        String key = Extra.Key.getBestScoreKey(mMode);
        mBestScore = PreferencesUtils.getLong(getContext(), key, 0);


        float paddingLR = 0.04F * mViewWidth;
        float paddingT = Resources.getSystem().getDisplayMetrics().density * 80;
        float dotWH = Math.min((mViewWidth - paddingLR * 2) / mCountX, (mViewHeight - 2 * paddingT) / mCountY);
        mDotArray = new Dot[mCountY][mCountX];
        for (int y = 0; y < mCountY; y++) {
            for (int x = 0; x < mCountX; x++) {
                float cX = paddingLR + (x + 0.5F) * dotWH;
                float cY = paddingT + (y + 0.5F) * dotWH;
                Dot dot = (Dot) new Dot.Build()
                        .setIndexX(x)
                        .setIndexY(y)
                        .setW(0.6F * dotWH)
                        .setH(0.6F * dotWH)
                        .centerTo(cX, cY)
                        .build();
                long number = ColorExtra.getRandomNumber();
                int color = ColorExtra.getRandomColor(getContext(), number);
                dot.reset(number, color);

                mDotArray[y][x] = dot;
            }
        }
    }

    @DebugLog
    @Override
    public void drawCache() {

        drawLinkLine(mCanvasCache, mPaint);

        drawDotArray(mCanvasCache, mPaint);

        drawScore(mCanvasCache, mPaint);

        drawBestScore(mCanvasCache, mPaint);

    }

    private void drawLinkLine(Canvas canvas, Paint paint) {
        if (mSelectedDotList.size() < 2) {
            return;
        }
        paint.setStrokeWidth(0.02F * mViewWidth);
        int color = mSelectedDotList.get(0).getColor();
        paint.setColor(color);

        for (int i = 0; i < mSelectedDotList.size() - 1; i++) {
            Dot dot0 = mSelectedDotList.get(i);
            Dot dot1 = mSelectedDotList.get(i + 1);
            canvas.drawLine(dot0.getCenterX(), dot0.getCenterY(), dot1.getCenterX(), dot1.getCenterY(), paint);
        }
    }

    private void drawDotArray(Canvas canvas, Paint paint) {
        if (!isDotArrayLegal()) {
            return;
        }
        for (int y = 0; y < mCountY; y++) {
            for (int x = 0; x < mCountX; x++) {
                Dot dot = mDotArray[y][x];
                dot.draw(canvas, paint);
            }
        }

    }


    private void drawScore(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        paint.setTextSize(0.05F * mViewHeight);
        String scoreMsg = "" + mScore;
        canvas.drawText(scoreMsg, 0.5F * mViewWidth, 0.08F * mViewHeight, paint);
    }

    private void drawBestScore(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(0.02F * mViewHeight);
        String scoreMsg = getContext().getString(R.string.best_score) + ": " + mBestScore;
        canvas.drawText(scoreMsg, 0.5F * mViewWidth, 0.12F * mViewHeight, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float eventX = event.getX();
        float eventY = event.getY();

        if (action == MotionEvent.ACTION_UP) {
            merge();
            mLastDot = null;
            for (Dot dot : mSelectedDotList) {
                dot.setSelected(false);
            }
            mSelectedDotList.clear();

            updateScoreAndBestScore(getContext());

            if (!isGameOver && checkGameOver()) {
                isGameOver = true;
                showGameOverDialog();
            }
            return true;
        }


        for (int y = 0; y < mCountY; y++) {
            for (int x = 0; x < mCountX; x++) {
                Dot dot = mDotArray[y][x];
                if (SpriteControl.isClicked(dot, eventX, eventY)) {
                    dot.setSelected(true);
                    if (mLastDot == null) {
                        mSelectedDotList.add(dot);
                        mLastDot = dot;
                    } else if (dot != mLastDot) {
                        //如果重复了，就删除重复点之后添加的。
                        if (mSelectedDotList.contains(dot)) {
                            int index = mSelectedDotList.indexOf(dot);
                            if (DEBUG) {
                                Log.d(TAG, "onTouchEvent: index = " + index);
                            }
                            if (DEBUG) {
                                Log.d(TAG, "onTouchEvent: size0 = " + mSelectedDotList.size());
                            }
                            int size = mSelectedDotList.size();
                            for (int i = index; i < size; i++) {
                                mSelectedDotList.remove(index);
                                if (DEBUG) {
                                    Log.d(TAG, "onTouchEvent: index00 = " + i);
                                }
                            }
                            if (DEBUG) {
                                Log.d(TAG, "onTouchEvent: size1 = " + mSelectedDotList.size());
                            }
                        }
                        //添加节点
                        mSelectedDotList.add(dot);
                        mLastDot = dot;
                        if (DEBUG) {
                            Log.d(TAG, "onTouchEvent: size2 = " + mSelectedDotList.size());
                        }
                    }
                    if (DEBUG) {
                        //                        Log.d(TAG, "onTouchEvent: mSelectedDotList.size() = " + mSelectedDotList.size());
                    }
                    return true;
                }
            }
        }


        return true;
        //        return super.onTouchEvent(event);
    }

    private void updateScoreAndBestScore(Context context) {
        String key = Extra.Key.getBestScoreKey(mMode);
        mBestScore = Math.max(mScore, mBestScore);
        PreferencesUtils.putLong(context, key, mBestScore);
    }

    private void merge() {
        if (!isCanBeMerged()) {
            return;
        }
        //处理最后一个Dot
        int size = mSelectedDotList.size();
        Dot lastDot = mSelectedDotList.get(size - 1);
        long score = lastDot.getNumber() * size;
        // 添加得分
        mScore += score;
        long mergeNumber = ColorExtra.manageNumber(score);
        lastDot.setNumber(mergeNumber);

        int mergeColor = getRandomColor(getContext(), mergeNumber);
        lastDot.setColor(mergeColor);

        //处理非最后一个Dot
        for (int i = 0; i < size - 1; i++) {
            Dot dot = mSelectedDotList.get(i);
            long number = ColorExtra.getRandomNumber();
            int color = getRandomColor(getContext(), number);
            dot.reset(number, color);
        }
    }

    /**
     * 检查是否可以合并。
     * 以下情况不能合并：
     * 1.小于2
     * 2.有重复添加
     * 3.数值不一样
     * 4.相邻两个节点处于斜线位置而非上下或者左右位置
     *
     * @return boolean
     */
    private boolean isCanBeMerged() {
        if (mSelectedDotList.size() < 2) {
            return false;
        }
        for (int i = 0; i < mSelectedDotList.size(); i++) {
            Dot dot = mSelectedDotList.get(i);
            for (int j = i; j < mSelectedDotList.size(); j++) {
                if (j > i && dot == mSelectedDotList.get(j)) {
                    //重复添加
                    return false;
                }
            }
        }

        //判断数值是否一样
        long value = mSelectedDotList.get(0).getNumber();
        for (Dot dot : mSelectedDotList) {
            if (value != dot.getNumber()) {
                return false;
            }
        }
        //判断相邻两个点是否处于上下或者左右位置，若为斜线位置则不能合并
        for (int i = 0; i < mSelectedDotList.size() - 1; i++) {
            Dot dot0 = mSelectedDotList.get(i);
            Dot dot1 = mSelectedDotList.get(i + 1);
            if (dot0.getIndexX() != dot1.getIndexX()
                    && dot0.getIndexY() != dot1.getIndexY()) {
                return false;
            }
        }
        return true;
    }

}

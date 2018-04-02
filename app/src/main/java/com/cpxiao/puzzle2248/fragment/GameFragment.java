package com.cpxiao.puzzle2248.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.puzzle2248.mode.extra.GameMode;
import com.cpxiao.puzzle2248.view.GameView;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/08/23.
 */

public class GameFragment extends BaseZAdsFragment {

    private int mMode = GameMode.DEFAULT[0];
    private int mCountX = GameMode.DEFAULT[1];
    private int mCountY = GameMode.DEFAULT[2];

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_GAME);

        Bundle bundle;
        bundle = getArguments();
        if (bundle != null) {
            mMode = bundle.getInt(GameMode.MODE, GameMode.DEFAULT[0]);
            mCountX = bundle.getInt(GameMode.MODE_X, GameMode.DEFAULT[1]);
            mCountY = bundle.getInt(GameMode.MODE_Y, GameMode.DEFAULT[2]);
        }
        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_game);
        GameView gameView = new GameView(view.getContext(), mMode, mCountX, mCountY);
        layout.addView(gameView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

}

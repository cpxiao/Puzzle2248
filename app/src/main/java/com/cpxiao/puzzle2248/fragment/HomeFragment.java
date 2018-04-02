package com.cpxiao.puzzle2248.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.RateAppUtils;
import com.cpxiao.androidutils.library.utils.ShareAppUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.puzzle2248.mode.extra.Extra;
import com.cpxiao.puzzle2248.mode.extra.GameMode;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/8/23.
 * @version cpxiao on 2017/9/25. 改为继承BaseZAdsFragment
 */

public class HomeFragment extends BaseZAdsFragment implements View.OnClickListener {

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_HOME);

        Button buttonPlay = (Button) view.findViewById(R.id.play);
        ImageView buttonRateApp = (ImageView) view.findViewById(R.id.rate_app);
        ImageButton buttonShare = (ImageButton) view.findViewById(R.id.share);
        ImageButton buttonBestScore = (ImageButton) view.findViewById(R.id.best_score);
        ImageButton buttonSettings = (ImageButton) view.findViewById(R.id.settings);

        buttonPlay.setOnClickListener(this);
        buttonRateApp.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonBestScore.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Context context = getHoldingActivity();

        Bundle bundle = new Bundle();
        if (id == R.id.play) {
            bundle.putInt(GameMode.MODE, GameMode.EASY[0]);
            bundle.putInt(GameMode.MODE_X, GameMode.EASY[1]);
            bundle.putInt(GameMode.MODE_Y, GameMode.EASY[2]);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.rate_app) {
            Toast.makeText(context, "Thanks for rating us.", Toast.LENGTH_SHORT).show();
            RateAppUtils.rate(context);
        } else if (id == R.id.share) {
            String msg = getString(R.string.share_msg) + "\n" +
                    getString(R.string.app_name) + "\n" +
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            ShareAppUtils.share(context, getString(R.string.share), msg);
        } else if (id == R.id.best_score) {
            showBestScoreDialog(context);
        } else if (id == R.id.settings) {
            Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBestScoreDialog(Context context) {
        String title = getString(R.string.best_score) +
                ":  " +
                PreferencesUtils.getLong(context, Extra.Key.getBestScoreKey(GameMode.EASY[0]), 0);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

}

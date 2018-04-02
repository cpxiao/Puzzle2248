package com.cpxiao.puzzle2248.fragment;

import android.os.Bundle;
import android.view.View;

import com.cpxiao.R;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;

/**
 * @author cpxiao on 2017/08/23.
 */

public class SettingsFragment extends BaseZAdsFragment {

    public static SettingsFragment newInstance(Bundle bundle) {
        SettingsFragment fragment = new SettingsFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

}

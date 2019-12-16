package com.nixsol.mahesh.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.nixsol.mahesh.R;
import com.nixsol.mahesh.common.ui.BaseActivity;
import com.nixsol.mahesh.ui.home.fragments.HomeFragment;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    private String TAG = getClass().getName();
    private Context mContext;

    @BindView(R.id.container)
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initUI();

    }

    private void initUI() {
        getSupportFragmentManager().beginTransaction().add(R.id.container, HomeFragment.newInstance()).commit();
    }

}

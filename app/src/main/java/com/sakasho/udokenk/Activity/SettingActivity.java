package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.sakasho.udokenk.Event.SettingFinishEvent;
import com.sakasho.udokenk.Fragment.SettingFragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class SettingActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingFragment settingFragment = new SettingFragment();
        fragmentTransaction.add(R.id.setting_fragment_container, settingFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(SettingFinishEvent settingFinishEvent) {
        finish();
        Intent intent = new Intent(this, TopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

}

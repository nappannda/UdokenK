package com.sakasho.udokenk.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.sakasho.udokenk.Event.SelectMenuEvent;
import com.sakasho.udokenk.Fragment.TopFragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;


public class TopActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TopFragment topFragment = new TopFragment();
        fragmentTransaction.add(R.id.top_fragment_container, topFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(SelectMenuEvent event) {
        switch (event.getMenu()) {
            case 1:
                finish();
                Intent awase_intent = new Intent(this, AwaseActivity.class);
                awase_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(awase_intent);
                break;
            case 2:
                finish();
                Intent siru_intent = new Intent(this, SiruActivity.class);
                siru_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(siru_intent);
                break;
            case 3:
                finish();
                Intent mitibiki_intent = new Intent(this, MitibikiActivity.class);
                mitibiki_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mitibiki_intent);
                break;
            case 4:
                finish();
                Intent moyori_intent = new Intent(this, MoyoriActivity.class);
                moyori_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(moyori_intent);
                break;
            case 5:
                finish();
                Intent setting_intent = new Intent(this, SettingActivity.class);
                setting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(setting_intent);
                break;
        }
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

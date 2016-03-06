package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sakasho.udokenk.Event.Animation1ClickEvent;
import com.sakasho.udokenk.Event.Animation2ClickEvent;
import com.sakasho.udokenk.Fragment.Animation1Fragment;
import com.sakasho.udokenk.Fragment.Animation2Fragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class AnimationActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Animation1Fragment animation1Fragment = new Animation1Fragment();
        fragmentTransaction.add(R.id.animation_fragment_container, animation1Fragment);
        fragmentTransaction.commit();
    }

    public void onEvent(Animation1ClickEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Animation2Fragment animation2Fragment = new Animation2Fragment();
        fragmentTransaction.replace(R.id.animation_fragment_container, animation2Fragment);
        fragmentTransaction.commit();
    }

    public void onEvent(Animation2ClickEvent animation2ClickEvent) {
        if (sp.getBoolean("isSetting", false)) {//設定が終わってるときトップ画面へ
            finish();
            Intent intent = new Intent(this, TopActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {//設定が終わってないとき設定画面へ
            finish();
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

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

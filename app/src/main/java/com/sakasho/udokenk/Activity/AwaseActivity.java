package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.sakasho.udokenk.Event.AwaseDescriptionFinishEvent;
import com.sakasho.udokenk.Event.AwaseSelectOkazuFinishEvent;
import com.sakasho.udokenk.Event.NextTopEvent;
import com.sakasho.udokenk.Fragment.AwaseDescriptionFragment;
import com.sakasho.udokenk.Fragment.AwaseResultFragment;
import com.sakasho.udokenk.Fragment.AwaseSelectOkazuFragment;
import com.sakasho.udokenk.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class AwaseActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int udons[] = new int[3];//うどんのデータ保持
    private String okazus[][] = new String[3][3];

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            nextTopStart();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.awase_toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AwaseDescriptionFragment awaseDescriptionFragment = new AwaseDescriptionFragment();
        fragmentTransaction.add(R.id.awase_fragment_container, awaseDescriptionFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(AwaseDescriptionFinishEvent event) {
        SetSelectOkazuFragment(1);
    }

    public void onEvent(AwaseSelectOkazuFinishEvent event) {
        int count = event.getCount();
        udons[count - 1] = event.getUdon();
        okazus[count - 1] = event.getOkazus();
        if (count == 3) {
            //結果画面への遷移
            Bundle bundle = new Bundle();
            bundle.putIntArray("awase_udons", udons);
            bundle.putStringArray("awase_okazu1", okazus[0]);
            bundle.putStringArray("awase_okazu2", okazus[1]);
            bundle.putStringArray("awase_okazu3", okazus[2]);

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AwaseResultFragment awaseResultFragment = new AwaseResultFragment();
            awaseResultFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.awase_fragment_container, awaseResultFragment);
            fragmentTransaction.commit();
        } else {
            SetSelectOkazuFragment(count + 1);
        }
    }

    public void onEvent(NextTopEvent event){
        nextTopStart();
    }


    private void SetSelectOkazuFragment(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("count", i);//i回目の処理ということを表す
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AwaseSelectOkazuFragment awaseSelectOkazuFragment = new AwaseSelectOkazuFragment();
        awaseSelectOkazuFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.awase_fragment_container, awaseSelectOkazuFragment);
        fragmentTransaction.commit();
    }

    private void nextTopStart() {
        finish();
        Intent top_intent = new Intent(this, TopActivity.class);
        top_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(top_intent);
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

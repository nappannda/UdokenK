package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sakasho.udokenk.Event.MapSettingEvent;
import com.sakasho.udokenk.Event.MoyoriUdonFinishEvent;
import com.sakasho.udokenk.Event.SearchCompleteEvent;
import com.sakasho.udokenk.Fragment.MoyoriSearchSettingFragment;
import com.sakasho.udokenk.Fragment.MoyoriUdonMapFragment;
import com.sakasho.udokenk.Fragment.TopFragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

/**
 * Created by nappannda on 2016/02/04.
 */
public class MoyoriActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            nextTopStart();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moyori);
        Toolbar toolbar = (Toolbar) findViewById(R.id.moyori_toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MoyoriSearchSettingFragment moyoriSearchSettingFragment = new MoyoriSearchSettingFragment();
        fragmentTransaction.add(R.id.moyori_fragment_container, moyoriSearchSettingFragment);
        fragmentTransaction.commit();
    }


    public void onEvent(SearchCompleteEvent event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("UdonShop", event.getUdonShopEntity());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MoyoriUdonMapFragment moyoriUdonMapFragment = new MoyoriUdonMapFragment();
        moyoriUdonMapFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.moyori_fragment_container, moyoriUdonMapFragment);
        fragmentTransaction.commit();
        //EventBus.getDefault().post(new MapSettingEvent(event.getUdonShopEntity()));
    }

    public void onEvent(MoyoriUdonFinishEvent event) {
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

    private void nextTopStart() {
        finish();
        Intent top_intent = new Intent(this, TopActivity.class);
        top_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(top_intent);
    }
}

package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.sakasho.udokenk.Event.NextTopEvent;
import com.sakasho.udokenk.Event.SiruDescriptionFinishEvent;
import com.sakasho.udokenk.Event.SiruToppingSelectFinishEvent;
import com.sakasho.udokenk.Event.SiruUdonSelectFinishEvent;
import com.sakasho.udokenk.Fragment.AwaseDescriptionFragment;
import com.sakasho.udokenk.Fragment.SiruDescriptionFragment;
import com.sakasho.udokenk.Fragment.SiruResultFragment;
import com.sakasho.udokenk.Fragment.SiruSelectToppingFragment;
import com.sakasho.udokenk.Fragment.UdonSelectFragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class SiruActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_siru);
        Toolbar toolbar = (Toolbar) findViewById(R.id.siru_toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SiruDescriptionFragment siruDescriptionFragment = new SiruDescriptionFragment();
        fragmentTransaction.add(R.id.siru_fragment_container, siruDescriptionFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(SiruDescriptionFinishEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UdonSelectFragment udonSelectFragment = new UdonSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);//0なら知るうどんの処理
        udonSelectFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.siru_fragment_container, udonSelectFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(SiruUdonSelectFinishEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SiruSelectToppingFragment siruSelectToppingFragment = new SiruSelectToppingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("size", event.getSize());//うどんのサイズ
        bundle.putInt("kanban", event.getKanban());//何うどんか
        siruSelectToppingFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.siru_fragment_container, siruSelectToppingFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(SiruToppingSelectFinishEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SiruResultFragment siruResultFragment = new SiruResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("size", event.getSize());
        bundle.putInt("kanban", event.getKanban());
        bundle.putIntegerArrayList("topping", event.getTopping());
        siruResultFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.siru_fragment_container, siruResultFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(NextTopEvent event) {
        nextTopStart();
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

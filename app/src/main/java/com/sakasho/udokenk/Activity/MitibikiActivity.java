package com.sakasho.udokenk.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.sakasho.udokenk.Event.MitibikiDescriptionFinishEvent;
import com.sakasho.udokenk.Event.MitibikiUdonSelectFinishEvent;
import com.sakasho.udokenk.Event.NextTopEvent;
import com.sakasho.udokenk.Fragment.MitibikiDescriptionFragment;
import com.sakasho.udokenk.Fragment.MitibikiRecommendFragment;
import com.sakasho.udokenk.Fragment.MoyoriSearchSettingFragment;
import com.sakasho.udokenk.Fragment.UdonSelectFragment;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class MitibikiActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_mitibiki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mitibiki_toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MitibikiDescriptionFragment mitibikiDescriptionFragment = new MitibikiDescriptionFragment();
        fragmentTransaction.add(R.id.mitibiki_fragment_container, mitibikiDescriptionFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(MitibikiDescriptionFinishEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UdonSelectFragment udonSelectFragment = new UdonSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);//1なら導きうどんの処理
        udonSelectFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mitibiki_fragment_container, udonSelectFragment);
        fragmentTransaction.commit();
    }

    public void onEvent(MitibikiUdonSelectFinishEvent event) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MitibikiRecommendFragment mitibikiRecommendFragment = new MitibikiRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("size", event.getSize());
        bundle.putInt("udon", event.getKanban());
        mitibikiRecommendFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mitibiki_fragment_container, mitibikiRecommendFragment);
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

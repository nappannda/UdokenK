package com.sakasho.udokenk.Fragment;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sakasho.udokenk.Event.SelectMenuEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class TopFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView awase_image, siru_image, mitibiki_image, moyori_image;
    private ImageView setting_image;
    private ImageView maru_image;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sp;
    private int point;
    private int udonmaru_drawable[][] = new int[7][2];
    private int count = 0;
    private boolean click = false;

    public TopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        awase_image = (ImageView) view.findViewById(R.id.top_awase_image);
        awase_image.setOnClickListener(this);
        siru_image = (ImageView) view.findViewById(R.id.top_siru_image);
        siru_image.setOnClickListener(this);
        mitibiki_image = (ImageView) view.findViewById(R.id.top_mitibiki_image);
        mitibiki_image.setOnClickListener(this);
        moyori_image = (ImageView) view.findViewById(R.id.top_moyori_image);
        moyori_image.setOnClickListener(this);
        setting_image = (ImageView) view.findViewById(R.id.top_setting_image);
        setting_image.setOnClickListener(this);
        maru_image = (ImageView) view.findViewById(R.id.top_udonmaru_image);
        maru_image.setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.taiko);
        point = sp.getInt("awase_point", -1);
        setImageId();
        maru_image.setImageResource(udonmaru_drawable[point + 1][0]);//ポイントに応じてうどん丸の画像変更
        return view;
    }

    private void setImageId() {
        udonmaru_drawable[0][0] = R.drawable.top_tukimi_maru;
        udonmaru_drawable[1][0] = R.drawable.top_tikara_maru;
        udonmaru_drawable[2][0] = R.drawable.top_wakame_maru;
        udonmaru_drawable[3][0] = R.drawable.top_kare_maru;
        udonmaru_drawable[4][0] = R.drawable.top_kitune_maru;
        udonmaru_drawable[5][0] = R.drawable.top_niku_maru;
        udonmaru_drawable[6][0] = R.drawable.top_tenpura_maru;
        udonmaru_drawable[0][1] = R.drawable.top_tukimi_maru_on;
        udonmaru_drawable[1][1] = R.drawable.top_tikara_maru_on;
        udonmaru_drawable[2][1] = R.drawable.top_wakame_maru_on;
        udonmaru_drawable[3][1] = R.drawable.top_kare_maru_on;
        udonmaru_drawable[4][1] = R.drawable.top_kitune_maru_on;
        udonmaru_drawable[5][1] = R.drawable.top_niku_maru_on;
        udonmaru_drawable[6][1] = R.drawable.top_tenpura_maru_on;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_awase_image:
                TaikoSound();
                EventBus.getDefault().post(new SelectMenuEvent(1));
                break;
            case R.id.top_siru_image:
                TaikoSound();
                EventBus.getDefault().post(new SelectMenuEvent(2));
                break;
            case R.id.top_mitibiki_image:
                TaikoSound();
                EventBus.getDefault().post(new SelectMenuEvent(3));
                break;
            case R.id.top_moyori_image:
                TaikoSound();
                EventBus.getDefault().post(new SelectMenuEvent(4));
                break;
            case R.id.top_setting_image:
                EventBus.getDefault().post(new SelectMenuEvent(5));
                break;
            case R.id.top_udonmaru_image:
                if (!click) {
                    count++;
                    TaikoSound();
                    maru_image.setImageResource(udonmaru_drawable[point + 1][1]);
                    if (count == 100) {
                        maru_image.setImageResource(udonmaru_drawable[6][1]);
                        count = 0;
                    }
                    handler.postDelayed(maruImageTask, 250);
                    click = true;
                }
                break;
        }
    }

    private final Handler handler = new Handler();
    private final Runnable maruImageTask = new Runnable() {
        public void run() {
            if (count == 0) {
                maru_image.setImageResource(udonmaru_drawable[6][0]);
            } else {
                maru_image.setImageResource(udonmaru_drawable[point + 1][0]);
            }
            click = false;
        }
    };

    private void TaikoSound() {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.taiko);
        mediaPlayer.start();
    }


}

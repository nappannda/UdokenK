package com.sakasho.udokenk.Fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sakasho.udokenk.Event.SettingFinishEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

/**
 * A placehageer fragment containing a simple view.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private SharedPreferences sp;
    private ImageView man_image, woman_image;
    private int BUTTON_SIZE = 6;
    private Button[] age_buttons = new Button[BUTTON_SIZE];//歳のボタン6つを管理
    private int[][] age_buttons_drwable = new int[BUTTON_SIZE][2];//ボタンがオンオフの場合の画像を管理

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        man_image = (ImageView) view.findViewById(R.id.setting_gender_man_image);
        man_image.setOnClickListener(this);
        woman_image = (ImageView) view.findViewById(R.id.setting_gender_woman_image);
        woman_image.setOnClickListener(this);
        age_buttons[0] = (Button) view.findViewById(R.id.setting_age1_btn);
        age_buttons[1] = (Button) view.findViewById(R.id.setting_age2_btn);
        age_buttons[2] = (Button) view.findViewById(R.id.setting_age3_btn);
        age_buttons[3] = (Button) view.findViewById(R.id.setting_age4_btn);
        age_buttons[4] = (Button) view.findViewById(R.id.setting_age5_btn);
        age_buttons[5] = (Button) view.findViewById(R.id.setting_age6_btn);
        age_buttons_drwable[0][0] = R.drawable.setting_age1;
        age_buttons_drwable[1][0] = R.drawable.setting_age2;
        age_buttons_drwable[2][0] = R.drawable.setting_age3;
        age_buttons_drwable[3][0] = R.drawable.setting_age4;
        age_buttons_drwable[4][0] = R.drawable.setting_age5;
        age_buttons_drwable[5][0] = R.drawable.setting_age6;
        age_buttons_drwable[0][1] = R.drawable.setting_age1_on;
        age_buttons_drwable[1][1] = R.drawable.setting_age2_on;
        age_buttons_drwable[2][1] = R.drawable.setting_age3_on;
        age_buttons_drwable[3][1] = R.drawable.setting_age4_on;
        age_buttons_drwable[4][1] = R.drawable.setting_age5_on;
        age_buttons_drwable[5][1] = R.drawable.setting_age6_on;
        for (int i = 0; i < BUTTON_SIZE; i++) {
            age_buttons[i].setOnClickListener(this);
        }
        age_buttons[sp.getInt("setting_age", 1) - 1].setBackgroundResource(age_buttons_drwable[sp.getInt("setting_age", 1) - 1][1]);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_age1_btn:
                OldClick(1);
                break;
            case R.id.setting_age2_btn:
                OldClick(2);
                break;
            case R.id.setting_age3_btn:
                OldClick(3);
                break;
            case R.id.setting_age4_btn:
                OldClick(4);
                break;
            case R.id.setting_age5_btn:
                OldClick(5);
                break;
            case R.id.setting_age6_btn:
                OldClick(6);
                break;
            case R.id.setting_gender_man_image:
                GenderClick(true);
                break;
            case R.id.setting_gender_woman_image:
                GenderClick(false);
                break;
        }
    }

    /*
        男性がtrue,女性がfalseと置きそれを保存する
     */
    private void GenderClick(boolean gender) {
        if (gender) {
            man_image.setImageResource(R.drawable.setting_gender_man_on);
        } else {
            woman_image.setImageResource(R.drawable.setting_gender_woman_on);
        }
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("setting_gender", gender);
        e.putBoolean("isSetting", true);//セッティングが終了していることを表す
        e.commit();

        EventBus.getDefault().post(new SettingFinishEvent());
    }

    private void OldClick(int t) {
        OldBtnReset();
        age_buttons[t - 1].setBackgroundResource(age_buttons_drwable[t - 1][1]);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("setting_age", t);
        e.commit();
    }

    /*
        歳の画像をすべてOffの画像にする
     */
    private void OldBtnReset() {
        for (int i = 0; i < BUTTON_SIZE; i++) {
            age_buttons[i].setBackgroundResource(age_buttons_drwable[i][0]);
        }
    }
}

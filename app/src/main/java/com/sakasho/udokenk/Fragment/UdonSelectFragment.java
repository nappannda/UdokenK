package com.sakasho.udokenk.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sakasho.udokenk.Event.MitibikiUdonSelectFinishEvent;
import com.sakasho.udokenk.Event.MoyoriUdonFinishEvent;
import com.sakasho.udokenk.Event.SiruUdonSelectFinishEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class UdonSelectFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView big_image, middle_image, small_image;
    private ImageView[] kanban_image = new ImageView[12];//看板のImageViewを管理
    private int now_kanban = 0;//今選択している看板
    private int now_size = 0;//今選択している大きさ 1がbig,2がmiddle,3がsmall
    private ImageView udon_image;
    private int[][] udon_resources = new int[12][3];
    private int id;//IDが0なら 1なら導きうどん
    private ImageView next_image;
    private LinearLayout udon_linear;

    public UdonSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_udon_select, container, false);
        id = getArguments().getInt("id");
        big_image = (ImageView) view.findViewById(R.id.udon_big_image);
        big_image.setOnClickListener(this);
        middle_image = (ImageView) view.findViewById(R.id.udon_middle_image);
        middle_image.setOnClickListener(this);
        small_image = (ImageView) view.findViewById(R.id.udon_small_image);
        small_image.setOnClickListener(this);
        settingKanban();
        udon_image = (ImageView) view.findViewById(R.id.udon_image);
        next_image = (ImageView) view.findViewById(R.id.udon_next_image);
        next_image.setOnClickListener(this);
        settingUdonResources();
        udon_linear = (LinearLayout) view.findViewById(R.id.udon_linear);
        if (id == 1) {
            udon_linear.setBackgroundResource(R.drawable.mitibiki_background);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.udon_big_image:
                OffSelectSizeImage();
                big_image.setImageResource(R.drawable.udon_big_image_on);
                now_size = 1;
                changeUdon();
                break;
            case R.id.udon_middle_image:
                OffSelectSizeImage();
                middle_image.setImageResource(R.drawable.udon_middle_image_on);
                now_size = 2;
                changeUdon();
                break;
            case R.id.udon_small_image:
                OffSelectSizeImage();
                small_image.setImageResource(R.drawable.udon_small_image_on);
                now_size = 3;
                changeUdon();
                break;
            case R.id.udon_kanban1:
                OffSelectUdonImage();
                kanban_image[0].setImageResource(R.drawable.udon_kake_kanban_on);
                now_kanban = 1;
                changeUdon();
                break;
            case R.id.udon_kanban2:
                OffSelectUdonImage();
                kanban_image[1].setImageResource(R.drawable.udon_kare_kanban_on);
                now_kanban = 2;
                changeUdon();
                break;
            case R.id.udon_kanban3:
                OffSelectUdonImage();
                kanban_image[2].setImageResource(R.drawable.udon_kitune_kanban_on);
                now_kanban = 3;
                changeUdon();
                break;
            case R.id.udon_kanban4:
                OffSelectUdonImage();
                kanban_image[3].setImageResource(R.drawable.udon_zaru_kanban_on);
                now_kanban = 4;
                changeUdon();
                break;
            case R.id.udon_kanban5:
                OffSelectUdonImage();
                kanban_image[4].setImageResource(R.drawable.udon_riki_kanban_on);
                now_kanban = 5;
                changeUdon();
                break;
            case R.id.udon_kanban6:
                OffSelectUdonImage();
                kanban_image[5].setImageResource(R.drawable.udon_tukimi_kanban_on);
                now_kanban = 6;
                changeUdon();
                break;
            case R.id.udon_kanban7:
                OffSelectUdonImage();
                kanban_image[6].setImageResource(R.drawable.udon_niku_kanban_on);
                now_kanban = 7;
                changeUdon();
                break;
            case R.id.udon_kanban8:
                OffSelectUdonImage();
                kanban_image[7].setImageResource(R.drawable.udon_hiyasi_kanban_on);
                now_kanban = 8;
                changeUdon();
                break;
            case R.id.udon_kanban9:
                OffSelectUdonImage();
                kanban_image[8].setImageResource(R.drawable.udon_kamatama_kanban_on);
                now_kanban = 9;
                changeUdon();
                break;
            case R.id.udon_kanban10:
                OffSelectUdonImage();
                kanban_image[9].setImageResource(R.drawable.udon_bukkake_kanban_on);
                now_kanban = 10;
                changeUdon();
                break;
            case R.id.udon_kanban11:
                OffSelectUdonImage();
                kanban_image[10].setImageResource(R.drawable.udon_wakame_kanban_on);
                now_kanban = 11;
                changeUdon();
                break;
            case R.id.udon_kanban12:
                OffSelectUdonImage();
                kanban_image[11].setImageResource(R.drawable.udon_tenpura_kanban_on);
                now_kanban = 12;
                changeUdon();
                break;
            case R.id.udon_next_image:
                if (now_kanban > 0 && now_size > 0) {
                    if (id == 1) {//導きうどんの場合
                        EventBus.getDefault().post(new MitibikiUdonSelectFinishEvent(now_size, now_kanban));
                    } else if (id == 0) {//知るうどんの場合
                        EventBus.getDefault().post(new SiruUdonSelectFinishEvent(now_size, now_kanban));
                    }
                } else {
                    Toast.makeText(getActivity(), "うどんか大きさが選択されていません。", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void changeUdon() {
        if (now_kanban > 0 && now_size > 0) {//看板が選択されているとき
            udon_image.setImageResource(udon_resources[now_kanban - 1][now_size - 1]);
        }
    }

    /*
        大、中、小の画像をすべてオフに
     */
    private void OffSelectSizeImage() {
        big_image.setImageResource(R.drawable.udon_big_image_off);
        middle_image.setImageResource(R.drawable.udon_middle_image_off);
        small_image.setImageResource(R.drawable.udon_small_image_off);
    }

    private void settingKanban() {
        kanban_image[0] = (ImageView) view.findViewById(R.id.udon_kanban1);
        kanban_image[1] = (ImageView) view.findViewById(R.id.udon_kanban2);
        kanban_image[2] = (ImageView) view.findViewById(R.id.udon_kanban3);
        kanban_image[3] = (ImageView) view.findViewById(R.id.udon_kanban4);
        kanban_image[4] = (ImageView) view.findViewById(R.id.udon_kanban5);
        kanban_image[5] = (ImageView) view.findViewById(R.id.udon_kanban6);
        kanban_image[6] = (ImageView) view.findViewById(R.id.udon_kanban7);
        kanban_image[7] = (ImageView) view.findViewById(R.id.udon_kanban8);
        kanban_image[8] = (ImageView) view.findViewById(R.id.udon_kanban9);
        kanban_image[9] = (ImageView) view.findViewById(R.id.udon_kanban10);
        kanban_image[10] = (ImageView) view.findViewById(R.id.udon_kanban11);
        kanban_image[11] = (ImageView) view.findViewById(R.id.udon_kanban12);
        for (ImageView kanban : kanban_image) {
            kanban.setOnClickListener(this);
        }
    }

    private void OffSelectUdonImage() {
        kanban_image[0].setImageResource(R.drawable.udon_kake_kanban_off);
        kanban_image[1].setImageResource(R.drawable.udon_kare_kanban_off);
        kanban_image[2].setImageResource(R.drawable.udon_kitune_kanban_off);
        kanban_image[3].setImageResource(R.drawable.udon_zaru_kanban_off);
        kanban_image[4].setImageResource(R.drawable.udon_riki_kanban_off);
        kanban_image[5].setImageResource(R.drawable.udon_tukimi_kanban_off);
        kanban_image[6].setImageResource(R.drawable.udon_niku_kanban_off);
        kanban_image[7].setImageResource(R.drawable.udon_hiyasi_kanban_off);
        kanban_image[8].setImageResource(R.drawable.udon_kamatama_kanban_off);
        kanban_image[9].setImageResource(R.drawable.udon_bukkake_kanban_off);
        kanban_image[10].setImageResource(R.drawable.udon_wakame_kanban_off);
        kanban_image[11].setImageResource(R.drawable.udon_tenpura_kanban_off);
    }

    private void settingUdonResources() {
        udon_resources[0][0] = R.drawable.udon_kake_big;
        udon_resources[0][1] = R.drawable.udon_kake_middle;
        udon_resources[0][2] = R.drawable.udon_kake_small;
        udon_resources[1][0] = R.drawable.udon_kare_big;
        udon_resources[1][1] = R.drawable.udon_kare_middle;
        udon_resources[1][2] = R.drawable.udon_kare_small;
        udon_resources[2][0] = R.drawable.udon_kitune_big;
        udon_resources[2][1] = R.drawable.udon_kitune_middle;
        udon_resources[2][2] = R.drawable.udon_kitune_small;
        udon_resources[3][0] = R.drawable.udon_zaru_big;
        udon_resources[3][1] = R.drawable.udon_zaru_middle;
        udon_resources[3][2] = R.drawable.udon_zaru_small;
        udon_resources[4][0] = R.drawable.udon_riki_big;
        udon_resources[4][1] = R.drawable.udon_riki_middle;
        udon_resources[4][2] = R.drawable.udon_riki_small;
        udon_resources[5][0] = R.drawable.udon_tukimi_big;
        udon_resources[5][1] = R.drawable.udon_tukimi_middle;
        udon_resources[5][2] = R.drawable.udon_tukimi_small;
        udon_resources[6][0] = R.drawable.udon_niku_big;
        udon_resources[6][1] = R.drawable.udon_niku_middle;
        udon_resources[6][2] = R.drawable.udon_niku_small;
        udon_resources[7][0] = R.drawable.udon_hiyasi_big;
        udon_resources[7][1] = R.drawable.udon_hiyasi_middle;
        udon_resources[7][2] = R.drawable.udon_hiyasi_small;
        udon_resources[8][0] = R.drawable.udon_kamatama_big;
        udon_resources[8][1] = R.drawable.udon_kamatama_middle;
        udon_resources[8][2] = R.drawable.udon_kamatama_small;
        udon_resources[9][0] = R.drawable.udon_bukkake_big;
        udon_resources[9][1] = R.drawable.udon_bukkake_middle;
        udon_resources[9][2] = R.drawable.udon_bukkake_small;
        udon_resources[10][0] = R.drawable.udon_wakame_big;
        udon_resources[10][1] = R.drawable.udon_wakame_middle;
        udon_resources[10][2] = R.drawable.udon_wakame_small;
        udon_resources[11][0] = R.drawable.udon_tenpura_big;
        udon_resources[11][1] = R.drawable.udon_tenpura_middle;
        udon_resources[11][2] = R.drawable.udon_tenpura_small;
    }
}

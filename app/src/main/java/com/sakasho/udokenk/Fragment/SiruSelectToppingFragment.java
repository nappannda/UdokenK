package com.sakasho.udokenk.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.sakasho.udokenk.Event.SiruToppingSelectFinishEvent;
import com.sakasho.udokenk.R;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiruSelectToppingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private int count = 0;//選択しているおかずの数
    private int[] toppingCount = new int[24];//各おかずの選択されている個数管理
    private ImageView select1, select2, select3;
    private int toppingViewId[] = new int[24];
    private int toppingDrawableId[] = new int[24];
    private int select[] = new int[3];//選択されているおかず管理
    private int size, kanban;//前の画面の選んだうどん情報を保持

    public SiruSelectToppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_siru_select_topping, container, false);
        size = getArguments().getInt("size");
        kanban = getArguments().getInt("kanban");
        String[] toppingArray = getResources().getStringArray(R.array.topppingId);
        for (int i = 0; i < 24; i++) {
            toppingDrawableId[i] = getResources().getIdentifier(toppingArray[i], "drawable",
                    getActivity().getPackageName());
            toppingCount[i] = 0;
        }
        SettingLayout();
        return view;
    }

    private void SettingLayout() {
        select1 = (ImageView) view.findViewById(R.id.siru_select_image1);
        select1.setOnClickListener(this);
        select2 = (ImageView) view.findViewById(R.id.siru_select_image2);
        select2.setOnClickListener(this);
        select3 = (ImageView) view.findViewById(R.id.siru_select_image3);
        select3.setOnClickListener(this);
        toppingViewId[0] = R.id.siru_topping_image1;
        toppingViewId[1] = R.id.siru_topping_image2;
        toppingViewId[2] = R.id.siru_topping_image3;
        toppingViewId[3] = R.id.siru_topping_image4;
        toppingViewId[4] = R.id.siru_topping_image5;
        toppingViewId[5] = R.id.siru_topping_image6;
        toppingViewId[6] = R.id.siru_topping_image7;
        toppingViewId[7] = R.id.siru_topping_image8;
        toppingViewId[8] = R.id.siru_topping_image9;
        toppingViewId[9] = R.id.siru_topping_image10;
        toppingViewId[10] = R.id.siru_topping_image11;
        toppingViewId[11] = R.id.siru_topping_image12;
        toppingViewId[12] = R.id.siru_topping_image13;
        toppingViewId[13] = R.id.siru_topping_image14;
        toppingViewId[14] = R.id.siru_topping_image15;
        toppingViewId[15] = R.id.siru_topping_image16;
        toppingViewId[16] = R.id.siru_topping_image17;
        toppingViewId[17] = R.id.siru_topping_image18;
        toppingViewId[18] = R.id.siru_topping_image19;
        toppingViewId[19] = R.id.siru_topping_image20;
        toppingViewId[20] = R.id.siru_topping_image21;
        toppingViewId[21] = R.id.siru_topping_image22;
        toppingViewId[22] = R.id.siru_topping_image23;
        toppingViewId[23] = R.id.siru_topping_image24;

        for (int i = 0; i < 24; i++) {
            view.findViewById(toppingViewId[i]).setOnClickListener(this);
        }

        view.findViewById(R.id.siru_select_topping_next).setOnClickListener(this);

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("siru_tab1");
        tab1.setIndicator("", getResources().getDrawable(R.drawable.siru_daikon_image2));
        tab1.setContent(R.id.siru_tab1);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("siru_tab2");
        tab2.setIndicator("", getResources().getDrawable(R.drawable.siru_aji_image2));
        tab2.setContent(R.id.siru_tab2);
        tabHost.addTab(tab2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("siru_tab3");
        tab3.setIndicator("", getResources().getDrawable(R.drawable.siru_omusubi_image2));
        tab3.setContent(R.id.siru_tab3);
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.siru_select_topping_next:
                ArrayList<Integer> topping = new ArrayList<>();
                for (int i = 0; i < 24; i++) {//各トッピングが1か2の時データを格納してイベント発行
                    if (toppingCount[i] == 1) {
                        topping.add(i);
                    } else if (toppingCount[i] == 2) {
                        topping.add(i);
                        topping.add(i);
                    }
                }
                EventBus.getDefault().post(new SiruToppingSelectFinishEvent(topping, size, kanban));
                break;
            case R.id.siru_select_image1:
                if (count == 1) {
                    toppingCount[select[0]] = 0;
                    select[0] = 0;
                    select1.setImageResource(R.drawable.all_empty);
                    count = 0;
                } else if (count == 2) {
                    if (select[0] == select[1]) {
                        toppingCount[select[0]] = 1;
                    } else {
                        toppingCount[select[0]] = 0;
                    }
                    select[0] = select[1];
                    select[1] = 0;
                    select2.setImageResource(R.drawable.all_empty);
                    count = 1;
                    select1.setImageResource(toppingDrawableId[select[0]]);
                } else if (count == 3) {
                    if (select[0] == select[1]) {
                        toppingCount[select[0]] = 1;
                    } else if (select[0] == select[2]) {
                        toppingCount[select[0]] = 1;
                    } else {
                        toppingCount[select[0]] = 0;
                    }
                    select[0] = select[1];
                    select[1] = select[2];
                    select[2] = 0;
                    select3.setImageResource(R.drawable.all_empty);
                    count = 2;
                    select1.setImageResource(toppingDrawableId[select[0]]);
                    select2.setImageResource(toppingDrawableId[select[1]]);
                }
                break;
            case R.id.siru_select_image2:
                if (count == 2) {
                    if (select[1] == select[0]) {
                        toppingCount[select[1]] = 1;
                    } else {
                        toppingCount[select[1]] = 0;
                    }
                    select[1] = 0;
                    select2.setImageResource(R.drawable.all_empty);
                    count = 1;
                } else if (count == 3) {
                    if (select[1] == select[2]) {
                        toppingCount[select[1]] = 1;
                    } else if (select[1] == select[0]) {
                        toppingCount[select[1]] = 1;
                    } else {
                        toppingCount[select[1]] = 0;
                    }
                    select[1] = select[2];
                    select[2] = 0;
                    select3.setImageResource(R.drawable.all_empty);
                    count = 2;
                    select2.setImageResource(toppingDrawableId[select[1]]);
                }
                break;
            case R.id.siru_select_image3:
                if (count == 3) {
                    if (select[2] == select[1]) {
                        toppingCount[select[2]] = 1;
                    } else if (select[2] == select[0]) {
                        toppingCount[select[2]] = 1;
                    } else {
                        toppingCount[select[2]] = 0;
                    }
                    select[2] = 0;
                    select3.setImageResource(R.drawable.all_empty);
                    count = 2;
                }
                break;
        }

        for (int i = 0; i < 24; i++) {
            if (id == toppingViewId[i]) {
                if (count == 0) {
                    select[0] = i;
                    toppingCount[i] = 1;
                    select1.setImageResource(toppingDrawableId[i]);
                    count = 1;
                } else if (count == 1) {
                    if (toppingCount[i] == 0) {
                        select[1] = i;
                        toppingCount[i] = 1;
                        select2.setImageResource(toppingDrawableId[i]);
                        count = 2;
                    } else if (toppingCount[i] == 1) {
                        select[1] = i;
                        toppingCount[i] = 2;
                        select2.setImageResource(toppingDrawableId[i]);
                        count = 2;
                    }
                } else if (count == 2) {
                    if (toppingCount[i] == 0) {
                        select[2] = i;
                        toppingCount[i] = 1;
                        select3.setImageResource(toppingDrawableId[i]);
                        count = 3;
                    } else if (toppingCount[i] == 1) {
                        select[2] = i;
                        toppingCount[i] = 2;
                        select3.setImageResource(toppingDrawableId[i]);
                        count = 3;
                    }
                }
            }
        }

    }
}

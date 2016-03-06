package com.sakasho.udokenk.Fragment;


import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakasho.udokenk.Event.AwaseSelectOkazuFinishEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AwaseSelectOkazuFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView okazu_name[] = new TextView[15];
    private ImageView okazu_image[] = new ImageView[15];
    private Drawable[] okazu_drawable = new Drawable[28];
    private String[] okazu_string = new String[28];
    private ImageView select_okazu_image[] = new ImageView[3];//選択されたおかずのImageView
    private ImageView select_udon;//ランダムに選ばれたうどんのImageView
    private int udon_number;//ランダムに選ばれたうどんが何番目かを表す
    private int count = 0;//選択されているおかずの数
    private int select_okazu_number[] = new int[3];//選択されたおかずが何なのかを保持する配列
    private int select_okazu_count;//Activityから今回が何回目かを受け取る

    public AwaseSelectOkazuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_awase_select_okazu, container, false);
        select_okazu_count = getArguments().getInt("count");
        init();
        return view;
    }

    private void init() {
        okazu_image[0] = (ImageView) view.findViewById(R.id.awase_okazu1);
        okazu_image[1] = (ImageView) view.findViewById(R.id.awase_okazu2);
        okazu_image[2] = (ImageView) view.findViewById(R.id.awase_okazu3);
        okazu_image[3] = (ImageView) view.findViewById(R.id.awase_okazu4);
        okazu_image[4] = (ImageView) view.findViewById(R.id.awase_okazu5);
        okazu_image[5] = (ImageView) view.findViewById(R.id.awase_okazu6);
        okazu_image[6] = (ImageView) view.findViewById(R.id.awase_okazu7);
        okazu_image[7] = (ImageView) view.findViewById(R.id.awase_okazu8);
        okazu_image[8] = (ImageView) view.findViewById(R.id.awase_okazu9);
        okazu_image[9] = (ImageView) view.findViewById(R.id.awase_okazu10);
        okazu_image[10] = (ImageView) view.findViewById(R.id.awase_okazu11);
        okazu_image[11] = (ImageView) view.findViewById(R.id.awase_okazu12);
        okazu_image[12] = (ImageView) view.findViewById(R.id.awase_okazu13);
        okazu_image[13] = (ImageView) view.findViewById(R.id.awase_okazu14);
        okazu_image[14] = (ImageView) view.findViewById(R.id.awase_okazu15);

        okazu_name[0] = (TextView) view.findViewById(R.id.awase_okazu_name1);
        okazu_name[1] = (TextView) view.findViewById(R.id.awase_okazu_name2);
        okazu_name[2] = (TextView) view.findViewById(R.id.awase_okazu_name3);
        okazu_name[3] = (TextView) view.findViewById(R.id.awase_okazu_name4);
        okazu_name[4] = (TextView) view.findViewById(R.id.awase_okazu_name5);
        okazu_name[5] = (TextView) view.findViewById(R.id.awase_okazu_name6);
        okazu_name[6] = (TextView) view.findViewById(R.id.awase_okazu_name7);
        okazu_name[7] = (TextView) view.findViewById(R.id.awase_okazu_name8);
        okazu_name[8] = (TextView) view.findViewById(R.id.awase_okazu_name9);
        okazu_name[9] = (TextView) view.findViewById(R.id.awase_okazu_name10);
        okazu_name[10] = (TextView) view.findViewById(R.id.awase_okazu_name11);
        okazu_name[11] = (TextView) view.findViewById(R.id.awase_okazu_name12);
        okazu_name[12] = (TextView) view.findViewById(R.id.awase_okazu_name13);
        okazu_name[13] = (TextView) view.findViewById(R.id.awase_okazu_name14);
        okazu_name[14] = (TextView) view.findViewById(R.id.awase_okazu_name15);

        select_okazu_image[0] = (ImageView) view.findViewById(R.id.awase_select_okazu1);
        select_okazu_image[0].setOnClickListener(this);
        select_okazu_image[1] = (ImageView) view.findViewById(R.id.awase_select_okazu2);
        select_okazu_image[1].setOnClickListener(this);
        select_okazu_image[2] = (ImageView) view.findViewById(R.id.awase_select_okazu3);
        select_okazu_image[2].setOnClickListener(this);

        select_udon = (ImageView) view.findViewById(R.id.awase_select_udon_image);
        view.findViewById(R.id.awase_select_okazu_next_image).setOnClickListener(this);

        count = 0;

        //ランダムでうどんを表示
        TypedArray udonImages = getResources().obtainTypedArray(R.array.udon_images);
        int udon_random = (int) (Math.random() * (12));//12個のうどんの中からランダムに選ぶ
        udon_number = udon_random;
        select_udon.setImageDrawable(udonImages.getDrawable(udon_number));

        //おかずの情報を読み込む
        TypedArray okazuNames = getResources()
                .obtainTypedArray(R.array.okazu_names);
        TypedArray okazuImages = getResources().obtainTypedArray(
                R.array.okazu_images);
        for (int i = 0; i < 28; i++) {
            okazu_string[i] = okazuNames.getString(i);
            okazu_drawable[i] = okazuImages.getDrawable(i);
        }

        for (int i = 0; i < 100; i++) {//おかずをランダムに並び替える
            int r1 = (int) (Math.random() * (28));
            int r2 = (int) (Math.random() * (28));
            Drawable tmp1;
            String tmp2;

            tmp1 = okazu_drawable[r1];
            okazu_drawable[r1] = okazu_drawable[r2];
            okazu_drawable[r2] = tmp1;

            tmp2 = okazu_string[r1];
            okazu_string[r1] = okazu_string[r2];
            okazu_string[r2] = tmp2;
        }

        for (int i = 0; i < 15; i++) {
            okazu_image[i].setOnClickListener(this);
            okazu_image[i].setImageDrawable(okazu_drawable[i]);
            okazu_name[i].setGravity(Gravity.CENTER);
            okazu_name[i].setText(okazu_string[i]);
        }
        //おかずの保持の配列の初期化 100の場合、空
        select_okazu_number[0] = 100;
        select_okazu_number[1] = 100;
        select_okazu_number[2] = 100;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < 15; i++) {
            if (v.getId() == okazu_image[i].getId()) {
                JudgeOkazu(i);
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (v.getId() == select_okazu_image[i].getId()) {
                if (count > 0) {
                    RemoveOkazu(i);
                }
                break;
            }
        }

        if (v.getId() == R.id.awase_select_okazu_next_image) {
            String okazus[] = new String[3];
            for (int i = 0; i < 3; i++) {
                if (select_okazu_number[i] == 100) {
                    okazus[i] = null;
                } else {
                    okazus[i] = okazu_string[select_okazu_number[i]];
                }
            }
            EventBus.getDefault().post(new AwaseSelectOkazuFinishEvent(udon_number, okazus, select_okazu_count));
        }

    }

    /*
        選択したおかずを消す処理
        @pos 消すおかずの場所を引数に取る、上の三つのどれか
     */
    private void RemoveOkazu(int pos) {
        if (pos == 0) {
            if (select_okazu_number[1] != 100 && select_okazu_number[2] != 100) {
                select_okazu_image[0].setImageDrawable(okazu_drawable[select_okazu_number[1]]);
                select_okazu_image[1].setImageDrawable(okazu_drawable[select_okazu_number[2]]);
                select_okazu_image[2].setImageResource(R.drawable.all_empty);
                select_okazu_number[0] = select_okazu_number[1];
                select_okazu_number[1] = select_okazu_number[2];
                select_okazu_number[2] = 100;
            } else if (select_okazu_number[1] != 100) {
                select_okazu_image[0].setImageDrawable(okazu_drawable[select_okazu_number[1]]);
                select_okazu_image[1].setImageResource(R.drawable.all_empty);
                select_okazu_number[0] = select_okazu_number[1];
                select_okazu_number[1] = 100;
            } else {
                select_okazu_image[0].setImageResource(R.drawable.all_empty);
                select_okazu_number[0] = 100;
            }
        } else if (pos == 1) {
            if (select_okazu_number[2] != 100) {//三つ目におかずがあるとき
                select_okazu_image[1].setImageDrawable(okazu_drawable[select_okazu_number[2]]);
                select_okazu_image[2].setImageResource(R.drawable.all_empty);
                select_okazu_number[1] = select_okazu_number[2];
                select_okazu_number[2] = 100;
            } else {
                select_okazu_image[1].setImageResource(R.drawable.all_empty);
                select_okazu_number[1] = 100;
            }
        } else if (pos == 2) {
            select_okazu_number[2] = 100;
            select_okazu_image[2].setImageResource(R.drawable.all_empty);
        }
        count--;
    }

    /*
        選択したおかずを追加する処理
        @pos 0-14のどれかを引数に取る、どのおかずが押されたか
        @number どこにおかずを追加するか
     */
    private void AddOkazu(int pos, int number) {
        select_okazu_image[number].setImageDrawable(okazu_drawable[pos]);
        select_okazu_number[number] = pos;
        count++;
    }

    /*
        選択したおかずを消すか追加するかを判定
        @pos 0-14のどれかを引数に取る、どのおかずが押されたか
     */
    private void JudgeOkazu(int pos) {
        switch (count) {
            case 0:
                AddOkazu(pos, count);
                break;
            case 1:
            case 2:
            case 3:
                boolean okazu_add = true;
                for (int i = 0; i < count; i++) {
                    if (pos == select_okazu_number[i]) {
                        RemoveOkazu(i);
                        okazu_add = false;
                        break;
                    }
                }
                if (okazu_add && count != 3) {
                    AddOkazu(pos, count);
                }
                break;

        }
    }
}

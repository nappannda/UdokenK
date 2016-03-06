package com.sakasho.udokenk.Fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakasho.udokenk.Event.NextTopEvent;
import com.sakasho.udokenk.NutritionsUtil;
import com.sakasho.udokenk.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiruResultFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView tourist_spot_image;
    private int tourist_spot_number;//ランダムに観光地を決める
    private AnimationDrawable tourist_frame_animation;
    private int tourist_drawable_id[] = new int[4];
    private int nutrition_drawable_id[][] = new int[7][6];//栄養表示用の画像ID保存配列
    private Double total_nutritions[] = new Double[7];//うどんとおかずの合計栄養素を入れる
    private int size, kanban;//選んだうどんが入る
    private ArrayList<Integer> topping;//選んだおかずが入る
    private TextView nutritions_text[] = new TextView[7];
    private ImageView nutritions_image[] = new ImageView[7];
    private String[] nutritions_unit = {"g", "g", "g", "mg", "g", "g", "Kcal"};
    private String[] nutritions_name = {"タンパク質\n", "脂質\n", "炭水化物\n", "カルシウム\n", "食物繊維\n", "食塩\n", "カロリー\n"};
    private NutritionsUtil nutritionsUtil;
    private TextView result_text1, result_text2, result_text3;

    public SiruResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_siru_result, container, false);
        size = getArguments().getInt("size");
        kanban = getArguments().getInt("kanban");
        topping = getArguments().getIntegerArrayList("topping");
        nutritionsUtil = new NutritionsUtil(getActivity());

        SettingView();
        AnimationStart();

        CalculationNutrition();
        SetTouristConsumptionCalorie();
        return view;
    }

    private void SetTouristConsumptionCalorie() {
        BigDecimal consumptionCalorie = new BigDecimal(nutritionsUtil.getConsumptionCalorie(tourist_spot_number));//消費カロリー取得
        BigDecimal totalCalorie = new BigDecimal(total_nutritions[6]);
        BigDecimal result = totalCalorie.divide(consumptionCalorie, 1, BigDecimal.ROUND_HALF_UP);
        switch (tourist_spot_number) {
            case 0://瀬戸大橋
                BigDecimal s1 = new BigDecimal(15000);
                BigDecimal s2 = new BigDecimal(24000);//12000*2
                result = result.multiply(s1).divide(s2, 1, BigDecimal.ROUND_HALF_UP);
                result_text1.setText("あなたの選んだ食品の合計カロリーを消費するには瀬戸大橋を");
                result_text2.setText(result.doubleValue() + "往復");
                result_text3.setText("しなければいけません。");
                break;
            case 1:
                BigDecimal m1 = new BigDecimal(10000);
                BigDecimal m2 = new BigDecimal(1100);
                result = result.multiply(m1).divide(m2, 1, BigDecimal.ROUND_HALF_UP);
                result_text1.setText("あなたの選んだ食品の合計カロリーを消費するには丸亀城を");
                result_text2.setText(result.doubleValue() + "周");
                result_text3.setText("しなければいけません。");
                break;
            case 2:
                BigDecimal i1 = new BigDecimal(10000);
                BigDecimal i2 = new BigDecimal(1100);
                result = result.multiply(i1).divide(i2, 1, BigDecimal.ROUND_HALF_UP);
                result_text1.setText("あなたの選んだ食品の合計カロリーを消費するには満濃池を");
                result_text2.setText(result.doubleValue() + "往復");
                result_text3.setText("しなければいけません。");
                break;
            case 3:
                BigDecimal k1 = new BigDecimal(10000);
                BigDecimal k2 = new BigDecimal(1100);
                result = result.multiply(k1).divide(k2, 1, BigDecimal.ROUND_HALF_UP);
                result_text1.setText("あなたの選んだ食品の合計カロリーを消費するには金比羅さんを");
                result_text2.setText(result.doubleValue() + "往復");
                result_text3.setText("しなければいけません。");
                break;
        }
    }

    private void SettingView() {
        view.findViewById(R.id.siru_top_next).setOnClickListener(this);
        tourist_drawable_id[0] = R.drawable.siru_animation_setoohasi;
        tourist_drawable_id[1] = R.drawable.siru_animation_marugamezyo;
        tourist_drawable_id[2] = R.drawable.siru_animation_manno;
        tourist_drawable_id[3] = R.drawable.siru_animation_konpira;

        Random random = new Random();
        tourist_spot_number = random.nextInt(4);
        tourist_spot_image = (ImageView) view.findViewById(R.id.siru_tourist_spot_image);
        tourist_spot_image.setOnClickListener(this);

        SetIdNutritionDrawable();

        nutritions_text[0] = (TextView) view.findViewById(R.id.siru_protein_text);
        nutritions_text[1] = (TextView) view.findViewById(R.id.siru_lipid_text);
        nutritions_text[2] = (TextView) view.findViewById(R.id.siru_carbohydrate_text);
        nutritions_text[3] = (TextView) view.findViewById(R.id.siru_calcium_text);
        nutritions_text[4] = (TextView) view.findViewById(R.id.siru_dietary_fiber_text);
        nutritions_text[5] = (TextView) view.findViewById(R.id.siru_salt_text);
        nutritions_text[6] = (TextView) view.findViewById(R.id.siru_calorie_text);
        nutritions_image[0] = (ImageView) view.findViewById(R.id.siru_protein_image);
        nutritions_image[1] = (ImageView) view.findViewById(R.id.siru_lipid_image);
        nutritions_image[2] = (ImageView) view.findViewById(R.id.siru_carbohydrate_image);
        nutritions_image[3] = (ImageView) view.findViewById(R.id.siru_calcium_image);
        nutritions_image[4] = (ImageView) view.findViewById(R.id.siru_dietary_fiber_image);
        nutritions_image[5] = (ImageView) view.findViewById(R.id.siru_salt_image);
        nutritions_image[6] = (ImageView) view.findViewById(R.id.siru_calorie_image);

        result_text1 = (TextView) view.findViewById(R.id.siru_result_text1);
        result_text2 = (TextView) view.findViewById(R.id.siru_result_text2);
        result_text3 = (TextView) view.findViewById(R.id.siru_result_text3);

    }

    /*
        うどんとおかずの栄養素を足していく
     */
    private void CalculationNutrition() {
        //栄養素配列初期化
        for (int i = 0; i < 7; i++) {
            total_nutritions[i] = 0.0;
        }

        //うどんの栄養素を取得する
        Double udon_nutritions[] = nutritionsUtil.getUdonNutritions(kanban, size);

        //おかずの栄養素を取得する
        Double topping_nutritions[] = nutritionsUtil.getToppingNutritions(topping);

        //うどんとおかずの栄養素を足して表示する
        for (int i = 0; i < 7; i++) {
            BigDecimal u = new BigDecimal(udon_nutritions[i]);
            BigDecimal t = new BigDecimal(topping_nutritions[i]);
            total_nutritions[i] = u.add(t).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//うどんとおかずの栄養素を足す 値を少数第二位で四捨五入
            nutritions_text[i].setText(nutritions_name[i] + total_nutritions[i].toString() + nutritions_unit[i]);
            nutritions_image[i].setImageResource(nutrition_drawable_id[i][JudgeNutrition(i)]);
        }
    }

    /*
        栄養素が不足しているか取り過ぎているかを判断しその数値を返す
     */
    private int JudgeNutrition(int i) {
        int num = 0;
        BigDecimal judge_num = new BigDecimal(nutritionsUtil.getJudgeNutritionNumber(i));
        BigDecimal total_num = judge_num;
        while (total_nutritions[i] > total_num.doubleValue()) {
            total_num = total_num.add(judge_num);
            num++;
        }
        if (num >= 6) {
            num = 5;
        }
        return num;
    }


    private void SetIdNutritionDrawable() {//画像のIDを配列に入れる
        String[] calorie = getResources().getStringArray(R.array.all_nutrition_calorie);
        String[] protein = getResources().getStringArray(R.array.all_nutrition_protein);
        String[] lipid = getResources().getStringArray(R.array.all_nutrition_lipid);
        String[] carbohydrate = getResources().getStringArray(R.array.all_nutrition_carbohydrate);
        String[] salt = getResources().getStringArray(R.array.all_nutrition_salt);
        String[] calcium = getResources().getStringArray(R.array.all_nutrition_calcium);
        String[] dietary_fiber = getResources().getStringArray(R.array.all_nutrition_dietary_fiber);

        for (int i = 0; i < 6; i++) {
            nutrition_drawable_id[6][i] = getResources().getIdentifier(calorie[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[0][i] = getResources().getIdentifier(protein[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[1][i] = getResources().getIdentifier(lipid[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[2][i] = getResources().getIdentifier(carbohydrate[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[5][i] = getResources().getIdentifier(salt[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[3][i] = getResources().getIdentifier(calcium[i],
                    "drawable", getActivity().getPackageName());
            nutrition_drawable_id[4][i] = getResources().getIdentifier(dietary_fiber[i],
                    "drawable", getActivity().getPackageName());
        }
    }


    private void AnimationStart() {
        tourist_spot_image.setImageResource(tourist_drawable_id[tourist_spot_number]);
        tourist_frame_animation = (AnimationDrawable) tourist_spot_image.getDrawable();
        tourist_frame_animation.start();//アニメーションスタート
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siru_tourist_spot_image:
                if (tourist_spot_number == 0) {
                    if (tourist_frame_animation.getCurrent() == tourist_frame_animation.getFrame(7)) {
                        tourist_spot_image.setImageResource(R.drawable.siru_animation_setoohasi7);
                    }
                } else if (tourist_spot_number == 1) {
                    if (tourist_frame_animation.getCurrent() == tourist_frame_animation.getFrame(5)) {
                        tourist_spot_image.setImageResource(R.drawable.siru_animation_marugamezyo5);
                    }
                } else if (tourist_spot_number == 2) {
                    if (tourist_frame_animation.getCurrent() == tourist_frame_animation.getFrame(9)) {
                        tourist_spot_image.setImageResource(R.drawable.siru_animation_manno9);
                    }
                } else if (tourist_spot_number == 3) {
                    if (tourist_frame_animation.getCurrent() == tourist_frame_animation.getFrame(6)) {
                        tourist_spot_image.setImageResource(R.drawable.siru_animation_konpira6);
                    }
                }
                break;
            case R.id.siru_top_next:
                EventBus.getDefault().post(new NextTopEvent());
                break;
        }

    }
}

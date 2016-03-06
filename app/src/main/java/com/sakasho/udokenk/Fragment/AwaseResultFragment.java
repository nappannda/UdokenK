package com.sakasho.udokenk.Fragment;


import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakasho.udokenk.Event.NextTopEvent;
import com.sakasho.udokenk.NutritionsUtil;
import com.sakasho.udokenk.R;

import java.math.BigDecimal;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AwaseResultFragment extends Fragment implements View.OnClickListener {
    private View view;
    private int nutrition_drawable_id[][] = new int[7][6];//栄養表示用の画像ID保存配列
    private int udons[];//うどんのデータ保持
    private ArrayList<Integer> okazu1, okazu2, okazu3;//おかずのデータ保持
    private ImageView select_udon;//うどんのImageView
    private ImageView select_okazu[] = new ImageView[3];//おかずのImageView
    private ImageView score_images[] = new ImageView[4];//スコア確認用ImageView
    private int okazu_drawable_id[] = new int[28];
    private Double nutritions[][] = new Double[4][7];//一、二、三回目と総合の栄養素情報を保存
    private NutritionsUtil nutritionsUtil;
    private TextView nutritions_text[] = new TextView[7];
    private ImageView nutritions_image[] = new ImageView[7];
    private String[] nutritions_unit = {"g", "g", "g", "mg", "g", "g", "Kcal"};
    private String[] nutritions_name = {"タンパク質\n", "脂質\n", "炭水化物\n", "カルシウム\n", "食物繊維\n", "食塩\n", "カロリー\n"};
    private int point_drawable_id[][] = new int[6][2];//点数用のどんぶり画像Id保持
    private int donburi_drawable_id[] = new int[6];//点数用のどんぶり画像Id保持
    private ImageView point_imageview[] = new ImageView[6];
    private LinearLayout donburi_linear;
    private SharedPreferences sp;

    public AwaseResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_awase_result, container, false);
        udons = getArguments().getIntArray("awase_udons");
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SettingView();
        okazu1 = new ArrayList<>();
        okazu2 = new ArrayList<>();
        okazu3 = new ArrayList<>();

        TypedArray okazuNames = getResources()
                .obtainTypedArray(R.array.okazu_names);
        for (String okazuName : getArguments().getStringArray("awase_okazu1")) {
            for (int i = 0; i < 28; i++) {
                if (okazuNames.getString(i).equals(okazuName)) {
                    okazu1.add(i);
                }
            }
        }
        for (String okazuName : getArguments().getStringArray("awase_okazu2")) {
            for (int i = 0; i < 28; i++) {
                if (okazuNames.getString(i).equals(okazuName)) {
                    okazu2.add(i);
                }
            }
        }
        for (String okazuName : getArguments().getStringArray("awase_okazu3")) {
            for (int i = 0; i < 28; i++) {
                if (okazuNames.getString(i).equals(okazuName)) {
                    okazu3.add(i);
                }
            }
        }
        nutritionsUtil = new NutritionsUtil(getActivity());
        CalculationNutrition();
        score_images[0].setImageResource(R.drawable.awase_total_on);
        return view;
    }

    /*
        うどんとおかずの栄養素を足していく
     */
    private void CalculationNutrition() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                nutritions[i][j] = 0.0;
            }
        }

        Double udons_nutritions[][] = new Double[3][7];
        Double okazus_nutritions[][] = new Double[3][7];
        for (int i = 0; i < 3; i++) {
            udons_nutritions[i] = nutritionsUtil.getUdonNutritions(udons[i] + 1, 2);//サイズは普通の2

        }
        okazus_nutritions[0] = nutritionsUtil.getOkazuNutritions(okazu1);
        okazus_nutritions[1] = nutritionsUtil.getOkazuNutritions(okazu2);
        okazus_nutritions[2] = nutritionsUtil.getOkazuNutritions(okazu3);

        BigDecimal total[] = new BigDecimal[7];
        for (int i = 0; i < 7; i++) {//合計の初期化
            total[i] = new BigDecimal(0);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                BigDecimal u = new BigDecimal(udons_nutritions[i][j]);
                BigDecimal o = new BigDecimal(okazus_nutritions[i][j]);
                nutritions[i][j] = u.add(o).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                total[j] = total[j].add(new BigDecimal(nutritions[i][j]));
            }
        }
        for (int i = 0; i < 7; i++) {//合計値を３で割って平均を代入 第二位を四捨五入
            nutritions[3][i] = total[i].divide(new BigDecimal(3), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        SetNutritionsImageAndText(3);//3なので総合
        int point = nutritionsUtil.getAwaseNutritionPoint(nutritions[3]);
        SetPointImage(point);
        sp.edit().putInt("awase_point", point).commit();

    }

    /*
        pointを受け取りそのポイントに応じて画像を変える
     */
    private void SetPointImage(int point) {
        for (int i = 0; i < 6; i++) {
            if (i <= point) {
                point_imageview[i].setImageResource(point_drawable_id[i][1]);
            } else {
                point_imageview[i].setImageResource(point_drawable_id[i][0]);
            }
        }
        donburi_linear.setBackgroundResource(donburi_drawable_id[point]);
    }

    /*
        総合か1,2,3回目かをposで受け取り情報を書き換える
     */
    private void SetNutritionsImageAndText(int pos) {
        for (int i = 0; i < 7; i++) {
            nutritions_text[i].setText(nutritions_name[i] + nutritions[pos][i].toString() + nutritions_unit[i]);
            nutritions_image[i].setImageResource(nutrition_drawable_id[i][JudgeNutrition(i, nutritions[pos][i])]);
        }
    }


    /*
     栄養素が不足しているか取り過ぎているかを判断しその数値を返す
  */
    private int JudgeNutrition(int i, Double total_nutrition) {
        int num = 0;
        BigDecimal judge_num = new BigDecimal(nutritionsUtil.getJudgeNutritionNumber(i));
        BigDecimal total_num = judge_num;
        while (total_nutrition > total_num.doubleValue()) {
            total_num = total_num.add(judge_num);
            num++;
        }
        if (num >= 6) {
            num = 5;
        }
        return num;
    }

    private void SettingView() {
        donburi_drawable_id[0] = R.drawable.awase_donburi_background1;
        donburi_drawable_id[1] = R.drawable.awase_donburi_background2;
        donburi_drawable_id[2] = R.drawable.awase_donburi_background3;
        donburi_drawable_id[3] = R.drawable.awase_donburi_background4;
        donburi_drawable_id[4] = R.drawable.awase_donburi_background5;
        donburi_drawable_id[5] = R.drawable.awase_donburi_background6;

        donburi_linear = (LinearLayout) view.findViewById(R.id.awase_donburi_linear);

        for (int i = 0; i < 6; i++) {
            point_drawable_id[i][0] = R.drawable.awase_point_udon0;
        }
        point_drawable_id[0][1] = R.drawable.awase_kake_top_image;
        point_drawable_id[1][1] = R.drawable.awase_kitune_top_image;
        point_drawable_id[2][1] = R.drawable.awase_tukimi_top_image;
        point_drawable_id[3][1] = R.drawable.awase_kare_top_image;
        point_drawable_id[4][1] = R.drawable.awase_niku_top_image;
        point_drawable_id[5][1] = R.drawable.awase_tenpura_top_image;

        point_imageview[0] = (ImageView) view.findViewById(R.id.awase_point_udon1);
        point_imageview[1] = (ImageView) view.findViewById(R.id.awase_point_udon2);
        point_imageview[2] = (ImageView) view.findViewById(R.id.awase_point_udon3);
        point_imageview[3] = (ImageView) view.findViewById(R.id.awase_point_udon4);
        point_imageview[4] = (ImageView) view.findViewById(R.id.awase_point_udon5);
        point_imageview[5] = (ImageView) view.findViewById(R.id.awase_point_udon6);

        select_udon = (ImageView) view.findViewById(R.id.awase_result_select_udon);
        select_okazu[0] = (ImageView) view.findViewById(R.id.awase_result_select_okazu1);
        select_okazu[1] = (ImageView) view.findViewById(R.id.awase_result_select_okazu2);
        select_okazu[2] = (ImageView) view.findViewById(R.id.awase_result_select_okazu3);

        nutritions_text[0] = (TextView) view.findViewById(R.id.awase_protein_text);
        nutritions_text[1] = (TextView) view.findViewById(R.id.awase_lipid_text);
        nutritions_text[2] = (TextView) view.findViewById(R.id.awase_carbohydrate_text);
        nutritions_text[3] = (TextView) view.findViewById(R.id.awase_calcium_text);
        nutritions_text[4] = (TextView) view.findViewById(R.id.awase_dietary_fiber_text);
        nutritions_text[5] = (TextView) view.findViewById(R.id.awase_salt_text);
        nutritions_text[6] = (TextView) view.findViewById(R.id.awase_calorie_text);
        nutritions_image[0] = (ImageView) view.findViewById(R.id.awase_protein_image);
        nutritions_image[1] = (ImageView) view.findViewById(R.id.awase_lipid_image);
        nutritions_image[2] = (ImageView) view.findViewById(R.id.awase_carbohydrate_image);
        nutritions_image[3] = (ImageView) view.findViewById(R.id.awase_calcium_image);
        nutritions_image[4] = (ImageView) view.findViewById(R.id.awase_dietary_fiber_image);
        nutritions_image[5] = (ImageView) view.findViewById(R.id.awase_salt_image);
        nutritions_image[6] = (ImageView) view.findViewById(R.id.awase_calorie_image);

        score_images[0] = (ImageView) view.findViewById(R.id.awase_total_image);
        score_images[1] = (ImageView) view.findViewById(R.id.awase_first_image);
        score_images[2] = (ImageView) view.findViewById(R.id.awase_second_image);
        score_images[3] = (ImageView) view.findViewById(R.id.awase_third_image);

        view.findViewById(R.id.awase_top_image).setOnClickListener(this);

        for (int i = 0; i < 4; i++) {
            score_images[i].setOnClickListener(this);
        }
        SetIdOkazuDrawable();
        SetIdNutritionDrawable();
    }

    private void SetIdOkazuDrawable() {
        TypedArray okazuArray = getResources().obtainTypedArray(R.array.okazu_images);
        for (int i = 0; i < 28; i++) {
            okazu_drawable_id[i] = okazuArray.getResourceId(i, 0);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awase_total_image:
                SetNutritionsImageAndText(3);
                SetPointImage(nutritionsUtil.getAwaseNutritionPoint(nutritions[3]));
                select_udon.setImageResource(R.drawable.all_empty);
                for (int i = 0; i < 3; i++) {
                    select_okazu[i].setImageResource(R.drawable.all_empty);
                }
                InitScoreImage();
                score_images[0].setImageResource(R.drawable.awase_total_on);
                break;
            case R.id.awase_first_image:
                SetNutritionsImageAndText(0);
                SetUdonAndOkazuImage(0);
                SetPointImage(nutritionsUtil.getAwaseNutritionPoint(nutritions[0]));
                InitScoreImage();
                score_images[1].setImageResource(R.drawable.awase_first_on);
                break;
            case R.id.awase_second_image:
                SetNutritionsImageAndText(1);
                SetUdonAndOkazuImage(1);
                SetPointImage(nutritionsUtil.getAwaseNutritionPoint(nutritions[1]));
                InitScoreImage();
                score_images[2].setImageResource(R.drawable.awase_second_on);
                break;
            case R.id.awase_third_image:
                SetNutritionsImageAndText(2);
                SetUdonAndOkazuImage(2);
                SetPointImage(nutritionsUtil.getAwaseNutritionPoint(nutritions[2]));
                InitScoreImage();
                score_images[3].setImageResource(R.drawable.awase_third_on);
                break;
            case R.id.awase_top_image:
                EventBus.getDefault().post(new NextTopEvent());
                break;
        }
    }

    private void InitScoreImage() {
        score_images[0].setImageResource(R.drawable.awase_total);
        score_images[1].setImageResource(R.drawable.awase_first);
        score_images[2].setImageResource(R.drawable.awase_second);
        score_images[3].setImageResource(R.drawable.awase_third);

    }


    private void SetUdonAndOkazuImage(int pos) {
        TypedArray udonArray = getResources().obtainTypedArray(R.array.udon_top_images);
        TypedArray okazuImages = getResources().obtainTypedArray(
                R.array.okazu_images);
        select_udon.setImageResource(udonArray.getResourceId(udons[pos], 0));
        int i = 0;
        switch (pos) {
            case 0:
                for (int o : okazu1) {
                    select_okazu[i].setImageResource(okazuImages.getResourceId(o, 0));
                    i++;
                }
                while (i < 3) {
                    select_okazu[i].setImageResource(R.drawable.all_empty);
                    i++;
                }
                break;
            case 1:
                for (int o : okazu2) {
                    select_okazu[i].setImageResource(okazuImages.getResourceId(o, 0));
                    i++;
                }
                while (i < 3) {
                    select_okazu[i].setImageResource(R.drawable.all_empty);
                    i++;
                }
                break;
            case 2:
                for (int o : okazu3) {
                    select_okazu[i].setImageResource(okazuImages.getResourceId(o, 0));
                    i++;
                }
                while (i < 3) {
                    select_okazu[i].setImageResource(R.drawable.all_empty);
                    i++;
                }
                break;
        }
    }
}

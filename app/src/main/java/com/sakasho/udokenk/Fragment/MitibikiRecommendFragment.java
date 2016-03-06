package com.sakasho.udokenk.Fragment;


import android.content.res.TypedArray;
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

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitibikiRecommendFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TypedArray todohuken, specialty_names, specialty_calories, specialty_drawables,
            udon_drawable_id, udon_names,
            okazu_names, okazu_drawable_id, okazu_description;
    private Double udon_nutritions[];//うどんの栄養素保存
    private NutritionsUtil nutritionsUtil;
    private int udon, size;//うどんとサイズ保存用
    private ImageView specialty_image, udon_image, okazu_image;
    private TextView specialty_text, specialty_name, specialty_number, udon_name_text, okazu_name_text, okazu_description_text;

    public MitibikiRecommendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mitibiki_recommend, container, false);
        nutritionsUtil = new NutritionsUtil(getActivity());
        udon = getArguments().getInt("udon");
        size = getArguments().getInt("size");
        udon_nutritions = nutritionsUtil.getUdonNutritions(udon, size);

        todohuken = getResources().obtainTypedArray(R.array.specialty_todohuken);
        specialty_names = getResources().obtainTypedArray(R.array.specialty_name);
        specialty_calories = getResources().obtainTypedArray(R.array.specialty_calorie);
        specialty_drawables = getResources().obtainTypedArray(R.array.specialty_drawable);
        udon_drawable_id = getResources().obtainTypedArray(R.array.udon_top_images);
        udon_names = getResources().obtainTypedArray(R.array.udon_names);
        okazu_names = getResources().obtainTypedArray(R.array.mitibiki_okazu_name);
        okazu_drawable_id = getResources().obtainTypedArray(R.array.mitibiki_okazu_drawable);
        okazu_description = getResources().obtainTypedArray(R.array.mitibiki_okazu_description);
        SetView();
        SetSpecialty();
        return view;
    }

    private void SetView() {
        specialty_image = (ImageView) view.findViewById(R.id.specialty_image);
        specialty_image.setOnClickListener(this);
        udon_image = (ImageView) view.findViewById(R.id.mitibiki_udon_image);
        view.findViewById(R.id.recommend_next_top).setOnClickListener(this);
        specialty_name = (TextView) view.findViewById(R.id.specialty_name);
        specialty_text = (TextView) view.findViewById(R.id.specialty_text);
        specialty_number = (TextView) view.findViewById(R.id.specialty_number);
        udon_name_text = (TextView) view.findViewById(R.id.mitibiki_udon_name);
        okazu_name_text = (TextView) view.findViewById(R.id.mitibiki_okazu_name);
        okazu_description_text = (TextView) view.findViewById(R.id.mitibiki_okazu_description);
        okazu_image = (ImageView) view.findViewById(R.id.mitibiki_okazu_image);

        udon_image.setImageResource(udon_drawable_id.getResourceId(udon - 1, 0));
        udon_name_text.setText(udon_names.getString(udon - 1) + "にぴったりなおかずは");
        int okazu = nutritionsUtil.getUdonFitOkazu(udon_nutritions);
        okazu_name_text.setText(okazu_names.getString(okazu));
        okazu_description_text.setText(okazu_description.getString(okazu));
        okazu_image.setImageResource(okazu_drawable_id.getResourceId(okazu, 0));
    }

    private void SetSpecialty() {
        int random = (int) (Math.random() * (47));//都道府県の名産をどれにするかランダム関数を使って決める
        BigDecimal specialty_calorie = new BigDecimal(specialty_calories.getInt(random, 0));
        BigDecimal udon_calorie = new BigDecimal(udon_nutritions[6]);
        Double specialty_num = udon_calorie.divide(specialty_calorie, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        specialty_image.setImageDrawable(specialty_drawables.getDrawable(random));
        specialty_number.setText("×" + specialty_num);
        specialty_name.setText(todohuken.getString(random) + "\n" + specialty_names.getString(random));
        specialty_text.setText(udon_names.getString(udon-1) + "のカロリー（" + udon_calorie.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "Kcal）を" + specialty_names.getString(random) + "に換算すると" + specialty_num + "個です。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.specialty_image:
                SetSpecialty();
                break;
            case R.id.recommend_next_top:
                EventBus.getDefault().post(new NextTopEvent());
                break;
        }
    }
}

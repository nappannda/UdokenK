package com.sakasho.udokenk.Fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sakasho.udokenk.Event.MitibikiDescriptionFinishEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

public class MitibikiDescriptionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView animationImage, nextImage;

    public MitibikiDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mitibiki_description, container, false);
        nextImage = (ImageView) view.findViewById(R.id.mitibiki_next_image);
        nextImage.setOnClickListener(this);
        animationImage = (ImageView) view.findViewById(R.id.mitibiki_animation_image);
        animationImage.setBackgroundResource(R.drawable.all_udonmaru_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) animationImage
                .getBackground();
        frameAnimation.start();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mitibiki_next_image:
                EventBus.getDefault().post(new MitibikiDescriptionFinishEvent());
                break;
        }
    }
}

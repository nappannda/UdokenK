package com.sakasho.udokenk.Fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sakasho.udokenk.Event.AwaseDescriptionFinishEvent;
import com.sakasho.udokenk.Event.MitibikiDescriptionFinishEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AwaseDescriptionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView animationImage, nextImage;

    public AwaseDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_awase_description, container, false);
        nextImage = (ImageView) view.findViewById(R.id.awase_description_next_image);
        nextImage.setOnClickListener(this);
        animationImage = (ImageView) view.findViewById(R.id.awase_animation_image);
        animationImage.setBackgroundResource(R.drawable.all_udonmaru_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) animationImage
                .getBackground();
        frameAnimation.start();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awase_description_next_image:
                EventBus.getDefault().post(new AwaseDescriptionFinishEvent());
                break;
        }
    }
}

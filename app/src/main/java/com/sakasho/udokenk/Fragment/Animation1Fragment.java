package com.sakasho.udokenk.Fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakasho.udokenk.Event.Animation1ClickEvent;
import com.sakasho.udokenk.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Animation1Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private LinearLayout container_linear;
    private TextView description_text;
    private ImageView animation1_image;
    private String description;
    private String put_word = "", put_text = "";
    private int i = 0;
    private static int TIMEOUT_MESSAGE = 1;
    private static int INTERVAL = 1;

    public Animation1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animation1, container, false);
        container_linear = (LinearLayout) view.findViewById(R.id.anim_container1_linear);
        container_linear.setOnClickListener(this);
        description_text = (TextView) view.findViewById(R.id.anim_description1_text);
        description = getResources().getString(R.string.anim_description1_text);
        handler.sendEmptyMessage(TIMEOUT_MESSAGE);
        animation1_image = (ImageView) view.findViewById(R.id.anim_animation1_image);
        animation1_image.setBackgroundResource(R.drawable.anim_animation1);
        AnimationDrawable frameAnimation = (AnimationDrawable) animation1_image
                .getBackground();
        frameAnimation.start();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anim_container1_linear:
                //EventBusでActivityへ
                EventBus.getDefault().post(new Animation1ClickEvent());
                break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {

            char data[] = description.toCharArray();
            int arr_num = data.length;
            if (i < arr_num) {
                if (msg.what == TIMEOUT_MESSAGE) {
                    put_word = String.valueOf(data[i]);
                    put_text = put_text + put_word;
                    description_text.setText(put_text);
                    handler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE,
                            INTERVAL * 100);
                    i++;
                } else {
                    super.dispatchMessage(msg);
                }
            }
        }
    };

}

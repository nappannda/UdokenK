package com.sakasho.udokenk.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sakasho.udokenk.Event.MoyoriUdonFinishEvent;
import com.sakasho.udokenk.R;
import com.sakasho.udokenk.UdonShopEntity;
import com.sakasho.udokenk.UdonShop;

import de.greenrobot.event.EventBus;


public class MoyoriUdonMapFragment extends Fragment implements View.OnClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private UdonShopEntity udonShopEntity;
    private View view;
    private TextView shop_number_text;
    private ImageView top_image;
    private LinearLayout shop_list_linear;

    public MoyoriUdonMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moyori_udon_map, container, false);
        shop_number_text = (TextView) view.findViewById(R.id.moyori_shop_number_text);
        top_image = (ImageView) view.findViewById(R.id.moyori_top_image);
        top_image.setOnClickListener(this);
        shop_list_linear = (LinearLayout) view.findViewById(R.id.moyori_shop_list_linear);
        udonShopEntity = (UdonShopEntity) getArguments().getSerializable("UdonShop");
        setUpMapIfNeeded();
        return view;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment smp = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.moyori_map_fragment);
            mMap = smp.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);//拡大縮小ボタン表示

        LatLng START_POS = new LatLng(udonShopEntity.getNowLocation().getLatitude(), udonShopEntity.getNowLocation().getLongitude());//現在地
        CameraPosition pos = new CameraPosition(START_POS, 16, 0, 0);
        // カメラにセット
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(pos);
        // カメラの位置に移動
        mMap.moveCamera(camera);
        setUpUdonMarker();
    }

    private void setUpUdonMarker() {
        shop_number_text.setText("検索の結果、" + udonShopEntity.getUdonShosList().size() + "件見つかりました。");
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(udonShopEntity.getNowLocation().getLatitude(),
                                udonShopEntity.getNowLocation().getLongitude()))
                .title("現在地"));

        for (UdonShop udonShop : udonShopEntity.getUdonShosList()) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(Double.valueOf(udonShop.getLatitude()),
                                    Double.valueOf(udonShop.getLongitude())))
                    .title(udonShop.getName() + "(" + udonShop.getName_kana() + ")")
                    .snippet(udonShop.getAddress())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.moyori_map_udon_icon)));

            TextView shop = new TextView(getActivity());
            shop.setText(udonShop.getName());
            shop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            shop_list_linear.addView(shop);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moyori_top_image:
                EventBus.getDefault().post(new MoyoriUdonFinishEvent());
                break;
        }
    }
}

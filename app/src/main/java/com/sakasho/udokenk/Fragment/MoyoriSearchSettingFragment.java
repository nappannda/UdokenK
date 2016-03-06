package com.sakasho.udokenk.Fragment;


import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.sakasho.udokenk.Event.SearchCompleteEvent;
import com.sakasho.udokenk.R;
import com.sakasho.udokenk.UdonShopApi;
import com.sakasho.udokenk.UdonShopEntity;

import java.util.Date;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoyoriSearchSettingFragment extends Fragment implements View.OnClickListener, LocationListener {
    private static final String END_POINT = "http://api.gnavi.co.jp";
    private View view;
    private Spinner distance_spinner;
    private ImageView search_image;
    private SharedPreferences sp;
    private LocationManager lManager;
    private SearchProgressDialogFragment searchProgressDialogFragment;

    public MoyoriSearchSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_moyori_search_setting, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        distance_spinner = (Spinner) view.findViewById(R.id.moyori_distance_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("300m");
        adapter.add("500m");
        adapter.add("1000m");
        adapter.add("2000m");
        adapter.add("3000m");

        distance_spinner.setAdapter(adapter);
        distance_spinner.setSelection(sp.getInt("moyori_selected_distance", 0));
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                SharedPreferences.Editor e = sp.edit();
                e.putInt("moyori_selected_distance", position);
                e.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        search_image = (ImageView) view.findViewById(R.id.moyori_search_image);
        search_image.setOnClickListener(this);

        lManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moyori_search_image:
                if (!lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//GPSがオンじゃないとき
                    if (getActivity().getSupportFragmentManager().findFragmentByTag("dialog") == null) {
                        GpsDialogFragment dialog = new GpsDialogFragment();
                        dialog.show(getFragmentManager(), "dialog");
                    }

                } else {
                    //ProgressDialogを表示
                    if (searchProgressDialogFragment == null) {
                        searchProgressDialogFragment = SearchProgressDialogFragment.newInstance(R.string.moyori_progress_title, R.string.moyori_progress_message);
                        searchProgressDialogFragment.show(getActivity().getFragmentManager(), "progress");
                        try {//TODO Android6.0対応 パーミッション関係
                            lManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 10000, 100, this);
                        } catch (SecurityException se) {

                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationManagerRemoveUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        GpsDialogFragment dialog = (GpsDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog != null) {
            if (dialog.getShowsDialog()) dialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationManagerRemoveUpdates();
    }

    private void LocationManagerRemoveUpdates() {
        if (lManager != null) {
            try {
                lManager.removeUpdates(this);
            } catch (SecurityException se) {

            }

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        LocationManagerRemoveUpdates();
        getUdonShop(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getUdonShop(final Location location) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        // RestAdapterを作成する
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("=NETWORK="))
                .build();
        UdonShopApi api = adapter.create(UdonShopApi.class);

        final Observer observer = new Observer<UdonShopEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                searchProgressDialogFragment.dismiss();
                searchProgressDialogFragment = null;
                Toast.makeText(getActivity(), "店の情報取得に失敗しました。もう一度お試しください。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(UdonShopEntity udonShop) {
                searchProgressDialogFragment.dismiss();
                searchProgressDialogFragment = null;
                udonShop.setNowLocation(location);
                EventBus.getDefault().post(new SearchCompleteEvent(udonShop));
            }
        };
        api.getUdonShops(getResources().getString(R.string.api_key),
                "json",
                "RSFST08000",
                "RSFST08002",
                "2",
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                String.valueOf(sp.getInt("moyori_selected_distance", 0) + 1),
                "100").
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(observer);
        ;

    }

}

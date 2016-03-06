package com.sakasho.udokenk;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by nappannda on 2016/02/04.
 */
public interface UdonShopApi {
    @GET("/RestSearchAPI/20150630/")
    public Observable<UdonShopEntity> getUdonShops(@Query("keyid") final String keyid,
                                                   @Query("format") final String format,
                                                   @Query("category_l") final String category_l,
                                                   @Query("category_s") final String category_s,
                                                   @Query("input_coordinates_mode") final String input_coordinates_mode,
                                                   @Query("latitude") final String latitude,
                                                   @Query("longitude") final String longitude,
                                                   @Query("range") final String range,
                                                   @Query("hit_per_page") final String hit_per_page);

}
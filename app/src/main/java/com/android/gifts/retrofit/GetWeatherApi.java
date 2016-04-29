package com.android.gifts.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ahmedadel on 26/04/16.
 */
public interface GetWeatherApi {

    /**
     * Synchronous and Asynchronous background requests through Retrofit
     * In regard to service interface declaration in Retrofit 1.9, you have to to declare a synchronous function,
     * and an asynchronous separately, But on Retrofit 2.0,
     * it is far more simple since you can declare with only just a single pattern.
     * @param cityName London as an example
     * @param units metrics as an example
     * @param appId that the developer got from OpenWeatherMap after singing up
     * @return
     */
    @GET("/data/2.5/weather")
    Call<WeatherDataBean> getWeatherFromApiSyncAndAsync(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("APPID") String appId);

}
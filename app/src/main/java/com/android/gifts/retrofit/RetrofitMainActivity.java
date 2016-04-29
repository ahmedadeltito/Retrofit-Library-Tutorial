package com.android.gifts.retrofit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Define the root OpenWeatherMap URL and your OpenWeatherMap AppID
     */
    private static final String API_URL = "http://api.openweathermap.org";
    private static final String CITY_NAME = "London";
    private static final String UNITS = "metric";
    private static final String APP_ID = "c6afdab60aa89481e297e0a4f19af055";

    /**
     * Instance variables to represent the "London Current Weather Synchronously"
     * and "London Current Weather Asynchronously" buttons,
     * "Temperature", "Pressure" and "Humidity" TextViews and loadingProgressbar
     */
    private Button getLondonCurrentWeatherSync, getLondonCurrentWeatherAsync;
    private TextView temperatureTextView, pressureTextView, humidityTextView;
    private ProgressBar loadingProgressBar;
    private LinearLayout getLondonCurrentWeatherLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_main);

        /**
         * Instantiate the variables we declared above using the ID values
         * we specified in the layout XML file.
         */
        getLondonCurrentWeatherSync = (Button) findViewById(R.id.activity_retrofit_main_get_weather_sync_btn);
        getLondonCurrentWeatherAsync = (Button) findViewById(R.id.activity_retrofit_main_get_weather_async_btn);
        getLondonCurrentWeatherLinearLayout = (LinearLayout) findViewById(R.id.activity_retrofit_main_root_ll);
        temperatureTextView = (TextView) findViewById(R.id.activity_retrofit_main_temperature_tv);
        pressureTextView = (TextView) findViewById(R.id.activity_retrofit_main_pressure_tv);
        humidityTextView = (TextView) findViewById(R.id.activity_retrofit_main_humidity_tv);
        loadingProgressBar = (ProgressBar) findViewById(R.id.activity_retrofit_main_pb);

        /**
         * Add a listener to getLondonCurrentWeatherSync and getLondonCurrentWeatherAsync so that we can handle presses
         */
        getLondonCurrentWeatherSync.setOnClickListener(this);
        getLondonCurrentWeatherAsync.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * make a synchronous background request
             */
            case R.id.activity_retrofit_main_get_weather_sync_btn:
                getWeatherSync();
                break;
            /**
             * make an Asynchronous background request
             */
            case R.id.activity_retrofit_main_get_weather_async_btn:
                getWeatherAsync();
                break;
        }
    }

    /**
     * getWeatherAsync() method will make an asynchronous background request
     * by using Call Interface built in Retrofit Library
     */
    private void getWeatherAsync() {
        getLondonCurrentWeatherLinearLayout.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        /**
         * To make REST API call through Android Retrofit Library we may first need to build an instance of Retrofit class.
         * This class is the main class of Retrofit Library which executes all the requests.
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /**
         * Next you need to create an instance of your Interface GetWeatherApi.
         * since create() method that is inside retrofit object return the parsed response in
         * GetWeatherApi Interface format
         */
        GetWeatherApi getWeatherApi = retrofit.create(GetWeatherApi.class);
        /**
         * After this, call getWeatherFromApiSyncAndAsync() method from getWeatherApi object that return an instance of
         * Call Retrofit Interface then call enqueue() method to make an asynchronous API request, and implement inside it
         * the CallBack Interface Listener "Observer" since this Callback Interface has to methods onResponse()
         * that is fire once a successive response is returned from OpenWeatherMap API and onFailure()
         * that is fire once an error occurs
         */
        Call<WeatherDataBean> call = getWeatherApi.getWeatherFromApiSyncAndAsync(CITY_NAME, UNITS, APP_ID);
        call.enqueue(new Callback<WeatherDataBean>() {
            @Override
            public void onResponse(Call<WeatherDataBean> call, Response<WeatherDataBean> response) {
                updateUI(response.body());
            }

            @Override
            public void onFailure(Call<WeatherDataBean> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(RetrofitMainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * updateUI() method will be used once a successive response is returned from OpenWeatherMap API
     *
     * @param weatherDataBean
     */
    private void updateUI(WeatherDataBean weatherDataBean) {
        loadingProgressBar.setVisibility(View.GONE);
        if (weatherDataBean != null) {
            getLondonCurrentWeatherLinearLayout.setVisibility(View.VISIBLE);
            temperatureTextView.setText("Temperature : " + weatherDataBean.getMain().getTemp() + " Celsius");
            pressureTextView.setText("Pressure : " + weatherDataBean.getMain().getPressure());
            humidityTextView.setText("Humidity : " + weatherDataBean.getMain().getHumidity());
        }
    }

    /**
     * getWeatherSync() method will make an synchronous background request
     * by using Call Interface built in Retrofit Library
     */
    private void getWeatherSync() {
        GetWeatherSync getWeatherSync = new GetWeatherSync();
        getWeatherSync.execute();
    }

    private class GetWeatherSync extends AsyncTask<Void, Void, WeatherDataBean> {

        Retrofit retrofit;

        @Override
        protected void onPreExecute() {
            getLondonCurrentWeatherLinearLayout.setVisibility(View.INVISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
            /**
             * To make REST API call through Android Retrofit Library we may first need to build an instance of Retrofit class.
             * This class is the main class of Retrofit Library which executes all the requests.
             */
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        @Override
        protected WeatherDataBean doInBackground(Void... params) {
            WeatherDataBean weatherDataBean = null;
            try {
                /**
                 * Next you need to create an instance of your Interface GetWeatherApi.
                 * since create() method that is inside retrofit object return the parsed response in
                 * GetWeatherApi Interface format
                 */
                GetWeatherApi getWeatherApi = retrofit.create(GetWeatherApi.class);
                /**
                 * After this, call getWeatherFromApiSyncAndAsync() method from getWeatherApi object that return an instance of
                 * Call Interface and then call execute() method since this method make a request synchronous
                 * so it needs to be handled in background thread like AsyncTask Class to avoid
                 * NetworkOnMainThreadException runtime error.
                 */
                Call<WeatherDataBean> call = getWeatherApi.getWeatherFromApiSyncAndAsync(CITY_NAME, UNITS, APP_ID);
                Response<WeatherDataBean> response = call.execute();
                weatherDataBean = response.body();
            } catch (final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(RetrofitMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return weatherDataBean;
        }

        /**
         * Get the returned object from doInBackground() method and get from this object the
         * necessary data that we want.
         *
         * @param weatherDataBean is the returned object that came from doInBackground() merhod
         */
        @Override
        protected void onPostExecute(WeatherDataBean weatherDataBean) {
            updateUI(weatherDataBean);
        }
    }
}

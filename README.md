# Retrofit-Library-Tutorial

In this tutorial we are having the Retrofit Android Library Tutorial which is a type-safe HTTP client for Android and Java by Square. By using Retrofit in Android we can seamlessly capture JSON responses from a web API. It is different from other libraries because Retrofit gives us an easy way to use since it uses the GSON library in background to parse the responses. All we need to do is define a POJO (Plain Old Java Object) to do all the parsing. In this Retrofit Android Library Tutorial, we will discuss the basic scenarios where it can be used.

#Our Application:

Our application will use Retrofit Library to make a sample application has a single button “London Current Weather”. Once the user is going to click on this button, London current weather data are appeared on defined TextViews  by integrating with REST web API from OpenWeatherMap.

Comparison of AsyncHttp ,Volley and Retrofit which this is showing how retrofit is beating other libraries

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/retrofit_speed-768x214.png)

#Converters:

In the past, Retrofit relied on the GSON library to serialize and deserialize JSON data. Retrofit 2 now supports many different parsers for processing network response data, including Moshi, a library build by Square for efficient JSON parsing. However, there are a few limitations, so if you are not sure which one to choose, use the Gson converter for now.

| CONVERTER  | LIBRARY |
| ------------- |:-------------:|
| Gson  | com.squareup.retrofit2:converter-gson:2.0.2 |
| Jackson  | com.squareup.retrofit2:converter-jackson:2.0.2 |
| Moshi | com.squareup.retrofit2:converter-moshi:2.0.2  |
| Protobuf  | com.squareup.retrofit2:converter-protobuf:2.0.2 |
| Wire  | com.squareup.retrofit2:converter-wire:2.0.2 |
| Simple XML | com.squareup.retrofit2:converter-simplexml:2.0.2 |

# Main Steps:

1. First of all, you have to create a free account on OpenWeatherMap to get a free Application ID to be able to access OpenWeatherMap APIs,the creating account steps are very sample.
2. Create an Android Application.
3. Add permission to your Manifest.xml and build.gradle file.
4. Start to access OpenWeatherMap APIs to get London current weather data.

# 1. Setup
* Retrofit Library dependency and its Gson converter library dependency should be added in the build.gradle of Retrofit Example Application:

```xml
compile 'com.squareup.retrofit2:retrofit:2.0.2'
compile 'com.squareup.retrofit2:converter-gson:2.0.2'
```

* Internet permission should be added in the Manifest.xml file.
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

# 2. Creating a Retrofit Request

* To send out network requests to an API, you need to use the Retrofit.Builder class and specify the base URL for the service.

```java
private static final String API_URL = "http://api.openweathermap.org";
Retrofit retrofit = new Retrofit.Builder()
 .baseUrl(API_URL)
 .addConverterFactory(GsonConverterFactory.create())
 .build();
 ```

* Note also that you need to specify a factory for deserializing the response using the GSON library. If you wish to pass in a custom Gson parser instance, it can be specified too:

```java
Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create();
Retrofit retrofit = new Retrofit.Builder()
 .baseUrl(API_URL)
 .addConverterFactory(GsonConverterFactory.create())
 .build();
 ```
# 3. Defining The Endpoints

* With Retrofit 2, endpoints are defined inside of an interface using special retrofit annotations to encode details about the parameters and request method. In addition, the return value is always a parameterized Call<T> object such as Call<WeatherDataBean>. If you do not need any type-specific response, you can specify return value as simply Call<ResponseBody>.
* Notice that each endpoint specifies an annotation of the HTTP method (GET, POST, etc.) and method that will be used to dispatch the network call. Note that the parameters of this method can also have special annotations:

| ANNOTATION  | DESCRIPTION |
| ------------- |:-------------:|
| @Path  | variable substitution for the API endpoint (i.e. username will be swapped for {username} in the URL endpoint). |
| @Query  | specifies the query key name with the value of the annotated parameter. |
| @Body | payload for the POST call (serialized from a Java object to a JSON string)  |
| @Header  | specifies the header with the value of the annotated parameter |

# 4. Changing the base URL

* Normally, the base URL is defined when you instantiated an Retrofit instance. Retrofit 2 allows you to override the base URL specified by changing it in the annotation

```java
@POST("http://api.openweathermap.org")
```

# 5. Adding Headers

* Notice that there is a @Headers and @Header annotation. The Headers can be used to provide predefined ones:

```java
@Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
@GET("/some/endpoint")
```

* You can also add headers as a parameter to the endpoint:

```java
@Multipart
@POST("/some/endpoint")
Call<SomeResponse> someEndpoint(@Header("Cache-Control") int maxAge)
```

# 6. Uploading images or files

* If you need to upload images or files, you need to send by using Multipart forms. You will to mark the endpoint with @Multipart, and label at least one parameter with @Part.

```java
@Multipart
@POST("some/endpoint")
Call<Response> uploadImage(@Part("description") String description, @Part("image") RequestBody image)
```

* Assuming you have a reference to the file, you can create a RequestBody object:

```java
MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
file = new File("/storage/emulated/0/Pictures/RetrofitExampleProject/test.png");
RequestBody requestBody = RequestBody.create(MEDIA_TYPE_PNG, file);
Call<Response> call = apiService.uploadImage("test", requestBody);
```

* If you need to specify a unique filename for your multipart upload. Alternatively, you can create a multi-part RequestBody according to this OkHttp recipe guide and pass it along as one of the @Part annotated parameters:

```java
RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"mycustomfile.png\""),
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();
```

# 7. Request from URL Encoding

* If you want to make a Retrofit request form-encoded name/value pairs, you can use the @FormUrlEncoded and @FieldMap annotations:

```java
@FormUrlEncoded
@POST("some/endpoint")
Call<SomeResponse> someEndpoint(@FieldMap Map<String, String> names);
```

# 8. POST or CREATE fields by JSON data

* Retrofit 2 will use the converter library chosen to handle the deserialization of data from a Java object. If you annotate the parameter with a @Body  parameter, this work will be done automatically. If you are using the Gson library for instance, any field belonging to the class will be serialized for you. You can change this name using the @SerializedName decorator:

```java
public class User {
 
  @SerializedName("id")
  int mId;
 
  @SerializedName("name")
  String mName;
 
  public User(int id, String name ) {
    this.mId = id;
    this.mName = name;
  }
}
```

* Our endpoint would look like the following:

```java
@POST("/users/new")
Call<User> createUser(@Body User user);
```

* You could invoke this API call as follows:

```java
User user = new User(123, "John Doe");
Call<User> call = apiService.createuser(user);
call.enqueue(new Callback<User>() {
  @Override
  public void onResponse(Call<User> call, Response<User> response) {
 
  }
 
  @Override
  public void onFailure(Call<User> call, Throwable t) {
 
  }
// The resulting network call would POST this data: 
{"name":"John Doe","id":123}
```

# 9. Accessing API Asynchronously or Synchronously

* If you want to consume the API asynchronously, you call the service as follows:

```java
String username = "Ahmed Adel";
Call<User> call = apiService.getUser(username);
call.enqueue(new Callback<User>() {
    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        int statusCode = response.code();
        User user = response.body();  
    }
 
    @Override
    public void onFailure(Call<User> call, Throwable t) {
        // Log error here since request failed
 
    }
});
```

* If you want to consume the API synchronously, you call the service as follows:

```java
try {
  Response<User> response = call.execute();
} catch (IOException e ){
   // handle error
}
```

# 10. Using Authentication Headers

* Headers can be added to a request using an Interceptor. To send requests to an authenticated API, add headers to your requests using an interceptor as outlined below:

```java
// Define the interceptor, add authentication headers
 
Interceptor interceptor = new Interceptor() {
  @Override
  public okhttp3.Response intercept(Chain chain) throws IOException {
    Request newRequest = chain.request().newBuilder().addHeader("User-Agent", "Retrofit-Sample-App").build();
    return chain.proceed(newRequest);
  }
};
 
// Add the interceptor to OkHttpClient
 
OkHttpClient.Builder builder = new OkHttpClient.Builder();
builder.interceptors().add(interceptor);
OkHttpClient client = builder.build();
 
// Set the custom client when building adapter
 
Retrofit retrofit = new Retrofit.Builder()
  .baseUrl("https://api.github.com")
  .addConverterFactory(GsonConverterFactory.create())
  .client(client)
  .build();
```

# Creating Retrofit Example Project:

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/RetrofitDiagram-1-768x403.jpg)

* In Android Studio, create a new activity, select Empty Activity and lets name RetrofitMainActivity.
For this Android Retrofit Example, we are showing responses in TextViews, hence a layout with the TextViews, Buttons and ProgressBar should be created, open activity_retrofit_main.xml and add this code:

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.android.gifts.retrofit.RetrofitMainActivity">
 
    <Button
        android:id="@+id/activity_retrofit_main_get_weather_sync_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="5dp"
        android:text="@string/get_weather_sync"
        android:textAllCaps="false" />
 
    <Button
        android:id="@+id/activity_retrofit_main_get_weather_async_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/activity_retrofit_main_get_weather_sync_btn"
        android:layout_centerHorizontal="true"
        android:text="@string/get_weather_async"
        android:textAllCaps="false" />
 
    <ProgressBar
        android:id="@+id/activity_retrofit_main_pb"
        style="?android:attr/progressBarStyleLarge"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignBottom="@+id/activity_retrofit_main_root_ll"
        android:layout_alignTop="@+id/activity_retrofit_main_root_ll"
        android:layout_centerHorizontal="true"
        android:layout_margin="5dp"
        android:visibility="gone" />
 
    <LinearLayout
        android:id="@+id/activity_retrofit_main_root_ll"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:orientation="vertical"
        android:visibility="invisible">
 
        <TextView
            android:id="@+id/activity_retrofit_main_temperature_tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="5dp"
            android:layout_marginTop="5dp"
            android:text="@string/temperature"
            android:textAppearance="?android:textAppearanceMedium" />
 
        <TextView
            android:id="@+id/activity_retrofit_main_pressure_tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="5dp"
            android:layout_marginTop="5dp"
            android:text="@string/pressure"
            android:textAppearance="?android:textAppearanceMedium" />
 
        <TextView
            android:id="@+id/activity_retrofit_main_humidity_tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="5dp"
            android:layout_marginTop="5dp"
            android:text="@string/humidity"
            android:textAppearance="?android:textAppearanceMedium" />
 
    </LinearLayout>
 
</RelativeLayout>
```

* Therefore we will make a POJO Class for the data we need, here we will need London current weather data. To parse the response properly you may need to define the WeatherDataBean class carefully. As it should have the same structure as JSON being returned in response. Although it is not required to define all the fields, if you don’t want to parse all of them.

**JSON that we will parse from OpenWeatherMap APIs:**

```xml
{
    "main": {
        "temp": 3.72, 
        "pressure": 1009,
        "humidity": 86,
        "temp_min": 2,
        "temp_max": 5.3
    }
}
```

* You can convert your JSON object into POJO one through this website, so go to this site, copy and paste this JSON above, select source type to be JSON and click generate.

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/json2pojo-768x651.png)

* Create a new class, call it WeatherDataBean.java class and add this code or the code generated from http://www.jsonschema2pojo.org/ :

```java
package com.android.gifts.retrofit;
 
public class WeatherDataBean {
 
    private Main main;
 
    public Main getMain() {
        return main;
    }
 
    public void setMain(Main main) {
        this.main = main;
    }
 
    class Main {
        double temp;
        double pressure;
        int humidity;
 
        public double getTemp() {
            return temp;
        }
 
        public void setTemp(double temp) {
            this.temp = temp;
        }
 
        public double getPressure() {
            return pressure;
        }
 
        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
 
        public int getHumidity() {
            return humidity;
        }
 
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }
}
```

* Next we will make a Java Interface class “GetWeatherApi” to access the web service. In GetWeatherApi interface you may notice that, two custom annotations have been used. The @GET annotation is used to define the relative HTTP URL path and method. While the @Query annotation is used to define the query string for URL.

**GetWeatherApi.java Interface Class:**

```java
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
    Call&lt;WeatherDataBean&gt; getWeatherFromApiSyncAndAsync(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("APPID") String appId);
 
}
```

* The above interface “GetWeatherApi” would be making a web API hit on the following URL:

```java
http://api.openweathermap.org/data/2.5/weather?q=cityName&units=units&appid=appId
```

* Then we will go to implement GetWeatherApi interface function in onCreate() method of RetrofitMainActivity and start to handle synchronous and Asynchronous request calling.

**RetrofitMainActivity.java Class**

```java
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
        Call&lt;WeatherDataBean&gt; call = getWeatherApi.getWeatherFromApiSyncAndAsync(CITY_NAME, UNITS, APP_ID);
        call.enqueue(new Callback&lt;WeatherDataBean&gt;() {
            @Override
            public void onResponse(Call&lt;WeatherDataBean&gt; call, Response&lt;WeatherDataBean&gt; response) {
                updateUI(response.body());
            }

            @Override
            public void onFailure(Call&lt;WeatherDataBean&gt; call, Throwable t) {
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

    private class GetWeatherSync extends AsyncTask&lt;Void, Void, WeatherDataBean&gt; {

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
                Call&lt;WeatherDataBean&gt; call = getWeatherApi.getWeatherFromApiSyncAndAsync(CITY_NAME, UNITS, APP_ID);
                Response&lt;WeatherDataBean&gt; response = call.execute();
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
```

# Finally The Retrofit Example Application after running should be as screenshots attached below:

Main Screen with two buttons “London Current Weather Synchronously” and “London Current Weather Asynchronously”

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/Home-Screen-576x1024.png)

After pressing on “London Current Weather Synchronously”:

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/After-pressing-on-London-Current-Weather-Synchronously-576x1024.png)

After pressing on “London Current Weather Asynchronously”

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/After-pressing-on-London-Current-Weather-Asynchronously-576x1024.png)

Loading progress bar appears informing the user that your request is in progress

![alt tag](http://androidgifts.com/wp-content/uploads/2016/04/Loading-576x1024.png)

[You can check the whole article here](http://androidgifts.com/retrofit-android-library-tutorial-library-6/)

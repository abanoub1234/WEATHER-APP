package com.example.whether_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String API_KEY = "041f7408456c0fb4f8458e13cbe60bd2";
    Button btn_search;
    EditText et_search;
    TextView tv_temp;
    TextView tv_CityName;
    ImageView iconWeather;
    ListView lvDailyWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btn_search = findViewById(R.id.btn_Search);
        iconWeather = findViewById(R.id.icon_weather);
        et_search = findViewById(R.id.et_CityName);
        tv_CityName = findViewById(R.id.tv_cityName);
        tv_temp = findViewById(R.id.tv_temp);
        lvDailyWeather = findViewById(R.id.lvDailyWeather);

        btn_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String City = et_search.getText().toString();
                if(City.isEmpty())
                {
                    Toast.makeText(getBaseContext() , "Please Enter City Name!" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //سنقوم بتحميل بيانات الطقس لاسم المدينه اللى دخلها اليوزر
                    loadWeatherByCityName(City);
                }
            }
        });

    }

    private void loadWeatherByCityName(String city)
    {
        Ion.with(this)
                .load("https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid="+API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        // do stuff with the result or error
                        if(e != null)
                        {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext() , "Server Error" , Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                           //تحويل الملف اللى عملناله لودينج بامتداد Json  سيتم تحويله الى كود جافا
                            JsonObject main = result.get("main").getAsJsonObject();
                            double temp = main.get("temp").getAsDouble();
                            tv_temp.setText(temp+" °C");

                            JsonObject sys = result.get("sys").getAsJsonObject();
                            String Country = sys.get("country").getAsString();
                            tv_CityName.setText(city + ", " + Country);

                            JsonArray weather = result.get("weather").getAsJsonArray();
                            String icon = weather.get(0).getAsJsonObject().get("icon").getAsString();
                            Ion.with(getBaseContext())
                                    .load("http://openweathermap.org/img/w/"+icon+".png")
                                    .intoImageView(iconWeather);

                            JsonObject coord = result.get("coord").getAsJsonObject();
                            Double lat = coord.get("lat").getAsDouble();
                            Double lon = coord.get("lon").getAsDouble();
                            loadDailyForecast(lat , lon);
                        }
                    }
                });
    }

    private void loadDailyForecast(Double lat, Double lon)
    {
        Ion.with(this)
                .load("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely,current&units=metric&appid="+API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        // do stuff with the result or error
                        if(e != null)
                        {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext() , "Server Error" , Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            //تحويل الملف اللى عملناله لودينج بامتداد Json  سيتم تحويله الى كود جافا
                            List<Weather> WeatherList = new ArrayList<>();
                            String timeZone = result.get("timezone").getAsString();
                            JsonArray daily = result.get("daily").getAsJsonArray();
                            for(int i=1 ; i<daily.size() ; i++)
                            {
                                long Date = daily.get(i).getAsJsonObject().get("dt").getAsLong();
                                Double Temp = daily.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();
                                String Icon = daily.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                                WeatherList.add(new Weather(Date , timeZone , Temp , Icon));
                            }

                            //وضع الادابتر فى ليست فيو لعرض البيانات عليها
                            DailyWeatherAdapter adapter = new DailyWeatherAdapter(MainActivity.this, WeatherList);
                            lvDailyWeather.setAdapter(adapter);
                        }
                    }
                });
    }


}
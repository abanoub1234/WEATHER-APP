package com.example.whether_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyWeatherAdapter extends ArrayAdapter<Weather>
{
    private Context context;
    private List<Weather> weatherList;
    DecimalFormat df = new DecimalFormat("#.00");


    public DailyWeatherAdapter(@NonNull Context context, @NonNull List<Weather> weatherList)
    {
        super(context, 0, weatherList);
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_weather , parent , false);
        TextView tv_Date = convertView.findViewById(R.id.tv_Date);
        TextView tv_Temp = convertView.findViewById(R.id.tv_Temp);
        ImageView icon_Weather = convertView.findViewById(R.id.iconWeather);

        Weather weather = weatherList.get(position);
        Double temp = weather.getTemp();
        tv_Temp.setText(df.format(temp) + " °C");
        Ion.with(context)
                .load("http://openweathermap.org/img/w/"+weather.getIcon()+".png")
                .intoImageView(icon_Weather);
        Date date = new Date(weather.getDate()*1000);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MM yy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(weather.getTimeZone()));
        tv_Date.setText(dateFormat.format(date));
        return convertView;
    }
}

package com.example.whether_app;

public class Weather
{
    private long Date;
    private String timeZone;
    private Double temp;
    private String icon;

    public Weather(long date, String timeZone, Double temp, String icon) {
        Date = date;
        this.timeZone = timeZone;
        this.temp = temp;
        this.icon = icon;
    }

    public Weather(Double temp, String icon) {
        this.temp = temp;
        this.icon = icon;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

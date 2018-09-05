package com.mansoor.asmaulhusna.activity;

import android.app.Application;
import android.content.res.Configuration;

import com.mansoor.asmaulhusna.models.Prayers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by l4208412 on 18/6/2018.
 */

public class MyApplication extends Application {



    public String QuotesUrl = "https://raw.githubusercontent.com/kpamansoor/api/master/asmaulhusna/quotes.json";
    public String DailyVerseUrl = "http://api.alquran.cloud/ayah/";
    public String PrayerTimesUrl = "http://api.aladhan.com/v1/calendar?";
    public List<String> prayersList = new ArrayList<>(Arrays.asList("Fajr","Sunrise","Dhuhr","Asr","Sunset","Maghrib","Isha"));
    public String getPrayerTimesUrl() {
        return PrayerTimesUrl;
    }
    public String getDailyVerseUrl() {
        return DailyVerseUrl;
    }
    public String getQuotesUrl() {
        return QuotesUrl;
    }
    public String app_name = "Islamic Hub";
    public String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public  String convert24To12(String time){
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("K:mm aa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public String dataToText(String text) {
//        String text = "2015-01-17";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");

            Date date = sdf1.parse(text);

            return sdf2.format(date);
        } catch (ParseException exp) {
            exp.printStackTrace();
        }

        return text;
    }

    public Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    public String showCurrentPrayer(Date date, Prayers prayers) {
        int hour = date.getHours();
        int minut = date.getMinutes();
        int currentIndex = 0;
        double currentTime = Double.parseDouble(hour+"."+minut);
        double min, diff ;
        String nextPrayerName = "",nextPrayerTime = "";
        Double[] prayersTimes = new Double[7];
        prayersTimes[0] = Double.parseDouble(prayers.getFajr().split(" ")[0].replace(":","."));
        prayersTimes[1] = Double.parseDouble(prayers.getSunrise().split(" ")[0].replace(":","."));
        prayersTimes[2] = Double.parseDouble(prayers.getDhuhr().split(" ")[0].replace(":","."));
        prayersTimes[3] = Double.parseDouble(prayers.getAsr().split(" ")[0].replace(":","."));
        prayersTimes[4] = Double.parseDouble(prayers.getSunset().split(" ")[0].replace(":","."));
        prayersTimes[5] = Double.parseDouble(prayers.getMaghrib().split(" ")[0].replace(":","."));
        prayersTimes[6] = Double.parseDouble(prayers.getIsha().split(" ")[0].replace(":","."));

        min = prayersTimes[0] - currentTime;
        for (int i = 0; i < prayersTimes.length; i++) {
            diff = prayersTimes[i] - currentTime;
            if(min < 0)
                min = diff;
            if(diff >= 0 && diff <= min) {
                min = diff;
                nextPrayerTime = convert24To12(String.valueOf(prayersTimes[i]).replace(".",":"));
                nextPrayerName = prayersList.get(i);
                currentIndex = i;
            }
        }
        return nextPrayerName+","+nextPrayerTime+","+currentIndex;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}

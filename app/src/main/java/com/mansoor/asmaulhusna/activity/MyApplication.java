package com.mansoor.asmaulhusna.activity;

import android.app.Application;
import android.content.res.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by l4208412 on 18/6/2018.
 */

public class MyApplication extends Application {



    public String QuotesUrl = "https://raw.githubusercontent.com/kpamansoor/api/master/asmaulhusna/quotes.json";
    public String DailyVerseUrl = "http://api.alquran.cloud/ayah/";
    public String PrayerTimesUrl = "http://api.aladhan.com/v1/calendar?";

    public String getPrayerTimesUrl() {
        return PrayerTimesUrl;
    }
    public String getDailyVerseUrl() {
        return DailyVerseUrl;
    }
    public String getQuotesUrl() {
        return QuotesUrl;
    }

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

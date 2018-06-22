package com.mansoor.asmaulhusna.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.activity.MainActivity;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.fragments.DailyVerseFragment;
import com.mansoor.asmaulhusna.utils.DBHelper;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class DailyVerseReceiver extends BroadcastReceiver {

    private static final int MID = 2;
    private MyApplication myapp;
    private Context context;
    private SharedPreferences.Editor editor;
    private String ayath,surath,surathEnglish,arab_text,eng_text;
    private int rnadomAyathNumber;
    private Random random = new Random();
    private DBHelper mydb;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        this.context = context;
        myapp = ((MyApplication) context.getApplicationContext());
        new LoadDailyVerse().execute();



    }

    class LoadDailyVerse extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rnadomAyathNumber = random.nextInt(6200) + 1;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String urlLink = myapp.getDailyVerseUrl();

            try {
                URL url = new URL(urlLink+rnadomAyathNumber+"/en.asad");
                InputStream inputStream = url.openConnection().getInputStream();
                String result = myapp.getStringFromInputStream(inputStream);
                JSONObject json = new JSONObject(result);
                if(json.get("status").equals("OK")){
                    mydb = new DBHelper(context);
                    editor = context.getSharedPreferences("asmaulhusna", MODE_PRIVATE).edit();
                    json = (JSONObject) json.get("data");
                    surath = ((JSONObject)json.get("surah")).get("name").toString();
                    surathEnglish = ((JSONObject)json.get("surah")).get("englishName").toString();
                    ayath = json.get("numberInSurah").toString();
                    eng_text = json.get("text").toString();

                    editor.putString("dailyVers_surath",surath );
                    editor.putString("dailyVers_surath_english",surathEnglish );
                    editor.putString("dailyVers_ayath",ayath );
                    editor.putString("dailyVers_english", eng_text);

                    URL url1 = new URL(urlLink+rnadomAyathNumber);
                    InputStream inputStream1 = url1.openConnection().getInputStream();
                    String result1 = myapp.getStringFromInputStream(inputStream1);
                    JSONObject json1 = new JSONObject(result1);
                    if(json1.get("status").equals("OK")){
                        json1 = (JSONObject) json1.get("data");
                        arab_text = json1.get("text").toString();
                        editor.putString("dailyVers_arabic", arab_text);
                        editor.commit();
                        mydb.insertVerse(ayath, surath, surathEnglish, arab_text, eng_text);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        } // protected Void doInBackground(String... params)

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.putExtra("target_frag", "daily_verse");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.alarm)
                    .setContentTitle("\uD83D\uDCD6 "+"Read an ayath from quran.")
                    .setContentText("Surath: "+surathEnglish+" , ayath: "+ayath).setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationManager.notify(MID, mNotifyBuilder.build());

        }

    }

}

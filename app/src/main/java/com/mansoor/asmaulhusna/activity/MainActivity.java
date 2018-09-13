package com.mansoor.asmaulhusna.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.fragments.AllVerseFragment;
import com.mansoor.asmaulhusna.fragments.ConfigureParyerTimeFragment;
import com.mansoor.asmaulhusna.fragments.DailyVerseFragment;
import com.mansoor.asmaulhusna.fragments.DuaFragment;
import com.mansoor.asmaulhusna.fragments.DuasCardsFragment;
import com.mansoor.asmaulhusna.fragments.HomeFragment;
import com.mansoor.asmaulhusna.fragments.ImageFragment;
import com.mansoor.asmaulhusna.fragments.ImagePostFragment;
import com.mansoor.asmaulhusna.fragments.LecturesFragment;
import com.mansoor.asmaulhusna.fragments.NameDetailsFragment;
import com.mansoor.asmaulhusna.fragments.NamesFragment;
import com.mansoor.asmaulhusna.fragments.NotificationFragment;
import com.mansoor.asmaulhusna.fragments.QiblaFragment;
import com.mansoor.asmaulhusna.fragments.ViewPrayerFragment;
import com.mansoor.asmaulhusna.receivers.AlarmReceiver;
import com.mansoor.asmaulhusna.receivers.DailyVerseReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        NamesFragment.OnFragmentInteractionListener,
        ImagePostFragment.OnFragmentInteractionListener,
        NameDetailsFragment.OnFragmentInteractionListener,
        ImageFragment.OnFragmentInteractionListener,
        DailyVerseFragment.OnFragmentInteractionListener,
        AllVerseFragment.OnFragmentInteractionListener,
        DuasCardsFragment.OnFragmentInteractionListener,
        DuaFragment.OnFragmentInteractionListener,
        ConfigureParyerTimeFragment.OnFragmentInteractionListener,
        ViewPrayerFragment.OnFragmentInteractionListener,
        LecturesFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener,
        QiblaFragment.OnFragmentInteractionListener{


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frame_fragment, HomeFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.allah_names:
                    transaction.replace(R.id.frame_fragment, NamesFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.frame_fragment, ImagePostFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.frame_fragment, DailyVerseFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.navigation_dua:
                    transaction.replace(R.id.frame_fragment, DuasCardsFragment.newInstance("param1","param2"));
                    transaction.commit();
//                case R.id.navigation_lecture:
//                    transaction.replace(R.id.frame_fragment, LecturesFragment.newInstance("param1","param2"));
//                    transaction.commit();
//                    return true;
            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(getIntent().getStringExtra("target_frag") != null && getIntent().getStringExtra("target_frag").equals("names"))
            transaction.replace(R.id.frame_fragment, NamesFragment.newInstance("param1","param2"));
        else if(getIntent().getStringExtra("target_frag") != null && getIntent().getStringExtra("target_frag").equals("daily_verse"))
            transaction.replace(R.id.frame_fragment, DailyVerseFragment.newInstance("param1","param2"));
        else
            transaction.replace(R.id.frame_fragment, HomeFragment.newInstance("param1","param2"));
        transaction.commit();
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        initiateDailyVerseSechduler();
    }

    private void initiateDailyVerseSechduler() {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 58);
//        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE,1);
        Intent intent1 = new Intent(MainActivity.this, DailyVerseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//        Toast.makeText(MainActivity.this,"DailyVerse set successfully!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

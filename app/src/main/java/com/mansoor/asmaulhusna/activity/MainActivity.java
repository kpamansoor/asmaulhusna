package com.mansoor.asmaulhusna.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.fragments.ImageFragment;
import com.mansoor.asmaulhusna.fragments.ImagePostFragment;
import com.mansoor.asmaulhusna.fragments.NameDetailsFragment;
import com.mansoor.asmaulhusna.fragments.NamesFragment;

public class MainActivity extends AppCompatActivity implements NamesFragment.OnFragmentInteractionListener,ImagePostFragment.OnFragmentInteractionListener,NameDetailsFragment.OnFragmentInteractionListener,ImageFragment.OnFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    transaction.replace(R.id.frame_fragment, NamesFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.frame_fragment, ImagePostFragment.newInstance("param1","param2"));
                    transaction.commit();
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_fragment, NamesFragment.newInstance("param1","param2"));
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.mansoor.asmaulhusna.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.adapters.ImagePostsAdapter;
import com.mansoor.asmaulhusna.utils.DBHelper;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class ConfigureParyerTimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Integer LOCATION = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String lat = "", lon = "",timezone = "";
    private LocationManager locationManager ;
    private OnFragmentInteractionListener mListener;
    private TextView tvLocation,tvProgress;
    private Button btnLocation,btnConfigureTime;
    private MyApplication myapp;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MKLoader loader;
    private DBHelper mydb;

    public ConfigureParyerTimeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConfigureParyerTimeFragment newInstance(String param1, String param2) {
        ConfigureParyerTimeFragment fragment = new ConfigureParyerTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(locationListener);
            editor.putString("location", location.getLatitude()+","+location.getLongitude());
            editor.commit();
            btnLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(location.getLatitude()+","+location.getLongitude());
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        final View view = inflater.inflate(R.layout.fragment_configure_paryer_time, container, false);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvProgress = view.findViewById(R.id.tvProgress);
        btnLocation = view.findViewById(R.id.btnLocation);
        btnConfigureTime = view.findViewById(R.id.btnConfigureTime);
        getActivity().setTitle("Prayer times - settings");
        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE).edit();
        tvLocation.setText(prefs.getString("location",""));
        myapp = ((MyApplication) getActivity().getApplicationContext());
        loader = view.findViewById(R.id.loader);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

            }
        });
        btnConfigureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvLocation.getText().length() == 0){
                    Toast.makeText(getActivity(), "Location not set.", Toast.LENGTH_LONG).show();
                }else
                    new LoadTimingTask().execute();

            }
        });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    btnLocation.setVisibility(View.GONE);
                    tvLocation.setText("Loading location...");
                    askForGPS();
                    break;

            }
            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    class LoadTimingTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            btnConfigureTime.setVisibility(View.GONE);
            tvProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(prefs.getString("location","").length() > 0) {
                lat = prefs.getString("location","").split(",")[0];
                lon = prefs.getString("location","").split(",")[1];
            }
            publishProgress("Fetching prayer times...");
            String urlLink = myapp.getPrayerTimesUrl()+"latitude="+lat+"&longitude="+lon+"&method=2&annual=true";

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                publishProgress("Analyzing data...");
                String result = myapp.getStringFromInputStream(inputStream);

                // Inserting prayer times to database.
                JSONObject array = ((JSONObject)new JSONObject(result).get("data"));
                publishProgress("Configuring...");
                updateTimeZone(array);
                mydb = new DBHelper(getActivity());
                mydb.deleteAllPrayerTimes();
                for (int month = 1; month <= 12; ++month) {
                    JSONArray monthArray = (JSONArray) array.get(month+"");
                    JSONObject dayArray;
                    for (int i = 0; i < monthArray.length(); ++i) {
                        dayArray = (JSONObject) ((JSONObject)monthArray.get(i)).get("timings");
                        mydb.insertPrayerTime (((JSONObject)((JSONObject) ((JSONObject)monthArray.get(i)).get("date")).get("gregorian")).get("date").toString(),
                                dayArray.get("Fajr").toString(), dayArray.get("Sunrise").toString(),
                                dayArray.get("Dhuhr").toString(), dayArray.get("Asr").toString(),
                                dayArray.get("Sunset").toString(), dayArray.get("Maghrib").toString(),
                                dayArray.get("Isha").toString());
                    }
                }

            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } // protected Void doInBackground(String... params)

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvProgress.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            loader.setVisibility(View.GONE);
            btnConfigureTime.setVisibility(View.VISIBLE);
            tvProgress.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Prayer time updated!!!", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_fragment, ViewPrayerFragment.newInstance("param1","param2"));
            transaction.commit();
        }

    }

    private void updateTimeZone(JSONObject array) {
        try {
            editor.putString("timezone",
                    ((JSONObject)((JSONObject)((JSONArray) array.get("1")).get(1)).get("meta")).get("timezone").toString());
        } catch (JSONException e) {
            e.printStackTrace();
            editor.putString("timezone","");
        }
        editor.commit();

    }


    @SuppressLint("MissingPermission")
    private void askForGPS() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale( permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                requestPermissions( new String[]{permission}, requestCode);

            } else {

                requestPermissions( new String[]{permission}, requestCode);
            }
        } else {
            btnLocation.setVisibility(View.GONE);
            tvLocation.setText("Loading location...");
            askForGPS();
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.google.android.gms.plus.PlusOneButton;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.models.Prayers;
import com.mansoor.asmaulhusna.utils.DBHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link ViewPrayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPrayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPrayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Menu menu;
    private SharedPreferences prefs;
    private OnFragmentInteractionListener mListener;
    private TextView tvDate,tvFajr,tvSunrise,tvDhuhr,tvAsr,tvSunset,tvMaghrib,tvIsha,tvCurrentPrayerName,tvCurrentPrayerTime;
    private DBHelper mydb;
    private MyApplication myapp;
    private Date date;
    String currentTime;
    DateFormat dateFormat;
    Prayers prayers;
    ImageView leftArrow,rightArrow;
    public ViewPrayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPrayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPrayerFragment newInstance(String param1, String param2) {
        ViewPrayerFragment fragment = new ViewPrayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_prayer, container, false);

        initializeVariables(view);

        showTimes();

        currentTime = myapp.showCurrentPrayer(date,prayers);
        tvCurrentPrayerName.setText("Next : "+currentTime.split(",")[0]);
        tvCurrentPrayerTime.setText(currentTime.split(",")[1]);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prayers != null) {
                    date = myapp.addDays(date, -1);
                    showTimes();
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prayers != null) {
                    date = myapp.addDays(date, 1);
                    showTimes();
                }
            }
        });


        return view;
    }



    private void initializeVariables(View view) {
        getActivity().setTitle("Prayer times");
        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        tvFajr = view.findViewById(R.id.tvFajr);
        tvSunrise = view.findViewById(R.id.tvSunrise);
        tvDhuhr = view.findViewById(R.id.tvDhuhr);
        tvAsr = view.findViewById(R.id.tvAsr);
        tvSunset = view.findViewById(R.id.tvSunset);
        tvMaghrib = view.findViewById(R.id.tvMaghrib);
        tvIsha = view.findViewById(R.id.tvIsha);
        tvDate = view.findViewById(R.id.tvDate);
        leftArrow = view.findViewById(R.id.leftArrow);
        rightArrow = view.findViewById(R.id.rightArrow);
        tvCurrentPrayerName = view.findViewById(R.id.tvCurrentPrayerName);
        tvCurrentPrayerTime = view.findViewById(R.id.tvCurrentPrayerTime);
        myapp = ((MyApplication) getActivity().getApplicationContext());
        // Getting current date
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = new Date();
    }

    private void showTimes() {

        tvDate.setText(myapp.dataToText(dateFormat.format(date)));
        mydb = new DBHelper(getActivity());
        prayers = mydb.getPrayerTimes(dateFormat.format(date));

        if(prayers != null) {
            tvFajr.setText("\uD83D\uDD4C" + myapp.convert24To12(prayers.getFajr().split(" ")[0]));
            tvSunrise.setText("\uD83C\uDF05" + myapp.convert24To12(prayers.getSunrise().split(" ")[0]));
            tvDhuhr.setText("\uD83D\uDD4C" + myapp.convert24To12(prayers.getDhuhr().split(" ")[0]));
            tvAsr.setText("\uD83D\uDD4C" + myapp.convert24To12(prayers.getAsr().split(" ")[0]));
            tvSunset.setText("\uD83C\uDF05" + myapp.convert24To12(prayers.getSunset().split(" ")[0]));
            tvMaghrib.setText("\uD83D\uDD4C" + myapp.convert24To12(prayers.getMaghrib().split(" ")[0]));
            tvIsha.setText("\uD83D\uDD4C" + myapp.convert24To12(prayers.getIsha().split(" ")[0]));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_prayer, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_configure:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_fragment, ConfigureParyerTimeFragment.newInstance("param1","param2"));
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = myapp.app_name+": Prayer timing\nFajr : "+prayers.getFajr()+
                        "\nSunrise : "+prayers.getSunrise().split(" ")[0]+
                        "\nDhuhr : "+prayers.getDhuhr().split(" ")[0]+
                        "\nAsr : "+prayers.getAsr().split(" ")[0]+
                        "\nSunset : "+prayers.getSunset().split(" ")[0]+
                        "\nMaghrib : "+prayers.getMaghrib().split(" ")[0]+
                        "\nIsha : "+prayers.getIsha().split(" ")[0];
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ayath of the day.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(prefs.getString("timezone",""));
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
    public void onPause() {
        super.onPause();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
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

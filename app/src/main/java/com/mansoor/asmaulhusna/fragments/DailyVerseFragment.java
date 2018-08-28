package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.utils.DBHelper;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;


import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class DailyVerseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyApplication myapp;
    private OnFragmentInteractionListener mListener;
    private TextView ayath,surath,arabicText,englishText;
    private SharedPreferences prefs;
    private LinearLayout share;
    private DBHelper mydb;
    private TextView tvViewAll;
    private int versesCount= 0;

    public DailyVerseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DailyVerseFragment newInstance(String param1, String param2) {
        DailyVerseFragment fragment = new DailyVerseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_daily_verse, container, false);
        myapp = ((MyApplication) getActivity().getApplicationContext());
        ayath = view.findViewById(R.id.ayath);
        surath = view.findViewById(R.id.surath);
        arabicText = view.findViewById(R.id.arabicText);
        englishText = view.findViewById(R.id.englishText);
        share = view.findViewById(R.id.share);
        tvViewAll = view.findViewById(R.id.tvViewAll);
        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        mydb = new DBHelper(getActivity());
        getActivity().setTitle("Today's verse");
        ayath.setText(prefs.getString("dailyVers_ayath", "---"));
        surath.setText(prefs.getString("dailyVers_surath", "---")+"("+prefs.getString("dailyVers_surath_english", "---")+")");
        arabicText.setText(prefs.getString("dailyVers_arabic", "---"));
        englishText.setText(prefs.getString("dailyVers_english", "---"));


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = surath.getText()+"\nayath :"+ayath.getText()+".\n"+arabicText.getText()+".\n"+englishText.getText();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ayath of the day.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(versesCount > 0) {
                    AppCompatActivity activity = (AppCompatActivity) getActivity();
                    Fragment myFragment = new AllVerseFragment().newInstance("", "");
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, myFragment).addToBackStack(null).commit();
                }

            }
        });

        versesCount = mydb.numberOfVerses();
        if(versesCount > 0)
            tvViewAll.setText("Click to view all "+versesCount+" verses.");
        else
            tvViewAll.setText("Everyday new quran verse will appear here.");
        return view;
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

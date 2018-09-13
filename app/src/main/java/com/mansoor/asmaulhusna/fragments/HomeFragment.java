package com.mansoor.asmaulhusna.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.models.Notification;
import com.mansoor.asmaulhusna.models.Prayers;
import com.mansoor.asmaulhusna.utils.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private TextView tvTime, tvDate, tvViewAll,tvPrayerName,tvPrayerTime,tvLocation,tvNotifTitle,tvNotifMessage;
    private MyApplication myapp;
    private ImageView ivShare,ivNotifClose;
    private OnFragmentInteractionListener mListener;
    private DBHelper mydb;
    LinearLayout layoutNotificationBlock,layoutAllahNames,layoutQuotes,layoutVerses,layoutDuas,layouNotifications,layouQibla;
    Prayers prayers;
    String notification;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeVariables(view);

        tvViewAll.setText("\uD83D\uDD50 VIEW ALL");
        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        editor = prefs.edit();
        tvLocation.setText(prefs.getString("timezone",""));
        myapp = ((MyApplication) getActivity().getApplicationContext());
        getActivity().setTitle(myapp.app_name);
        final Handler someHandler = new Handler(getContext().getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTime.setText(new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date()));
                tvDate.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
        checkForPrayerTimeDB();
        checkForNotification();
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(Intent.createChooser(sharingIntent, "Share today's prayer times via"));
            }
        });
        ivNotifClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutNotificationBlock.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                layoutNotificationBlock.setVisibility(View.GONE);
                                editor.putString("lastNotifId",notification.split("###")[2]);
                                editor.commit();
                            }
                        });
            }
        });


        return view;
    }

    private void initializeVariables(View view) {
        tvTime = view.findViewById(R.id.tvTime);
        tvDate = view.findViewById(R.id.tvDate);
        tvViewAll = view.findViewById(R.id.tvViewAll);
        tvPrayerName = view.findViewById(R.id.tvPrayerName);
        tvPrayerTime = view.findViewById(R.id.tvPrayerTime);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvNotifTitle = view.findViewById(R.id.tvNotifTitle);
        tvNotifMessage = view.findViewById(R.id.tvNotifMessage);
        ivShare = view.findViewById(R.id.ivShare);
        ivNotifClose = view.findViewById(R.id.ivNotifClose);
        layoutNotificationBlock = view.findViewById(R.id.layoutNotification);
        layoutAllahNames = view.findViewById(R.id.layoutAllahNames);
        layoutQuotes = view.findViewById(R.id.layoutQuotes);
        layoutVerses = view.findViewById(R.id.layoutVerses);
        layoutDuas = view.findViewById(R.id.layoutDuas);
        layouNotifications = view.findViewById(R.id.layouNotifications);
        layouQibla = view.findViewById(R.id.layouQibla);



        // Create an anonymous implementation of OnClickListener
        View.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                switch (v.getId() /*to get clicked view id**/) {
                    case R.id.tvViewAll:
                        transaction.replace(R.id.frame_fragment, ViewPrayerFragment.newInstance("param1","param2"));
                        break;
                    case R.id.tvPrayerName:
                        transaction.replace(R.id.frame_fragment, ConfigureParyerTimeFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layoutAllahNames:
                        transaction.replace(R.id.frame_fragment, NamesFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layoutQuotes:
                        transaction.replace(R.id.frame_fragment, ImagePostFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layoutVerses:
                        transaction.replace(R.id.frame_fragment, DailyVerseFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layoutDuas:
                        transaction.replace(R.id.frame_fragment, DuasCardsFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layouNotifications:
                        transaction.replace(R.id.frame_fragment, NotificationFragment.newInstance("param1","param2"));
                        break;
                    case R.id.layouQibla:
                        transaction.replace(R.id.frame_fragment, QiblaFragment.newInstance("param1","param2"));
                        break;
                    default:
                        break;
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        tvViewAll.setOnClickListener(mClickListener);
        tvPrayerName.setOnClickListener(mClickListener);
        layoutAllahNames.setOnClickListener(mClickListener);
        layoutQuotes.setOnClickListener(mClickListener);
        layoutVerses.setOnClickListener(mClickListener);
        layoutDuas.setOnClickListener(mClickListener);
        layouNotifications.setOnClickListener(mClickListener);
        layouQibla.setOnClickListener(mClickListener);
    }

    private void checkForNotification() {
        notification = mydb.getLastNotification();

        if(notification.length() > 0) {
            if(!prefs.getString("lastNotifId","").equals(notification.split("###")[2])){

                tvNotifTitle.setText(notification.split("###")[0]);
                tvNotifMessage.setText(notification.split("###")[1]);
                layoutNotificationBlock.setVisibility(View.VISIBLE);
            }
            if(prefs.getString("lastNotifId","").equals("")) {
                editor.putString("lastNotifId",notification.split("###")[2]);
                editor.commit();
            }
        }
    }

    private void checkForPrayerTimeDB() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        tvDate.setText(myapp.dataToText(dateFormat.format(date)));
        mydb = new DBHelper(getActivity());
        prayers = mydb.getPrayerTimes(dateFormat.format(date));
        if(prayers != null){
            tvViewAll.setVisibility(View.VISIBLE);
            ivShare.setVisibility(View.VISIBLE);
            String currentTime = myapp.showCurrentPrayer(date, prayers);
            tvPrayerName.setText("Next : "+"\uD83D\uDD4C"+currentTime.split(",")[0]);
            tvPrayerTime.setText(currentTime.split(",")[1]);
        }else{
            tvPrayerName.setText("\uD83D\uDD4C Configure Prayer times.");
            tvPrayerTime.setVisibility(View.VISIBLE);

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

package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.google.android.gms.plus.PlusOneButton;
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
    private SharedPreferences prefs;
    private OnFragmentInteractionListener mListener;
    private TextView tvDate,tvFajr,tvSunrise,tvDhuhr,tvAsr,tvSunset,tvMaghrib,tvIsha;
    private DBHelper mydb;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_prayer, container, false);
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

        // Getting current date
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        tvDate.setText(dataToText(dateFormat.format(date)));
        mydb = new DBHelper(getActivity());
        Prayers prayers = mydb.getPrayerTimes(dateFormat.format(date));
        tvFajr.setText(prayers.getFajr());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(prefs.getString("timezone",""));
        return view;
    }

    private String dataToText(String text) {
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


    @Override
    public void onResume() {
        super.onResume();

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

package com.mansoor.asmaulhusna.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aniket.mutativefloatingactionbutton.MutativeFab;
import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.adapters.AllVerseAdapter;
import com.mansoor.asmaulhusna.adapters.NameAdapter;
import com.mansoor.asmaulhusna.adapters.OnRmoveClickListener;
import com.mansoor.asmaulhusna.models.Verse;
import com.mansoor.asmaulhusna.receivers.AlarmReceiver;
import com.mansoor.asmaulhusna.utils.DBHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class AllVerseFragment extends Fragment implements OnRmoveClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private AllVerseAdapter allVerseAdapter;
    private List<Verse> verses;
    private OnFragmentInteractionListener mListener;
    private DBHelper mydb;
    private  int totalVerses = 0;
    public AllVerseFragment() {
        // Required empty public constructor
    }

    public static AllVerseFragment newInstance(String param1, String param2) {
        AllVerseFragment fragment = new AllVerseFragment();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_allverse_frag, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                    callDeleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callDeleteAll() {
        if(totalVerses > 0)
            new FancyGifDialog.Builder(getActivity())
                .setTitle("Confirm delete!")
                .setMessage("Would you like to delete all the verses?\nIt cannot restore.")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Yes, Delete.")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.giphy)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        if(mydb.deleteAllVerses() > 0) {
                            Toast.makeText(getContext(), "Deleted!!!", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_all_verse, container, false);
        mRecyclerView= view.findViewById(R.id.rv);
        mydb = new DBHelper(getActivity());

        verses = mydb.getAllVerses();
        getActivity().setTitle("All verses");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allVerseAdapter=new AllVerseAdapter(getContext(), verses);
        allVerseAdapter.setListener(this);
        mRecyclerView.setAdapter(allVerseAdapter);
        allVerseAdapter.notifyDataSetChanged();
        totalVerses = mydb.numberOfVerses();

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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRemoveClick(final Verse verse) {
        new FancyGifDialog.Builder(getActivity())
                .setTitle("Confirm remove!")
                .setMessage("Would you like to remove this verse?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Yes, Delete.")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.giphy)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        mydb.deleteVerse(verse.getId());
                        verses = mydb.getAllVerses();
                        allVerseAdapter=new AllVerseAdapter(getContext(), verses);
                        allVerseAdapter.setListener(AllVerseFragment.this);
                        mRecyclerView.setAdapter(allVerseAdapter);
                        allVerseAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Deleted!!!", Toast.LENGTH_SHORT).show();
                        totalVerses--;
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build();

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


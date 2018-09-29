package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.adapters.AllVerseAdapter;
import com.mansoor.asmaulhusna.adapters.DuaAdapter;
import com.mansoor.asmaulhusna.adapters.NotificationAdapter;
import com.mansoor.asmaulhusna.adapters.OnRmoveNotificationClickListener;
import com.mansoor.asmaulhusna.models.Dua;
import com.mansoor.asmaulhusna.models.Notification;
import com.mansoor.asmaulhusna.models.Verse;
import com.mansoor.asmaulhusna.utils.DBHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements OnRmoveNotificationClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private NotificationAdapter notificationDapater;
    private List<Notification> notifications;
    private OnFragmentInteractionListener mListener;
    private DBHelper mydb;
    private TextView noItemMessage;
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mRecyclerView= view.findViewById(R.id.rv);
        noItemMessage= view.findViewById(R.id.noItemMessage);

        getActivity().setTitle("Notifications");
        mydb = new DBHelper(getActivity());
        notifications = mydb.getAllNotification();
        if(notifications.size() > 0) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            notificationDapater = new NotificationAdapter(getContext(), notifications);
            notificationDapater.setListener(this);
            mRecyclerView.setAdapter(notificationDapater);
        }else
            noItemMessage.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onRemoveClick(final Notification notification,final int pos) {

        mydb.deleteNotification(notification.getId());
        notifications.remove(pos);
        if(notifications.size() > 0)
            notificationDapater.notifyItemRemoved(pos);
        else{
            noItemMessage.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_allverse_frag, menu);
        if (notifications != null && notifications.size() > 0){
            menu.findItem(R.id.action_delete).setVisible(true);
        }else{
            menu.findItem(R.id.action_delete).setVisible(false);
        }
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

            new FancyGifDialog.Builder(getActivity())
                    .setTitle("Confirm delete!")
                    .setMessage("Would you like to clear all the notification?")
                    .setNegativeBtnText("Cancel")
                    .setPositiveBtnBackground("#FF4081")
                    .setPositiveBtnText("Yes, Delete.")
                    .setNegativeBtnBackground("#FFA9A7A8")
                    .setGifResource(R.drawable.giphy)   //Pass your Gif here
                    .isCancellable(true)
                    .OnPositiveClicked(new FancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            if(mydb.deleteAllNotification() > 0) {
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

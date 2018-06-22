package com.mansoor.asmaulhusna.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.adapters.ImagePostsAdapter;
import com.mansoor.asmaulhusna.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class ImagePostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1,mParam2,localUrls,remortUrls;
    private RecyclerView mRecyclerView;
    private ImagePostsAdapter namesAdapter;
    private List<String> imagesList;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MyApplication myapp;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    public ImagePostFragment() {
        // Required empty public constructor
    }

    public static ImagePostFragment newInstance(String param1, String param2) {
        ImagePostFragment fragment = new ImagePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_image_post, container, false);
                mRecyclerView= view.findViewById(R.id.rv);
                getActivity().setTitle("Quotes");
        imagesList=new ArrayList<>();
        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE).edit();
        localUrls = prefs.getString("quotes_url", null);
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE);
        myapp = ((MyApplication) getActivity().getApplicationContext());
        if(localUrls != null){
            imagesList = new ArrayList<String>(Arrays.asList(localUrls.split(",")));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            namesAdapter=new ImagePostsAdapter(getContext(),imagesList);

            mRecyclerView.setAdapter(namesAdapter);
        }
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new LoadQuotesTask().execute();
                    }
                }
        );

        new LoadQuotesTask().execute();


        return view;
    }


        class LoadQuotesTask extends AsyncTask<String, String, Boolean> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Boolean doInBackground(String... params) {

//                String urlLink = "https://raw.githubusercontent.com/kpamansoor/api/master/asmaulhusna/quotes.json";
                String urlLink = myapp.getQuotesUrl();

                try {
                    if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                        urlLink = "http://" + urlLink;

                    URL url = new URL(urlLink);
                    InputStream inputStream = url.openConnection().getInputStream();
                    remortUrls = myapp.getStringFromInputStream(inputStream);
                    imagesList = new ArrayList<String>(Arrays.asList(remortUrls.split(",")));


                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                }
                return false;
            } // protected Void doInBackground(String... params)

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                mySwipeRefreshLayout.setRefreshing(false);
                if(localUrls == null){

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    namesAdapter=new ImagePostsAdapter(getContext(),imagesList);
                    mRecyclerView.setAdapter(namesAdapter);
                    editor.putString("quotes_url", remortUrls);
                    editor.commit();
                }
                else if(localUrls != null && !remortUrls.split(",")[0].equals(localUrls.split(",")[0])) {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    namesAdapter=new ImagePostsAdapter(getContext(),imagesList);
                    mRecyclerView.setAdapter(namesAdapter);
                    editor.putString("quotes_url", remortUrls);
                    editor.commit();
                    Toast.makeText(getActivity(),"QuoteBoard updated with new images!",Toast.LENGTH_LONG).show();
                }

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

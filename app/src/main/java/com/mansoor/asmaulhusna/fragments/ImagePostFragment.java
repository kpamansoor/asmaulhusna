package com.mansoor.asmaulhusna.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class ImagePostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;

    private ImagePostsAdapter namesAdapter;

    private List<String> imagesList;

    private OnFragmentInteractionListener mListener;

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
        imagesList=new ArrayList<>();


        imagesList.add("https://i.pinimg.com/564x/04/1b/b7/041bb72596a5627555144c301db8e7e3.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2016/11/18/12/44/wristwatch-1834241__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2015/08/13/17/24/vintage-1950s-887272__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2014/09/03/20/15/legs-434918__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2016/11/22/22/55/buildings-1851047__340.jpg");


        imagesList.add("https://cdn.pixabay.com/photo/2014/12/10/10/27/straw-carts-563005__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2014/07/20/10/40/bottle-397697__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2014/06/17/02/54/car-370173__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2015/04/10/00/39/snack-715534__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2016/08/30/18/45/grilled-1631727__340.jpg");


        imagesList.add("https://cdn.pixabay.com/photo/2013/11/21/12/25/orange-214872__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2016/02/05/15/34/pasta-1181189__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2014/08/20/23/10/raspberries-422979__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2015/02/05/05/58/peanut-624601__340.jpg");
        imagesList.add("https://cdn.pixabay.com/photo/2015/04/10/00/41/food-715539__340.jpg");


        new LoadQuotesTask().execute();


        return view;
    }


        class LoadQuotesTask extends AsyncTask<String, String, Boolean> {

            private ProgressDialog progressDialog = new ProgressDialog(getContext());
            InputStream inputStream = null;
            String result = "";

            @Override
            protected Boolean doInBackground(String... params) {

                String urlLink = "https://raw.githubusercontent.com/kpamansoor/api/master/asmaulhusna/quotes.json";

                try {
                    if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                        urlLink = "http://" + urlLink;

                    URL url = new URL(urlLink);
                    InputStream inputStream = url.openConnection().getInputStream();
                    imagesList = new ArrayList<String>(Arrays.asList(getStringFromInputStream(inputStream).split(",")));

                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                }
                return false;
            } // protected Void doInBackground(String... params)

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                namesAdapter=new ImagePostsAdapter(getContext(),imagesList);

                mRecyclerView.setAdapter(namesAdapter);
            }

            private String getStringFromInputStream(InputStream is) {

                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();

                String line;
                try {

                    br = new BufferedReader(new InputStreamReader(is));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return sb.toString();

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

package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mansoor.asmaulhusna.adapters.ImagePostsAdapter;
import com.mansoor.asmaulhusna.R;

import java.util.ArrayList;
import java.util.List;


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

        imagesList.add("https://cdn.pixabay.com/photo/2016/11/26/00/18/car-1859759__340.jpg");
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



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        namesAdapter=new ImagePostsAdapter(getContext(),imagesList);

        mRecyclerView.setAdapter(namesAdapter);
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

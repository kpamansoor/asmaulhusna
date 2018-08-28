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

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.adapters.DuaAdapter;
import com.mansoor.asmaulhusna.models.Dua;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;


public class DuaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private DuaAdapter duaDapater;
    private List<Dua> duas;
    private OnFragmentInteractionListener mListener;
    public DuaFragment() {
        // Required empty public constructor
    }

    public static DuaFragment newInstance(String param1, String param2) {
        DuaFragment fragment = new DuaFragment();
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
        final View view = inflater.inflate(R.layout.fragment_dua, container, false);
        mRecyclerView= view.findViewById(R.id.rv);
        getActivity().setTitle(mParam1);
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset(mParam2));
            duas = new ArrayList<Dua>();
            Dua dua;
            JSONArray item;
            for (int i = 0; i < array.length(); i++) {
                item = array.getJSONArray(i);


                //Add your values in your `ArrayList` as below:
                dua = new Dua(item.get(0).toString(),item.get(1).toString(),item.get(2).toString(),item.get(3).toString());
                duas.add(dua);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        duaDapater =new DuaAdapter(getContext(), duas);

        mRecyclerView.setAdapter(duaDapater);

        return view;
    }

    public String loadJSONFromAsset(String file_name) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(file_name+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


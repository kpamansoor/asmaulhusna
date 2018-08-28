package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.adapters.DuaCardsAdapter;
import com.mansoor.asmaulhusna.models.DuaCard;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DuasCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DuasCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DuasCardsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DuasCardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DuasCardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DuasCardsFragment newInstance(String param1, String param2) {
        DuasCardsFragment fragment = new DuasCardsFragment();
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
        View view = inflater.inflate(R.layout.fragment_duas_cards, container, false);
        List<DuaCard> rowListItem = getAllItemList();
        GridLayoutManager lLayout;
        lLayout = new GridLayoutManager(getActivity(), 2);
        getActivity().setTitle("Duas");
        RecyclerView rView = (RecyclerView)view.findViewById(R.id.rv);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        DuaCardsAdapter rcAdapter = new DuaCardsAdapter(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private List<DuaCard> getAllItemList(){

        List<DuaCard> allItems = new ArrayList<DuaCard>();
        allItems.add(new DuaCard("Morning & Evening Duas","duas_morning"));
        allItems.add(new DuaCard("Everyday Duas","duas_everyday"));
//        allItems.add(new DuaCard("United Kingdom","prayer"));
//        allItems.add(new DuaCard("Germany","prayer"));
//        allItems.add(new DuaCard("Sweden","prayer"));

        return allItems;
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

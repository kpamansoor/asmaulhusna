package com.mansoor.asmaulhusna.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.activity.MyApplication;
import com.mansoor.asmaulhusna.adapters.AllVerseAdapter;
import com.mansoor.asmaulhusna.adapters.QuranSearchResultAdapter;
import com.mansoor.asmaulhusna.models.QuranSearchResult;
import com.mansoor.asmaulhusna.utils.DBHelper;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuranFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuranFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuranFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etSearch;
    private TextView tvResult;
    private OnFragmentInteractionListener mListener;
    private MKLoader loader;
    private MyApplication myapp;
    private RecyclerView mRecyclerView;
    private QuranSearchResultAdapter quranResultAdapter;
    public QuranFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuranFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuranFragment newInstance(String param1, String param2) {
        QuranFragment fragment = new QuranFragment();
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
        final long delay = 1000; // 1 seconds after user stops typing
        final long[] last_text_edit = {0};
        final Handler handler = new Handler();

        final Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    if(etSearch.getText().length() >= 3)
                        new SearchTask().execute();
                }
            }
        };
        final View view = inflater.inflate(R.layout.fragment_quran, container, false);
        etSearch= view.findViewById(R.id.etSearch);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        etSearch.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count,
                                           int after){
            }
            @Override
            public void onTextChanged ( final CharSequence s, int start, int before,
                                        int count){
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }
            @Override
            public void afterTextChanged ( final Editable s){
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        }

        );
        etSearch.requestFocus();
        loader = view.findViewById(R.id.loader);
        tvResult = view.findViewById(R.id.tvResult);
        myapp = ((MyApplication) getActivity().getApplicationContext());
        mRecyclerView= view.findViewById(R.id.rv);
        return view;
    }

    class SearchTask extends AsyncTask<String, String, Boolean> {

        ArrayList<QuranSearchResult> results = new ArrayList<QuranSearchResult>();
        QuranSearchResult quranSearchResult = new QuranSearchResult();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected Boolean doInBackground(String... params) {

            String urlLink = myapp.QuranSearchUrl+etSearch.getText().toString()+"/all/en.pickthall";

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();

                String result = myapp.getStringFromInputStream(inputStream);

                // Inserting prayer times to database.
                JSONArray array = (JSONArray)((JSONObject)new JSONObject(result).get("data")).get("matches");
                for (int i = 0; i < array.length();i ++){
                    quranSearchResult = new QuranSearchResult();
                    JSONObject item = ((JSONObject)array.get(i));
                    quranSearchResult.setText(item.get("text").toString());
                    quranSearchResult.setSurath(((JSONObject)item.get("surah")).get("englishName")+" ("+((JSONObject)item.get("surah")).get("number")+")");
                    quranSearchResult.setAyath(item.get("numberInSurah").toString());
                    results.add(quranSearchResult);
                }


            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } // protected Void doInBackground(String... params)

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            loader.setVisibility(View.GONE);
            tvResult.setText("Showing "+results.size()+" results");
            tvResult.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            quranResultAdapter=new QuranSearchResultAdapter(getContext(), results, etSearch.getText().toString());
//            quranResultAdapter.setListener(getA);
            mRecyclerView.setAdapter(quranResultAdapter);
            quranResultAdapter.notifyDataSetChanged();
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

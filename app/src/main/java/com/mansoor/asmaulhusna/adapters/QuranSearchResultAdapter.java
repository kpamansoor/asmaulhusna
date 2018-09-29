package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.models.QuranSearchResult;
import com.mansoor.asmaulhusna.models.Verse;
import com.mansoor.asmaulhusna.utils.DBHelper;

import java.util.List;


public class QuranSearchResultAdapter extends RecyclerView.Adapter<QuranSearchResultAdapter.CardViewHolder>{

    private List<QuranSearchResult> list;
    private Context mContext;
    private QuranSearchResult result;
    String text,highlighetAs;
    OnRmoveClickListener listener;
    public void setListener(OnRmoveClickListener listener) {
        this.listener = listener;
    }
    public QuranSearchResultAdapter(Context mContext, List<QuranSearchResult> list, String text) {
        this.mContext=mContext;
        this.list = list;
        this.text =  text ;
        this.highlighetAs = "<font color='red'>" + text + "</font>";
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quran_result_single_item, parent, false);
        final CardViewHolder holder = new CardViewHolder(v);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pos = holder.getAdapterPosition();
//                QuranSearchResult verse = list.get(pos);
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = verse.getSurath()+"\nayath :"+verse.getAyath()+".\n"+verse.getArabicText()+".\n"+verse.getEnglishText();
//                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Ayath of the day.");
//                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//                mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }

        });

        return holder;
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
//        line = list.get(position).split("#");
        result = list.get(position);
        holder.text.setText(Html.fromHtml(result.getText().replaceAll(text,highlighetAs)));
        holder.surath.setText(result.getSurath()+", Ayath : "+result.getAyath());
        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView surath,text;
        private ImageView share;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.surath=itemView.findViewById(R.id.surath);
            this.share=itemView.findViewById(R.id.share);
            this.text=itemView.findViewById(R.id.text);
        }

        public View getView() {
            return view;
        }
    }
}

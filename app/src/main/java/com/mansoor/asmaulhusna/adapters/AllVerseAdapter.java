package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.fragments.NameDetailsFragment;
import com.mansoor.asmaulhusna.models.Verse;
import com.mansoor.asmaulhusna.utils.DBHelper;

import java.util.List;
import java.util.Random;


public class AllVerseAdapter extends RecyclerView.Adapter<AllVerseAdapter.CardViewHolder>{

    private List<Verse> list;
    private Context mContext;
    private Verse verse;
    private DBHelper mydb;
    String[] line;
    OnRmoveClickListener listener;
    public void setListener(OnRmoveClickListener listener) {
        this.listener = listener;
    }
    public AllVerseAdapter(Context mContext, List<Verse> list) {
        this.mContext=mContext;
        this.list = list;
        mydb = new DBHelper(mContext);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verse_single_item, parent, false);
        final CardViewHolder holder = new CardViewHolder(v);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Verse verse = list.get(pos);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = verse.getSurath()+"\nayath :"+verse.getAyath()+".\n"+verse.getArabicText()+".\n"+verse.getEnglishText();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ayath of the day.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }

        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Verse verse = list.get(pos);

                if(listener != null) {
                    listener.onRemoveClick(verse);
                }
            }

        });
        return holder;
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
//        line = list.get(position).split("#");
        verse = list.get(position);
        holder.ayath.setText(verse.getAyath());
        holder.surath.setText(verse.getSurath());
        holder.arabicText.setText(verse.getArabicText());
        holder.englishText.setText(verse.getEnglishText());
        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView ayath,surath,arabicText,englishText;
        private LinearLayout share,remove;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.ayath=itemView.findViewById(R.id.ayath);
            this.surath=itemView.findViewById(R.id.surath);
            this.arabicText=itemView.findViewById(R.id.arabicText);
            this.englishText=itemView.findViewById(R.id.englishText);
            this.share=itemView.findViewById(R.id.share);
            this.remove=itemView.findViewById(R.id.remove);
        }

        public View getView() {
            return view;
        }
    }
}

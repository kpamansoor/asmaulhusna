package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.models.Dua;
import com.mansoor.asmaulhusna.models.Verse;
import com.mansoor.asmaulhusna.utils.DBHelper;

import java.util.List;


public class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.CardViewHolder>{

    private List<Dua> list;
    private Context mContext;
    private Dua dua;
    OnRmoveClickListener listener;
    public void setListener(OnRmoveClickListener listener) {
        this.listener = listener;
    }
    public DuaAdapter(Context mContext, List<Dua> list) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.duas_single_item, parent, false);
        final CardViewHolder holder = new CardViewHolder(v);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pos = holder.getAdapterPosition();
//                Dua dua = list.get(pos);
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = dua.getSurath()+"\nayath :"+dua.getAyath()+".\n"+dua.getArabicText()+".\n"+dua.getEnglishText();
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
        dua = list.get(position);
        holder.duaName.setText(dua.getDuaName());
        holder.duaArabic.setText(dua.getDuaArabic());
        holder.duaEnglish.setText(dua.getDuaEnglish());
        holder.duaBenefits.setText(dua.getDuaBenefits());
        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView duaName,duaArabic,duaEnglish,duaBenefits;
        private ImageView share;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.duaName=itemView.findViewById(R.id.duaName);
            this.duaArabic=itemView.findViewById(R.id.duaArabic);
            this.duaEnglish=itemView.findViewById(R.id.duaEnglish);
            this.duaBenefits=itemView.findViewById(R.id.duaBenefits);
            this.share=itemView.findViewById(R.id.share);
        }

        public View getView() {
            return view;
        }
    }
}

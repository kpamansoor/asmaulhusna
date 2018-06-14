package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.List;
import java.util.Random;


public class NameAdapter extends RecyclerView.Adapter<NameAdapter.CardViewHolder>{

    private List<String> list;
    Random random = new Random();
    private Context mContext;
    String[] line;
    public NameAdapter(Context mContext, List<String> list) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_card_single_item, parent, false);
        final CardViewHolder holder = new CardViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int pos = holder.getAdapterPosition();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new NameDetailsFragment().newInstance(list.get(pos),"");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, myFragment).addToBackStack(null).commit();

            }

        });
        return holder;
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
//        Picasso.with(mContext).load(list.get(position)).into(holder.text);
        line = list.get(position).split("#");
        holder.name.setText(line[1]);
        holder.english.setText(line[0]);
        holder.meaning.setText(line[2]);
        switch (random.nextInt(6 - 1 + 1) + 1){
            case 1:
                holder.card.setBackgroundResource(R.color.material_blue);
                break;
            case 2:
                holder.card.setBackgroundResource(R.color.material_green);
                break;
            case 3:
                holder.card.setBackgroundResource(R.color.material_green2);
                break;
            case 4:
                holder.card.setBackgroundResource(R.color.material_red);
                break;
            case 5:
                holder.card.setBackgroundResource(R.color.material_lime);
                break;
            case 6:
                holder.card.setBackgroundResource(R.color.material_orange);
                break;
            default:
                holder.card.setBackgroundResource(R.color.material_orange);
                break;
        }

        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        protected ImageView text;
        protected LinearLayout card;
        protected TextView name;
        protected TextView english;
        protected TextView meaning;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.text=itemView.findViewById(R.id.text);
            this.card=itemView.findViewById(R.id.card);
            this.name=itemView.findViewById(R.id.name);
            this.english=itemView.findViewById(R.id.english);
            this.meaning=itemView.findViewById(R.id.meaning);
            this.meaning.setSelected(true);
        }

        public View getView() {
            return view;
        }
    }
}

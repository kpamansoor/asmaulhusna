package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.fragments.DuaFragment;
import com.mansoor.asmaulhusna.models.DuaCard;

import java.util.List;

/**
 * Created by L4208412 on 27/6/2018.
 */

public class DuaCardsAdapter extends RecyclerView.Adapter<DuaCardsAdapter.DuaCardsViewHolder> {

    private List<DuaCard> itemList;
    private Context context;
    String imageString;

    public DuaCardsAdapter(Context context, List<DuaCard> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public DuaCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.duas_cards_single_item, null);
        final DuaCardsViewHolder holder = new DuaCardsViewHolder(layoutView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new DuaFragment().newInstance(itemList.get(pos).getName(),itemList.get(pos).getImage());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, myFragment).addToBackStack(null).commit();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DuaCardsViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getName());
        imageString = itemList.get(position).getImage();
        switch (imageString){
            case "duas_morning" :
                holder.image.setImageResource(R.drawable.morning);
                break;
            case "duas_everyday" :
                holder.image.setImageResource(R.drawable.everyday);
                break;

            default:
                holder.image.setImageResource(R.drawable.dua_hand);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class DuaCardsViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView image;

        public DuaCardsViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            name = (TextView)itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

//        @Override
//        public void onClick(View view) {
//
//            AppCompatActivity activity = (AppCompatActivity) view.getContext();
//            Fragment myFragment = new DuaFragment().newInstance("","");
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, myFragment).addToBackStack(null).commit();
//
//        }
    }
}

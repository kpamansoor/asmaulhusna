package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mansoor.asmaulhusna.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImagePostsAdapter extends RecyclerView.Adapter<ImagePostsAdapter.CardViewHolder>{

    private List<String> list;

    private Context mContext;
    public ImagePostsAdapter(Context mContext, List<String> list) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_single_item, parent, false));
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Picasso.with(mContext).load(list.get(position)).into(holder.text);
//        holder.text.setText("hello");
        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        protected ImageView text;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.text=itemView.findViewById(R.id.text);
        }

        public View getView() {
            return view;
        }
    }
}

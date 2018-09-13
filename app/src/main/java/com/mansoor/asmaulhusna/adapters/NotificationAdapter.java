package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.models.Dua;
import com.mansoor.asmaulhusna.models.Notification;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CardViewHolder>{

    private List<Notification> notifications;
    private Context mContext;
    private Notification notification;
    private OnRmoveNotificationClickListener listener;
    public void setListener(OnRmoveNotificationClickListener listener) {
        this.listener = listener;
    }
    public NotificationAdapter(Context mContext, List<Notification> notifications) {
        this.mContext=mContext;
        this.notifications = notifications;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_single_item, parent, false);
        final CardViewHolder holder = new CardViewHolder(v);
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Notification notification = notifications.get(pos);
                if(listener != null) {
                    listener.onRemoveClick(notification,pos);
                }

            }

        });
        return holder;
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
//        line = list.get(position).split("#");
        notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.date.setText(notification.getDate());
        holder.getView().setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    protected class CardViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView title,message,date;
        private ImageView close;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.title=itemView.findViewById(R.id.notTitle);
            this.message=itemView.findViewById(R.id.notMessage);
            this.date=itemView.findViewById(R.id.notDate);
            this.close=itemView.findViewById(R.id.close);
        }

        public View getView() {
            return view;
        }
    }
}

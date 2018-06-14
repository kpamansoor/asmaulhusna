package com.mansoor.asmaulhusna.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.fragments.ImageFragment;
import com.mansoor.asmaulhusna.fragments.NameDetailsFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ImagePostsAdapter extends RecyclerView.Adapter<ImagePostsAdapter.CardViewHolder>{

    private List<String> list;

    private Context mContext;
    public ImagePostsAdapter(Context mContext, List<String> list) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card_single_item, parent, false);
        final ImagePostsAdapter.CardViewHolder holder = new ImagePostsAdapter.CardViewHolder(v);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
               shareImage(list.get(pos),mContext);
            }


        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Picasso.with(mContext)
                        .load(list.get(pos))
                        .into(new PhotoLoader(list.get(pos).substring(list.get(pos).lastIndexOf('/') + 1),parent));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new ImageFragment().newInstance(list.get(pos),"");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, myFragment).addToBackStack(null).commit();
            }
        });
        return holder;
    }
    public void shareImage(String url, final Context context) {
        Picasso.with(context).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                context.startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public class PhotoLoader implements Target {
        private final String name;
        private ImageView imageView;
        private View v;
        public PhotoLoader(String name,View v) {
            this.name = name;
            this.v = v;
//            this.imageView = imageView;
        }
        @Override
        public void onPrepareLoad(Drawable arg0) {
        }
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + name);
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                ostream.close();
//                imageView.setImageBitmap(bitmap);
                Toast.makeText(v.getContext(), "Image downloaded!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onBitmapFailed(Drawable arg0) {
        }
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
        protected LinearLayout share;
        protected LinearLayout download;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.text=itemView.findViewById(R.id.text);
            this.share=itemView.findViewById(R.id.share);
            this.download=itemView.findViewById(R.id.download);
        }

        public View getView() {
            return view;
        }
    }
}

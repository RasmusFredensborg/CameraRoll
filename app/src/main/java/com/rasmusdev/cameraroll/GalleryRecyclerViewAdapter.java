package com.rasmusdev.cameraroll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "GalleryRecyclerView";

    private ArrayList<GalleryItem> mDataSet;
    private Context mContext;
    private int lastPosition = -1;

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    void setDataSet(ArrayList<GalleryItem> dataSet) {
        this.mDataSet = dataSet;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private View rootView;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            rootView = v.getRootView();
            imageView = v.findViewById(R.id.image);
        }

        void clearAnimation()
        {
            rootView.clearAnimation();
        }

        ImageView getImageView() {
            return imageView;
        }
    }

    GalleryRecyclerViewAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        /** Custom animation:
         * Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
         * */

        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Glide.with(mContext)
                .load(mDataSet.get(position).getPath())
                .asBitmap()
                .into(viewHolder.getImageView());
//        setAnimation(viewHolder.getImageView(), position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
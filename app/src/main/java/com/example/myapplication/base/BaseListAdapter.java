package com.example.myapplication.base;

import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

public abstract class BaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected int VIEW_FOOTER = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_FOOTER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return getViewHolder(parent);
    }

     public abstract RecyclerView.ViewHolder getViewHolder(ViewGroup parent);

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private ImageView loadImage;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            loadImage = itemView.findViewById(R.id.loading_img);
        }
    }

    public void loadingAnimation(FooterViewHolder holder) {
        AnimationDrawable loadAnimation = (AnimationDrawable) holder.loadImage.getDrawable();
        loadAnimation.start();
    }
}
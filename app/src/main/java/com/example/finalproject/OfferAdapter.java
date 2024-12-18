package com.example.finalproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OfferItem> offerList;
    private Context context;

    public OfferAdapter(List<OfferItem> offerList) {
        this.offerList = offerList;
    }

    @Override
    public int getItemViewType(int position) {
        return offerList.get(position).getType(); // Use getType() from OfferItem
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if (viewType == OfferItem.TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_design, parent, false);
            return new ImageViewHolder(view);
        } else { 
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_design, parent, false);
            return new VideoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OfferItem item = offerList.get(position); // Get the current item

        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(item.getImageRes()); // Use getImageRes()
            holder.itemView.findViewById(R.id.offerImage).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.offerVideo).setVisibility(View.GONE);
        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).bind(item.getVideoUri()); // Use getVideoUri()
            holder.itemView.findViewById(R.id.offerImage).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.offerVideo).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView offerImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            offerImage = itemView.findViewById(R.id.offerImage);
        }

        public void bind(int imageRes) {
            offerImage.setImageResource(imageRes);
        }
    }

    // ViewHolder for video items
    public class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView offerVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            offerVideo = itemView.findViewById(R.id.offerVideo);
        }

        public void bind(Uri videoUri) {
            offerVideo.setVideoURI(videoUri);
            offerVideo.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                offerVideo.start();
            });
        }
    }
}

package com.example.finalproject;

import android.net.Uri;

public class OfferItem {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private int type;
    private int imageRes; // Only used for images
    private Uri videoUri; // Only used for videos

    public OfferItem(int imageRes) {
        this.type = TYPE_IMAGE;
        this.imageRes = imageRes;
    }

    public OfferItem(Uri videoUri) {
        this.type = TYPE_VIDEO;
        this.videoUri = videoUri;
    }

    public int getType() {
        return type;
    }

    public int getImageRes() {
        return imageRes;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

}

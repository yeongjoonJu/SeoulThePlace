package com.ensharp.seoul.seoultheplace;

import android.content.Context;

import android.media.Image;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoImage {


    public static void DownLoadImage(String[] URLS, ImageView image){
        Picasso.get().load(URLS[0]).into(image, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {
                Picasso.get().load(URLS[1]).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(URLS[2]).into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                });
            }
        });
        return;
    }
    public static void DownLoadImage(String URL, ImageView image){
        Picasso.get().load(URL).into(image);
    }
}

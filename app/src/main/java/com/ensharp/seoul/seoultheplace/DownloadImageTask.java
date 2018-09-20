package com.ensharp.seoul.seoultheplace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;

    public DownloadImageTask(ImageView image) {
        Log.d("TestImage : ", "new DownloadIamgeTask");
        this.image = image;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Log.e("TestImage : ", "doInBackground");
        String imageURL = urls[0];
        Bitmap bitmap = null;

        Log.e("TestImage : ", "downloadImage");
        InputStream inputStream = null;
        try {
            inputStream = new java.net.URL(imageURL).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(inputStream);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}

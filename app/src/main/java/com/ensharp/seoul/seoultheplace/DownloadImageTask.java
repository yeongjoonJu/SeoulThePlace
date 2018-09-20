package com.ensharp.seoul.seoultheplace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

    private ImageView image;

    public DownloadImageTask(ImageView image) {
        Log.d("TestImage : ", "new DownloadIamgeTask");
        this.image = image;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Log.e("TestImage : ", "doInBackground");
        Bitmap bitmap = null;
        for(int i = 0 ; i < 3; i++) {
            try {
                Log.e("TestImage : ", "downloadImage");
                InputStream inputStream = new java.net.URL(urls[i]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                break;
            } catch (IOException e) {
                Log.e("TestImage : ", "Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}

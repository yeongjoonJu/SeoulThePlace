package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter<String> {
    private Button preChoicedButton = null;

    public TagAdapter(Activity context, ArrayList<String> tags) { super(context, 0, tags); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.tagbutton, parent, false);
        }

        String currentTag = getItem(position);

        final Button tagButton = (Button) listItemView.findViewById(R.id.tagButton);
        tagButton.setText(currentTag);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
                tagButton.setTextColor(Color.WHITE);

                // 이전에 선택되어있던 버튼의 색을 변경
                if(preChoicedButton != null) {
                    preChoicedButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_unchoicedtag));
                    preChoicedButton.setTextColor(0xFF777788);
                }
                preChoicedButton = tagButton;
            }
        });



        return listItemView;
    }
}

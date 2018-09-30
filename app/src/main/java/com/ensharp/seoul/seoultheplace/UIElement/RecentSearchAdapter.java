package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class RecentSearchAdapter extends ArrayAdapter<String> {
    private EditText editText;
    private MainFragment mainFragment;

    public RecentSearchAdapter(Activity context, MainFragment mainFragment, ArrayList<String> recents) {
        super(context, 0, recents);
        this.mainFragment = mainFragment;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.recent_search_layout, parent, false);
        }
        final TextView word = (TextView) listItemView.findViewById(R.id.recent_word);
        word.setText(getItem(position));

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(word.getText());
                mainFragment.searchPlaceAndCourse();
            }
        });

        return listItemView;
    }
}

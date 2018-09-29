package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter<String> {
    private Button preChoicedButton = null;
    private MainFragment mainFragment = null;
    // 선택된 타입
    private String choicedType = null;

    public TagAdapter(Activity context, ArrayList<String> tags) {
        super(context, 0, tags);
    }

    public void setMainFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
        mainFragment.renewCardView(getItem(0));
        choicedType = getItem(0);
    }

    public Button getTagButton() {
        return preChoicedButton;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.tagbutton, parent, false);
        }

        String currentTag = "#" + getItem(position);
        final Button tagButton = (Button) listItemView.findViewById(R.id.tagButton);

        if(!currentTag.equals(choicedType)) {
            tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_unchoicedtag));
            tagButton.setTextColor(0xFF777788);
        }else {
            tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
            tagButton.setTextColor(Color.BLACK);
        }
        tagButton.setText(currentTag);

        // 아무 것도 선택 안 되어 있을 때
        if(preChoicedButton == null && position == 0) {
            tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
            tagButton.setTextColor(Color.BLACK);
            preChoicedButton = tagButton;
        }

        final int currentPosition = position;

        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 같은 버튼을 클릭했다면
                if(preChoicedButton != null && preChoicedButton.equals(tagButton))
                    return;

                choicedType = tagButton.getText().toString();
                tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
                tagButton.setTextColor(Color.BLACK);

                //이전에 선택되어있던 버튼의 색을 변경
                if(preChoicedButton != null) {
                    preChoicedButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_unchoicedtag));
                    preChoicedButton.setTextColor(0xFF777788);
                }
                preChoicedButton = tagButton;

                mainFragment.renewCardView(getItem(position));
            }
        });

        return listItemView;
    }
}

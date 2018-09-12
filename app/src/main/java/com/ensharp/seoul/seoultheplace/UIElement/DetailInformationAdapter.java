package com.ensharp.seoul.seoultheplace.UIElement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.DetailInformationVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;
import java.util.List;

public class DetailInformationAdapter extends ArrayAdapter<DetailInformationVO> {

    private Context context;
    private List<DetailInformationVO> informationList = new ArrayList<>();

    public DetailInformationAdapter(@NonNull Context context, ArrayList<DetailInformationVO> list) {
        super(context, 0, list);
        this.context = context;
        informationList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_place_category, parent, false);

        DetailInformationVO currentInformation = informationList.get(position);

        ImageView image = (ImageView) listItem.findViewById(R.id.icon);
        TextView name = (TextView) listItem.findViewById(R.id.catetory);
        TextView content = (TextView) listItem.findViewById(R.id.information);

        int id = content.getResources().getIdentifier("com.ensharp.seoul.seoultheplace:drawable/" + currentInformation.getImageSource(), null, null);

        image.setImageResource(id);
        name.setText(currentInformation.getCategory());
        content.setText(currentInformation.getDetail());

        return listItem;
    }
}

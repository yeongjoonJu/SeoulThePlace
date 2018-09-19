package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
import com.ensharp.seoul.seoultheplace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter<String> {
    private Button preChoicedButton = null;
    private ListView mainListView = null;
    private Context mainFragmentActivity = null;
    private DAO dao = null;
    private String useremail = null;

    public TagAdapter(Activity context, ArrayList<String> tags) {
        super(context, 0, tags);
        dao = new DAO();
    }

    public ArrayList<CourseVO> getCourseByType(String type, String user) {
        JSONArray jsonArray = dao.getUserCourseData(type, user);
        if (jsonArray == null)
            return null;
        ArrayList<CourseVO> courses = new ArrayList<CourseVO>();
        try {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                courses.add(new CourseVO(jsonObject));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // 메인 리스트 셋팅
    public void setMainListView(Context context, ListView mainListView) {
        this.mainListView = mainListView;
        this.mainFragmentActivity = context;

        SharedPreferences preferences = context.getSharedPreferences("data", context.MODE_PRIVATE);
        useremail = preferences.getString("email", null);
        ArrayList<CourseVO> courses = getCourseByType(getItem(0), useremail);
        if(courses != null) {
            CourseAdapter courseAdapter = new CourseAdapter(mainFragmentActivity, courses);
            mainListView.setAdapter(courseAdapter);
        }
    }

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

        // 아무 것도 선택 안 되어 있을 때
        if(preChoicedButton == null && position == 0) {
            tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
            tagButton.setTextColor(Color.WHITE);
            preChoicedButton = tagButton;
        }

        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 같은 버튼을 클릭했다면
                if(preChoicedButton != null && preChoicedButton.equals(tagButton))
                    return;

                tagButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_choicedtag));
                tagButton.setTextColor(Color.WHITE);

                //이전에 선택되어있던 버튼의 색을 변경
                if(preChoicedButton != null) {
                    preChoicedButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_unchoicedtag));
                    preChoicedButton.setTextColor(0xFF777788);
                }
                preChoicedButton = tagButton;

                // 타입에 따라 데이터를 불러온다
                ArrayList<CourseVO> courses = getCourseByType(getItem(0), useremail);
                if(courses != null) {
                    CourseAdapter courseAdapter = new CourseAdapter(mainFragmentActivity, courses);
                    mainListView.setAdapter(courseAdapter);
                }
            }
        });

        return listItemView;
    }
}

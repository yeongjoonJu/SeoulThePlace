package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.EdittedCourseVO;
import com.ensharp.seoul.seoultheplace.FavoriteVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.AddFloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionsMenu;
import com.ensharp.seoul.seoultheplace.UIElement.SwipeDismissListViewTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomizedFragment extends Fragment {

    private DAO dao = new DAO();
    private View rootView;
    private ListView listView;
    private List<EdittedCourseVO> customizedCourses = new ArrayList<EdittedCourseVO>();
    private CustomizedCourseAdapter adapter;
    private boolean isExpanded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customized, container, false);
        listView = (ListView) rootView.findViewById(R.id.customized_list);

        final MainActivity activity = (MainActivity) getActivity();

//        customizedCourses = dao.getCustomizedCourses(getUserID());

        Log.e("editted_course/CustomizedFragment", String.format("is costomizedCourses null? %s", customizedCourses == null));
        Log.e("editted_course/CustomizedFragment", String.format("length of customizedCourses = %d", customizedCourses.size()));

        adapter = new CustomizedCourseAdapter(getContext(), 0, customizedCourses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                if (isExpanded) return false;

                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                if (isExpanded) return;

                for (int position : reverseSortedPositions) {
                    dao.deleteEdittedCourse(getUserID(), customizedCourses.get(position).getCode());
                    customizedCourses.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnTouchListener(touchListener);

        return rootView;
    }

    public void initCustomizedCourses() {

    }

    public ListView getListView() {
        return listView;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (isExpanded) return;

            Object object = listView.getItemAtPosition(position);
            FavoriteVO favorite = (FavoriteVO) object;
            Toast.makeText(getContext(), ((FavoriteVO) object).getName(), Toast.LENGTH_SHORT).show();
        }
    };

    public String getUserID () {
        MainActivity activity = (MainActivity) getActivity();

        return activity.getUserID();
    }

}
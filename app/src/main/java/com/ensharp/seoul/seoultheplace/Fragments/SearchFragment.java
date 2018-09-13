package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class SearchFragment extends Fragment {
    static final String KEY_ADAPTER = "KEY_ADAPTER";
    static final String KEY_PAGER = "KEY_PAGER";

    InputMethodManager inputMethodManager;
    TextView courceText;
    TextView placeText;
    EditText searchEditText;

    ViewPager courseViewPager;
    ViewPager placeViewPager;
    CardFragmentPagerAdapter courseViewAdapter;
    PlaceFragmentPagerAdapter placeViewAdapter;

    ArrayList<PlaceVO> searchResult = null;

    public SearchFragment() {
        // Required empty public constructor
    }

    /*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_ADAPTER, (Serializable)placeViewAdapter);
        outState.putSerializable(KEY_PAGER, (Serializable)placeViewPager);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        /*
        if(savedInstanceState != null) {
            placeViewAdapter = (PlaceFragmentPagerAdapter) savedInstanceState.getSerializable(KEY_ADAPTER);
            placeViewPager = (ViewPager) savedInstanceState.getSerializable(KEY_PAGER);
            courceText.setVisibility(View.VISIBLE);
            placeText.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // 화면 구성 요소
        ImageButton searchButton = (ImageButton) rootView.findViewById(R.id.search_button);
        courceText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edittext);

        // 플레이스 카드 뷰
        placeViewPager = (ViewPager) rootView.findViewById(R.id.place_search_result);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courceText.setVisibility(View.VISIBLE);
                placeText.setVisibility(View.VISIBLE);

                // 플레이스 임시 데이터
                PlaceVO[] placeData = new PlaceVO[]{
                        new PlaceVO("j12315", "롯데월드1", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드2", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드3", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드4", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드5", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드6", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드7", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00")
                };

                ArrayList<PlaceVO> tempData = new ArrayList<PlaceVO>();
                for(int i=0;i<placeData.length; i++) {
                    tempData.add(placeData[i]);
                }

                searchResult = tempData;
                showPlaceCardView(searchResult);

                // 키보드 숨김
                inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
        });
        return rootView;
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(ArrayList<PlaceVO> places) {
        placeViewAdapter = new PlaceFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()));
        placeViewAdapter.setPlaceData(places);
        ShadowTransformer placeCardShadowTransformer = new ShadowTransformer(placeViewPager, placeViewAdapter);
        placeViewPager.setPageTransformer(false, placeCardShadowTransformer);
        try {
            placeViewPager.setAdapter(placeViewAdapter);
        }catch(Exception e) {
            e.printStackTrace();
            placeViewAdapter = new PlaceFragmentPagerAdapter(getFragmentManager(), dpToPixels(2, getActivity()));
            placeViewAdapter.setPlaceData(places);
        }
        placeViewPager.setOffscreenPageLimit(3);
        placeCardShadowTransformer.enableScaling(true);
    }
}

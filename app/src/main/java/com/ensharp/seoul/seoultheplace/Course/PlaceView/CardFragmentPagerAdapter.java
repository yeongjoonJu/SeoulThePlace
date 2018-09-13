package com.ensharp.seoul.seoultheplace.Course.PlaceView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ensharp.seoul.seoultheplace.Fragments.CardFragment;
import com.ensharp.seoul.seoultheplace.Fragments.CourseCardFragment;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceCardFragment;
import com.ensharp.seoul.seoultheplace.PlaceVO;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private float baseElevation;
    private List<ImageButton> imageButtons;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        PlaceVO place1 = new PlaceVO("j12315", "롯데월드", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg",	"http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00");
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        imageButtons = new ArrayList<>();

        addCardFragment(new CourseCardFragment());
        for(int i = 0; i < 7; i++){
            PlaceCardFragment placeCard = new PlaceCardFragment();
            placeCard.setPosition(i+1);
            placeCard.setData(place1);
            addCardFragment(placeCard);
            imageButtons.add(placeCard.getPlaceButton());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        CardFragment card = fragments.get(position);
        if (card instanceof CourseCardFragment)
            return ((CourseCardFragment)card).getCardView();
        else
            return ((PlaceCardFragment)card).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        CardFragment card = fragments.get(position);
        if (card instanceof CourseCardFragment)
            return (CourseCardFragment)card;
        else
            return (PlaceCardFragment)card;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public List<ImageButton> getImageButtons() {return imageButtons;}

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}

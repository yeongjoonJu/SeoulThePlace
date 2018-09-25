package com.ensharp.seoul.seoultheplace.Course.PlaceView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceCardFragment;
import com.ensharp.seoul.seoultheplace.PlaceVO;

import java.util.ArrayList;
import java.util.List;

public class PlaceFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    List<PlaceCardFragment> placeCards;
    private float baseElevation;

    public PlaceFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        placeCards = new ArrayList<>();
        this.baseElevation = baseElevation;
    }

    public void setPlaceData(ArrayList<PlaceVO> places) {
        for(int i = 0; i < places.size(); i++){
            PlaceCardFragment placeCard = new PlaceCardFragment();
            placeCard.setPosition(i);
            placeCard.setData(places.get(i));
            addCardFragment(placeCard);
        }
    }

    @Override
    public CardView getCardViewAt(int position) {
        return placeCards.get(position).getCardView();
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    public void addCardFragment(PlaceCardFragment fragment) {
        placeCards.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return placeCards.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Object fragment = super.instantiateItem(container, position);
        placeCards.set(position, (PlaceCardFragment)fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return placeCards.size();
    }
}

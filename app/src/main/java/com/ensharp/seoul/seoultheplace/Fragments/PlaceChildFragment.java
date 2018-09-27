package com.ensharp.seoul.seoultheplace.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.DetailInformationVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.DetailInformationAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.PlaceViewPagerAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("ValidFragment")
public class PlaceChildFragment extends Fragment {
    public static final int VIA_SEARCH = 0;
    public static final int VIA_COURSE = 1;
    public static final int DURING_EDITTING_COURSE = 2;

    private DAO dao = new DAO();
    private View rootView;
    private PullToRefreshView destroyView;

    private CourseVO course;
    private PlaceVO place;
    private int index;
    private int enterRoute;

    // for images
    private ViewPager imageViewPager;
    private LinearLayout sliderDotsPanel;
    private PlaceViewPagerAdapter placeViewPagerAdapter;
    private ImageView[] dots;
    private int dotCount;

    @SuppressLint("ValidFragment")
    public PlaceChildFragment(CourseVO course, PlaceVO place, int index, int enterRoute) {
        this.course = course;
        this.place = place;
        this.index = index;
        this.enterRoute = enterRoute;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_place_child, container, false);

        setDestroyView();
        setPlaceImages(place.getImageURL());
        setImageIndicators();
        setButtons();
        setElements();
        setPlaceCountImage();
        setMap();

        return rootView;
    }

    public void setDestroyView() {
        destroyView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_destroy);
        destroyView.setOnRefreshListener(onPullToDestroyListener);

        if (enterRoute != VIA_COURSE) {
            destroyView.setRefreshing(false);
            destroyView.setEnabled(false);
        }
    }

    private void setPlaceImages(String[] images) {
        imageViewPager = (ViewPager) rootView.findViewById(R.id.images);
        sliderDotsPanel = (LinearLayout) rootView.findViewById(R.id.sliderDots);
        placeViewPagerAdapter = new PlaceViewPagerAdapter(getContext(), images);

        imageViewPager.setAdapter(placeViewPagerAdapter);
    }

    private void setImageIndicators() {
        dotCount = placeViewPagerAdapter.getCount();
        dots = new ImageView[dotCount];

        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(rootView.getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);

            sliderDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_active_dot));

        imageViewPager.addOnPageChangeListener(onImageChangeListener);
    }

    private void setButtons() {
        if (index == 0) return;

        FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.buttons);
        ImageButton previousImage = (ImageButton) rootView.findViewById(R.id.previous_page_image_button);
        Button previous = (Button) rootView.findViewById(R.id.previous_page_text_button);
        ImageButton nextImage = (ImageButton) rootView.findViewById(R.id.next_page_image_button);
        Button next = (Button) rootView.findViewById(R.id.next_page_text_button);

        if (enterRoute == VIA_COURSE)
            layout.setVisibility(View.VISIBLE);

        if (index == 1) {
            previousImage.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
        }

        if (course != null) {
            if (index == course.getPlaceCount()) {
                nextImage.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);
            }
        }

        previousImage.setOnClickListener(onPreviousImageClickLIstener);
        previous.setOnClickListener(onPreviousClickListener);
        nextImage.setOnClickListener(onNextImageClickListener);
        next.setOnClickListener(onNextClickListener);
    }

    private void setElements() {
        TextView title = rootView.findViewById(R.id.title);
        TextView location = rootView.findViewById(R.id.address);
        TextView phone = rootView.findViewById(R.id.phone);
        TextView detail = rootView.findViewById(R.id.detail);
        ListView information = rootView.findViewById(R.id.information);

        if (enterRoute != DURING_EDITTING_COURSE) {
            FrameLayout space = rootView.findViewById(R.id.space);
            space.setVisibility(View.GONE);
        }

        String parking = place.getParking();
        if(!parking.equals("없음")) parking = place.getParkFee();

        title.setText(place.getName());
        location.setText(place.getLocation());
        phone.setText(place.getPhone());
        phone.setOnClickListener(onPhoneClickListener);
        detail.setText(place.getDetails());

        ArrayList<DetailInformationVO> detailInformation = new ArrayList<DetailInformationVO>(
                Arrays.asList(new DetailInformationVO[] {
                        new DetailInformationVO("item_time", "운영시간", place.getBusinessHours()),
                        new DetailInformationVO("item_parking", "주차", parking),
                        new DetailInformationVO("item_tip", "팁", place.getTip()),
                        new DetailInformationVO("item_tag", "tag", place.getType()),
                        new DetailInformationVO("item_liked", "이 플레이스를 좋아한 사람들", place.getLikes()+"명")
                })
        );

        DetailInformationAdapter adapter = new DetailInformationAdapter(getContext(), detailInformation);
        information.setAdapter(adapter);
        getTotalHeightOfListView(information);
        information.setDivider(null);
        information.setDividerHeight(0);
    }

    private void setPlaceCountImage() {
        FrameLayout numberIndicator = (FrameLayout) rootView.findViewById(R.id.numberIndicator);

        getLayoutInflater().inflate(R.layout.item_place_index, numberIndicator);
        TextView indexInImage = (TextView) numberIndicator.findViewById(R.id.index);
        indexInImage.setText(Integer.toString(index));

        if (enterRoute == VIA_COURSE)
            numberIndicator.setVisibility(View.VISIBLE);
    }

    private void setMap() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.map, new MapFragment(place.getCoordinate_x(), place.getCoordinate_y(), place.getName()));
        transaction.commit();
    }

    private void changeToPlaceFragment(CourseVO course, int index, int enterRoute) {
        final MainActivity activity = (MainActivity) getActivity();
        activity.changeToPlaceFragment(course, index, enterRoute);
    }

    private void makeCall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("전화를 거시겠습니까?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void getTotalHeightOfListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);

            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private PullToRefreshView.OnRefreshListener onPullToDestroyListener = new PullToRefreshView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            destroyView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backToCourseFragment();
                }
            }, 1000);
        }
    };

    private void backToCourseFragment() {
        MainActivity activity = (MainActivity) getActivity();
        int backStackEntryCount = activity.getSupportFragmentManager().getBackStackEntryCount();
        int i = backStackEntryCount - 1;

        while (i > 0 && activity.getSupportFragmentManager().getBackStackEntryAt(i).getName() == "PLACE_FRAGMENT") {
            Log.e("back/PlaceChildFragment", String.valueOf(i));
            activity.getSupportFragmentManager().popBackStack();
            i--;
        }
        activity.getSupportFragmentManager().popBackStack();
        activity.changeToCourseFragment(course, CourseFragment.VIA_NORMAL, index);
    }

    private ViewPager.OnPageChangeListener onImageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotCount; i++)
                dots[i].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_non_active_dot));

            dots[position].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_active_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener onPreviousImageClickLIstener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToPlaceFragment(course, index - 1, VIA_COURSE);
        }
    };

    private View.OnClickListener onPreviousClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToPlaceFragment(course, index - 1, VIA_COURSE);
        }
    };

    private View.OnClickListener onNextImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToPlaceFragment(course, index + 1, VIA_COURSE);
        }
    };

    private View.OnClickListener onNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToPlaceFragment(course, index + 1, VIA_COURSE);
        }
    };

    private View.OnClickListener onPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            makeCall();
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int userAnswer) {
            if (userAnswer == DialogInterface.BUTTON_NEGATIVE) return;

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, 0);
            }

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + place.getPhone()));
                getContext().startActivity(intent);
            }
        }
    };
}

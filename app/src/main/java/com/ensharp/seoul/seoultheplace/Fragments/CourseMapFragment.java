package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.Course.Map.NMapCalloutCustomOldOverlay;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapFragment;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapPOIflagType;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapViewerResourceProvider;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

import java.util.ArrayList;
import java.util.List;
import com.ensharp.seoul.seoultheplace.R;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

@SuppressLint("ValidFragment")
public class CourseMapFragment extends NMapFragment{

    private NMapContext mapContext;
    private NMapView mapView;
    private NMapController mapController;
    private NMapViewerResourceProvider mapViewerResourceProvider;
    private NMapPOIdataOverlay poIdataOverlay;
    private NMapOverlayManager overlayManager;
    private List<String> longitudes;
    private List<String> latitudes;

    @SuppressLint("ValidFragment")
    public CourseMapFragment(List<String> longitudes, List<String> latitudes) {
        this.longitudes = longitudes;
        this.latitudes = latitudes;
    }

    public void changeMapCenter(int position) {
        if (position == 0) {
            poIdataOverlay.showAllPOIdata(7);
            adjustLatitude();
            return;
        }

        NMapPOIitem item = poIdataOverlay.getPOIitemAtIndex(position - 1);

        NGeoPoint point = item.getPoint();
        poIdataOverlay.selectPOIitem(position - 1, false);
        NGeoPoint pointToCenter = new NGeoPoint();
        pointToCenter.latitude = point.latitude;
        pointToCenter.longitude = point.longitude;
        pointToCenter.latitude -= 0.005;
        mapController.setMapCenter(pointToCenter, 10);

    }

    public void adjustLatitude() {
        NGeoPoint center = mapController.getMapCenter();

        switch (mapController.getZoomLevel()) {
            case 6:
                center.latitude -= 0.1;
                break;
            case 7:
                center.latitude -= 0.047;
                break;
            case 8:
                center.latitude -= 0.01;
                break;
            default:
                break;
        }

        mapController.setMapCenter(center, mapController.getZoomLevel());
        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());
        mapContext.onCreate();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (NMapView) getView().findViewById(R.id.mapView);
        mapView.setClientId(getString(R.string.naver_client_id));
    }

    @Override
    public void onStart() {
        super.onStart();

        mapContext.setupMapView(mapView);

        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false, null);
        mapView.setScalingFactor(2.5f, false);
        mapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        NGeoPoint currentPoint = new NGeoPoint();

        mapController = mapView.getMapController();
        mapController.setMapCenter(currentPoint, 1);

        mapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        overlayManager = new NMapOverlayManager(getActivity(), mapView, mapViewerResourceProvider);

        int markedID = NMapPOIflagType.PIN1;
        NMapPOIdata poIData = new NMapPOIdata(longitudes.size(), mapViewerResourceProvider);
        poIData.beginPOIdata(longitudes.size());

        for (int i = 0; i < longitudes.size(); i++) {
            poIData.addPOIitem(Double.parseDouble(longitudes.get(i)), Double.parseDouble(latitudes.get(i)), "", markedID + i, i);
        }

        poIData.endPOIdata();

        poIdataOverlay = overlayManager.createPOIdataOverlay(poIData, null);
        poIdataOverlay.showAllPOIdata(7);
        poIdataOverlay.setOnStateChangeListener(onPOIDataStateChangeListener);

        adjustLatitude();

        mapContext.onStart();

        CourseFragment fragment = (CourseFragment) getParentFragment();
        if (fragment.getIndex() != 0) changeMapCenter(fragment.getIndex());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapContext.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapContext.onStop();
    }

    @Override
    public void onDestroy() {
        mapContext.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {
        @Override
        public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

        }

        @Override
        public void onLongPressCanceled(NMapView nMapView) {

        }

        @Override
        public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

        }

        @Override
        public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

        }

        @Override
        public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

        }

        @Override
        public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIDataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem== null) return;

            int position = nMapPOIitem.getId() + 1;
            CourseFragment fragment = (CourseFragment) getParentFragment();
            fragment.setPageritem(position);

            NGeoPoint point = nMapPOIitem.getPoint();
            NGeoPoint newPoint = new NGeoPoint(point.longitude, point.latitude);
            newPoint.latitude -= 0.0025;

            mapController.setMapCenter(newPoint, 11);
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };
}

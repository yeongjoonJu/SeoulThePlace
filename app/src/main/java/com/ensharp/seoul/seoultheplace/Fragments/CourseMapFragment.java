package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.Course.Map.NMapCalloutCustomOldOverlay;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapFragment;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapPOIflagType;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapViewerResourceProvider;
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
    private NMapOverlayManager overlayManager;
    private List<String> longitudes;
    private List<String> latitudes;

    @SuppressLint("ValidFragment")
    public CourseMapFragment(List<String> longitudes, List<String> latitudes) {
        this.longitudes = longitudes;
        this.latitudes = latitudes;
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
        mapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        NGeoPoint currentPoint = new NGeoPoint();

        mapController = mapView.getMapController();
        mapController.setMapCenter(currentPoint, 1);

        mapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        overlayManager = new NMapOverlayManager(getActivity(), mapView, mapViewerResourceProvider);
        overlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        int markedID = NMapPOIflagType.PIN;
        NMapPOIdata poIData = new NMapPOIdata(2, mapViewerResourceProvider);
        poIData.beginPOIdata(2);

        for (int i = 0; i < longitudes.size(); i++) {
            poIData.addPOIitem(Double.parseDouble(longitudes.get(i)), Double.parseDouble(latitudes.get(i)), "", markedID, 0);
        }

        poIData.endPOIdata();

        NMapPOIdataOverlay poIdataOverlay = overlayManager.createPOIdataOverlay(poIData, null);
        poIdataOverlay.showAllPOIdata(0);
        poIdataOverlay.setOnStateChangeListener(onPOIDataStateChangeListener);

        NGeoPoint center = mapController.getMapCenter();
        center.latitude = center.getLatitude() - 0.1;
        mapController.setMapCenter(center);

        mapContext.onStart();
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

    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {
        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            Log.e("abcd", "코스마커1");

            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poIdataOverlay = (NMapPOIdataOverlay)itemOverlay;

                // check if it is selected by touch event
                if (!poIdataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItemes = 1;

                    NMapPOIdata poiData = poIdataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poIitem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poIitem == overlayItem) {
                            continue;
                        }

                        if (Rect.intersects(poIitem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItemes++;
                        }
                    }

                    if (countOfOverlappedItemes > 1) {
                        String text = countOfOverlappedItemes + " overlapped items for " + overlayItem.getTitle();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mapViewerResourceProvider);
                }
            }

            // use custom callout overlay
            return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds, mapViewerResourceProvider);
        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIDataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            Log.e("abcd", "코스마커2");
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };
}

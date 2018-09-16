package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.Course.Map.NMapPOIflagType;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapViewerResourceProvider;
import com.ensharp.seoul.seoultheplace.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapFragment extends Fragment {

    private NMapContext mapContext;
    private NMapView mapView;
    private NMapController mapController;
    private NMapViewerResourceProvider mapViewerResourceProvider = null;
    private NMapOverlayManager overlayManager;
    private NMapPOIdataOverlay.OnStateChangeListener onPOIDataStateChangeListener = null;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());
        mapContext.onCreate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView = (NMapView)getView().findViewById(R.id.mapView);
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

        int markedID = NMapPOIflagType.PIN;
        NMapPOIdata poIData = new NMapPOIdata(2, mapViewerResourceProvider);
        poIData.beginPOIdata(2);

        NMapPOIitem item1 = poIData.addPOIitem(126.992064, 37.5884693, "", markedID, 0);
//        NMapPOIitem item2 = poIData.addPOIitem(126.914925, 37.528728, "", markedID, 0);

        poIData.endPOIdata();

        NMapPOIdataOverlay poIdataOverlay = overlayManager.createPOIdataOverlay(poIData, null);
        poIdataOverlay.showAllPOIdata(10);
        poIdataOverlay.setOnStateChangeListener(onPOIDataStateChangeListener);

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

    // 지도 초기화가 완료될 때 호출되는 콜백 인터페이스
    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) // success
            mapController.setMapCenter(new NGeoPoint(126.992064, 37.5884693), 11);
        else // fail
            Log.e("MAP_ERROR", "onMapInitHandler: error=" + errorInfo.toString());
    }
}

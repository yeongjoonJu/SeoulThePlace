package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.Course.Map.NMapCalloutCustomOldOverlay;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapFragment;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapPOIflagType;
import com.ensharp.seoul.seoultheplace.Course.Map.NMapViewerResourceProvider;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

@SuppressLint("ValidFragment")
public class MapFragment extends NMapFragment {

    private NMapContext mapContext;
    private NMapView mapView;
    private NMapController mapController;
    private NMapViewerResourceProvider mapViewerResourceProvider = null;
    private NMapOverlayManager overlayManager;
    private NMapLocationManager mapLocationManager;
    private String current = "내 위치";
    private double x;
    private double y;
    private double currentX = 126.937546;
    private double currentY = 37.5637563;
    private String destination;

    public MapFragment() {
        x = 126.973034;
        y = 37.5825288;
    }

    @SuppressLint("ValidFragment")
    public MapFragment(double x, double y, String destination) {
        this.x = x;
        this.y = y;
        this.destination = destination;
    }

    public MapFragment(String x, String y, String destination) {
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        this.destination = destination;
    }

    public void link() {
        mapLocationManager = new NMapLocationManager(getContext());
        mapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        boolean isMyLocationEnabled = mapLocationManager.enableMyLocation(true);

        if (!isMyLocationEnabled) {
            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
        }

        NGeoPoint currentLocation = mapLocationManager.getMyLocation();

        if (currentLocation != null) {
            currentX = currentLocation.getLongitude();
            currentY = currentLocation.getLatitude();
        } else {
            current = "알 수 없음";
        }

        String newCurrent = current.replace(" ", "%20");
        String newDestination = destination.replace(" ", "%20");
        StringBuilder url = new StringBuilder("http://m.map.naver.com/route.nhn?menu=route&sname=");
        url.append(current).append("&sx=").append(currentX).append("&sy=").append(currentY).append("&ename=").append(newDestination)
                .append("&ex=").append(x).append("&ey=").append(y).append("&pathType=0&showMap=true");

        final MainActivity activity = (MainActivity)getActivity();

        activity.changeToWebFragment(url.toString());
    }

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
        overlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        int markedID = NMapPOIflagType.MIN_PIN;
        NMapPOIdata poIData = new NMapPOIdata(2, mapViewerResourceProvider);
        poIData.beginPOIdata(2);

        NMapPOIitem item1 = poIData.addPOIitem(x, y, "", markedID, 0);

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
            link();
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint myLocation) {
            Log.e("abcd", "위치 받는 중");
            currentX = myLocation.getLongitude();
            currentY = myLocation.getLatitude();

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
            Toast.makeText(getContext(), "위치정보를 이용할 수 없습니다.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
            Toast.makeText(getContext(), "현재위치를 가져올 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    };
}

package com.lty.zgj.driver.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.DepartOverAdapter;
import com.lty.zgj.driver.adapter.DepartOverFullAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.DepartModel;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.lty.zgj.driver.event.StartLocationEvent;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.XRecyclerView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/6/19.
 */

public class DepartOverActivity extends BaseXActivity implements AMapLocationListener, LocationSource {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.al_autoLinearLayout)
    AutoRelativeLayout autoLinearLayout;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.tv_unfoldRv_down)
    ImageView tvUnfoldRvDown;
    @BindView(R.id.tv_unfoldRv_up)
    ImageView tvUnfoldRvUp;


    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private ViewGroup.LayoutParams layoutParams;
    private View mHeader;
    private DepartOverAdapter departOverAdapter;
    private List<HistoricalJourneyDetailModel.StationsBean> list = new ArrayList<>();
    private DepartOverFullAdapter departOverFullAdapter;
    private List<DepartModel.ListBean> departModelList;

    private Polyline mPolyline;
    private ArrayList<Marker> markerList;
    private ArrayList<MarkerOptions> markerOptionlist = new ArrayList<>();
    private List<Double> coords = new ArrayList<Double>();
    private List<Double> recoords= new ArrayList<Double>();
    private List<HistoricalJourneyDetailModel.StationsBean> stations;
    private int size;
    private TextView tvPlanTime;
    private View view_complete;

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private List<HistoricalJourneyDetailModel.RelStationsBean> relStations;

    @Override
    public void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        checkPermissionsLocation();
        tvTitle.setText("发车完成");
        tvBtn.setText("完成");
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        initGaoDeMap();

        initRv();

        layoutParams = autoLinearLayout.getLayoutParams();
        String scheduleId = getIntent().getStringExtra("scheduleId");
        fetchTripDetailData(scheduleId);

    }



    private void fetchTripDetailData(String Id) {
        ObjectLoader.getInstance().getTripDetailData(new ProgressSubscriber<HistoricalJourneyDetailModel>(new SubscriberOnNextListener<HistoricalJourneyDetailModel>() {
            @Override
            public void onNext(HistoricalJourneyDetailModel historicalJourneyDetailModel) {

                if (historicalJourneyDetailModel != null) {

                    stations = historicalJourneyDetailModel.getStations();
                    relStations = historicalJourneyDetailModel.getRelStations();
                    size = stations.size();

                    //把计划到站时间 替换 成实际到站时间
                    for (HistoricalJourneyDetailModel.RelStationsBean relStation : relStations) {
                        for (HistoricalJourneyDetailModel.StationsBean station: stations){
                            if(relStation.getStationId() == station.getStationId()){
                                station.setPlanTime(relStation.getRealTime());
                            }
                        }
                    }
                    setStation();
                    //计划辅助点信息
                    List<HistoricalJourneyDetailModel.AssitsBean> assits = historicalJourneyDetailModel.getAssits();
                    //辅助点信息
                    coords.clear();
                    for (HistoricalJourneyDetailModel.AssitsBean point : assits) {
                        coords.add(point.getLon());
                        coords.add(point.getLat());
                    }
                    addPolylineInPlayGround(coords, R.mipmap.route_re);

                    //实际辅助点信息
                    List<HistoricalJourneyDetailModel.RelAssitsBean> relAssits = historicalJourneyDetailModel.getRelAssits();
                    //辅助点信息
                    recoords.clear();
                    if(relAssits != null && relAssits.size() > 0){
                        for (HistoricalJourneyDetailModel.RelAssitsBean repoint : relAssits) {
                            recoords.add(repoint.getLon());
                            recoords.add(repoint.getLat());
                        }
                        addPolylineInPlayGround(recoords, R.mipmap.route); //实际辅助点信息
                    }

                    //添加终点起点
//                    addStartAndEnd(assits);
                    if(stations != null && stations.size() > 0){
                        addmark(stations);
                    }

                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Throwable", "---" + e.getMessage());
            }
        }, context), Id);
    }


    private void initRv() {
        setLayoutManager(xrecyclerview);
        mHeader = View.inflate(context, R.layout.depart_over_icon, null);
        getAdapter().setIconView(mHeader);
        xrecyclerview.setAdapter(getAdapter());
    }

    private void setStation() {
        if (stations != null && stations.size() > 0) {
            int size = stations.size();

            //把计划到站时间 替换 成实际到站时间
            for (HistoricalJourneyDetailModel.RelStationsBean relStation : relStations) {
                for (HistoricalJourneyDetailModel.StationsBean station: stations){
                    if(relStation.getStationId() == station.getStationId()){
                        station.setPlanTime(relStation.getRealTime());
                    }
                }
            }

            if (size < 4) {
                list.clear();
                list.addAll(stations);
                getAdapter().setData(list);
            } else {
                list.clear();
                list.add(stations.get(0));
                list.add(stations.get(1));
                list.add(stations.get(size - 2));
                list.add(stations.get(size - 1));
                getAdapter().setData(list);
            }
        }
    }

    private DepartOverAdapter getAdapter() {
        if (departOverAdapter == null) {
            departOverAdapter = new DepartOverAdapter(context);

        } else {
            departOverAdapter.notifyDataSetChanged();
        }
        return departOverAdapter;

    }

    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }


    /**
     * 检查定位权限
     */
    private void checkPermissionsLocation() {
        new RxPermissions(context).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            // All requested permissions are granted
                        } else {
                            // At least one permission is denied
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_depart_over;
    }


    public static void launch(Activity activity, String scheduleId, DepartModel mdepartModel) {
        Router.newIntent(activity)
                .to(DepartOverActivity.class)
                .putString("scheduleId", scheduleId)
                .putSerializable("mdepartModel", mdepartModel)
                .launch();
    }

    /**
     * 初始化
     */
    private void initGaoDeMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setTiltGesturesEnabled(false);//设置地图是否可以倾斜
        aMap.getUiSettings().setRotateGesturesEnabled(false);//设置地图是否可以旋转
        aMap.getUiSettings().setZoomControlsEnabled(false);//隐藏进度尺
        aMap.getUiSettings().setMyLocationButtonEnabled(false); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //蓝点初始化
        setupLocationStyle();

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
            }
        });
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {

        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //LOCATION_TYPE_LOCATION_ROTATE
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //显示小蓝点
        myLocationStyle.showMyLocation(true);
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_coordinate_point));
        // 自定义精度范围的圆形边框颜色
//        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);

//        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // 设置圆形的填充颜色
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);
            //是指定位间隔
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    if (isFirstLoc) {
                        //将地图移动到定位点
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                        //添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                        //获取定位信息
                        isFirstLoc = false;
                    }
                    StringBuffer buffer = new StringBuffer();

//                    sendText(buffer.toString());//调用 WebSocket 发送数据

//                    Log.e(THIS_FILE, "----latitude------"+ latitude +"..longitude......."+ longitude);
//                    Log.e(THIS_FILE, "------"+ Utils.doubleToString(latitude) +"....."+ Utils.doubleToString(longitude));
//                    Log.e(THIS_FILE, "buffer.toString------"+buffer.toString());
//                    Log.e(THIS_FILE, "city------"+ city);

                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("定位AmapErr", errText);
                }
            }
        }
    }


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up,
            R.id.al_btn_complete

    })

    public void unfoldRvOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_unfoldRv_up:

                //设置展开高度
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                autoLinearLayout.setLayoutParams(layoutParams);

                xrecyclerview.setAdapter(getFullAdapter());
                list.clear();

                if (stations != null && stations.size() > 0) {
                    list.addAll(stations);
                    getFullAdapter().setData(list);
                }


                tvUnfoldRvDown.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                getUiDelegate().gone(true, mapView);
                break;

            case R.id.tv_unfoldRv_down:
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                autoLinearLayout.setLayoutParams(layoutParams);
                xrecyclerview.setAdapter(getAdapter());
                setStation();

                tvUnfoldRvDown.setVisibility(View.GONE);
                tvUnfoldRvUp.setVisibility(View.VISIBLE);
                getUiDelegate().visible(true, mapView);
                break;

            case R.id.al_btn_complete:
                EventBus.getDefault().post(new StartLocationEvent());
                finish();
                break;

        }
    }


    public DepartOverFullAdapter getFullAdapter() {
        if (departOverFullAdapter == null) {
            departOverFullAdapter = new DepartOverFullAdapter(context);


        } else {
            departOverFullAdapter.notifyDataSetChanged();
        }
        return departOverFullAdapter;
    }


    private void addmark(List<HistoricalJourneyDetailModel.StationsBean> stations) {

//        第一步添加marker到地图上：
        for (int i = 0; i < stations.size(); i++) {//此处dataList是存有想要添加marker点的集合
            MarkerOptions markerOptions = new MarkerOptions();//初始化 MarkerOptions对象

//            if (i != 0 && i != dataList.size() - 1) {
//                markerOptions.position(new LatLng(dataList.get(i).getLat(), dataList.get(i).getLon()));
//            }

            if(i == 0){
                markerOptions.position(new LatLng(stations.get(0).getLat(), stations.get(0).getLon()));
                markerOptions.anchor(0.5f,0.6f);
                view_complete = View.inflate(context, R.layout.customer_start_marker, null);
                setMarker(stations, 0, markerOptions);
            }else if(i == stations.size() - 1){
                markerOptions.position(new LatLng(stations.get(stations.size() - 1).getLat(), stations.get(stations.size() - 1).getLon()));
                markerOptions.anchor(0.5f,0.6f);
                view_complete = View.inflate(context, R.layout.customer_end_marker, null);
                setMarker(stations, stations.size() - 1, markerOptions);
            }else {
                markerOptions.position(new LatLng(stations.get(i).getLat(), stations.get(i).getLon()));
                markerOptions.anchor(0.5f,0.2f);
                view_complete = View.inflate(context, R.layout.customer_complete_marker, null);
                setMarker(stations, i, markerOptions);
            }

            markerOptionlist.add(markerOptions);
        }
        //第二个参数是设置是否移动到屏幕中心，FALSE是不移动
        //添加到地图上，返回所有marker的集合
        //添加到地图上，返回所有marker的集合
        markerList = aMap.addMarkers(markerOptionlist, false);

        // 第二步设置marker监听
        //设置market 点击事件// 定义 Marker 点击事件监听
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
// 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                //markerList即为添加到地图上返回的marker集合
                for (int i = 0; i < markerList.size(); i++) {
                    if (marker.equals(markerList.get(i))) {
                        //已找到你所点击的marker，接下来进行你想要的操作
                        return true;
                    }
                }
                return false;
            }


        });


    }

    private void setMarker(List<HistoricalJourneyDetailModel.StationsBean> stations, int i, MarkerOptions markerOptions) {
        tvPlanTime = view_complete.findViewById(R.id.tv_planTime);
        if(relStations != null && relStations.size() >0){
            for (HistoricalJourneyDetailModel.RelStationsBean  relStation :relStations){
                int stationId = stations.get(i).getStationId();
                int stationIRe = relStation.getStationId();
                if(stationId == stationIRe){
                    String planTime = relStation.getRealTime();
                    String stationTime = TimeUtils.formatStationTime(planTime);
                    tvPlanTime.setText(stationTime);
                }

            }
        }
        Bitmap bitmap_complete = convertViewToBitmap(view_complete);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap_complete));//设置marker图标
    }

    /**
     * 设置起点终点站
     *
     * @param assits
     */
    private void addStartAndEnd(List<HistoricalJourneyDetailModel.AssitsBean> assits) {

        View view_start = View.inflate(context, R.layout.customer_start_marker, null);
        Bitmap bitmap_start = convertViewToBitmap(view_start);
        MarkerOptions startMarker = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_start));
        HistoricalJourneyDetailModel.AssitsBean startStation = assits.get(0);
        LatLng startLatlng = new LatLng(startStation.getLat(), startStation.getLon());
        startMarker.position(startLatlng);
        aMap.addMarker(startMarker);


        View view_end = View.inflate(context, R.layout.customer_end_marker, null);
        Bitmap bitmap_end = convertViewToBitmap(view_end);
        MarkerOptions endMarker = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(bitmap_end));
        HistoricalJourneyDetailModel.AssitsBean endStation = assits.get(assits.size() - 1);
        LatLng endLatlng = new LatLng(endStation.getLat(), endStation.getLon());
        endMarker.position(endLatlng);
        aMap.addMarker(endMarker);
    }

    //view 转bitmap

    public static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }


    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround(List<Double> coordPoints, int resourceId) {
        List<LatLng> list = readLatLngs(coordPoints);
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(resourceId));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(resourceId)) //setCustomTextureList
                // (bitmapDescriptors)
                .setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(20));

        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
        for (int i = 0; i < list.size(); i++) {//trajectorylist为轨迹集合
            newbounds.include(list.get(i));
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 200));//第二个参数为四周留空宽度

    }


    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs(List<Double> coordPoints) {
        List<LatLng> points = new ArrayList<LatLng>();

        for (int i = 0; i < coordPoints.size(); i += 2) {
            points.add(new LatLng(coordPoints.get(i + 1), coordPoints.get(i)));
        }
        return points;
    }

}

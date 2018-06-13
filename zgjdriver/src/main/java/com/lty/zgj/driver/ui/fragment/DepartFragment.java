package com.lty.zgj.driver.ui.fragment;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketFragment;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.adapter.DepartAdapter;
import com.lty.zgj.driver.bean.WebSocketResponse;
import com.lty.zgj.driver.core.tool.ScreenUtils;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xrecyclerview.XRecyclerView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/6/6.
 * 发车
 */

public class DepartFragment extends AbsBaseWebSocketFragment implements LocationSource,
        AMapLocationListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.tv_unfoldRv_down)
    TextView tvUnfoldRvDown;
    @BindView(R.id.tv_unfoldRv_up)
    TextView tvUnfoldRvUp;
    @BindView(R.id.ar)
    AutoRelativeLayout auAr;

    List<String> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    List<Double> pointsRepairShop = new ArrayList<Double>();
    List<Double> coords = new ArrayList<Double>();private float mapZoom;
    private LatLng mapTarget;
    private Polyline mPolyline;




    private AMap aMap;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;


    //自定义定位小蓝点的Marker
    Marker locationMarker;

    //坐标和经纬度转换工具
    Projection projection;
    private MyLocationStyle myLocationStyle;
    private DepartAdapter departAdapter;
    private ViewGroup.LayoutParams layoutParams;
    private String THIS_FILE = "DepartFragment";


    public void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        SharedPref.getInstance(context).putInt("position", list.size());
        checkPermissionsLocation();
        init();
        initRv();
        getAdapter().setData(list);
        layoutParams = auAr.getLayoutParams();
        addPolylineInPlayGround(coords);
//        startMove();
    }

    private void initRv() {
        setLayoutManager(xrecyclerview);
        xrecyclerview.setAdapter(getAdapter());
    }

    public DepartAdapter getAdapter() {
        if (departAdapter == null) {
            departAdapter = new DepartAdapter(context);

        } else {
            departAdapter.notifyDataSetChanged();
        }
        return departAdapter;

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


    /**
     * 初始化
     */
    private void init() {
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

        //蓝点初始化
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        setupLocationStyle();
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        myLocationStyle.showMyLocation(true);


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
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
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

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        if (response != null) {
            //我们需要通过 path 判断是不是登陆接口返回的数据，因为也有可能是其他接口返回的
            closeRoundProgressDialog();//关闭加载对话框
            showToastMessage("登陆成功");
        }
    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {
        showToastMessage(String.format("登陆失败：%s", response));
    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
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
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {

                    if (isFirstLoc) {
                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(amapLocation);
                        //添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                        //获取定位信息
                        isFirstLoc = false;
                    }
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    sendText(buffer.toString());//调用 WebSocket 发送数据
                    sendData();

                    Log.e(THIS_FILE, "------"+amapLocation.getLatitude()+"....."+amapLocation.getLongitude());

                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("定位AmapErr", errText);
                }
            }
        }
    }

    private void sendData() {
        JSONObject param = new JSONObject();
        WebSocketResponse webSorketResponse = new WebSocketResponse();
        WebSocketResponse.BodyBean body = webSorketResponse.getBody();


    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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

    /**
     * 返回Fragment layout资源ID
     *
     * @return
     */
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.depart_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up
    })

    public void unfoldRvOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_unfoldRv_down:
                //设置展开高度
                layoutParams.height = ScreenUtils.getScreenHeight(context) - 700;
                auAr.setLayoutParams(layoutParams);

                ShowDialogRelative.toastDialog(context, "点击了");
                list.clear();
                list.add("C");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");
                list.add("D");

                getAdapter().setData(list);
                tvUnfoldRvDown.setVisibility(View.GONE);
                tvUnfoldRvUp.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_unfoldRv_up:
                layoutParams.height = 500;
                auAr.setLayoutParams(layoutParams);

                list.clear();
                list.add("A");
                list.add("B");
                list.add("C");
                list.add("D");
                getAdapter().setData(list);
                tvUnfoldRvDown.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 开始移动
     */
    public void startMove() {

        if (mPolyline == null) {
            ShowDialogRelative.toastDialog(context,"请先设置路线");
            return;
        }

        mPolyline.remove();
        // 读取轨迹点
        List<LatLng> points = readLatLngs(coords);
        // 构建 轨迹的显示区域
        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // 实例 SmoothMoveMarker 对象
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(40);

        // 设置  自定义的InfoWindow 适配器
//        aMap.setInfoWindowAdapter(infoWindowAdapter);
        // 显示 infowindow
        smoothMarker.getMarker().showInfoWindow();

        // 设置移动的监听事件  返回 距终点的距离  单位 米
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(final double distance) {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (infoWindowLayout != null && title != null) {

//                            title.setText("距离终点还有： " + (int)distance + "米");
//                            title.setText("前往修理厂…");

                            //救援车辆到达目的地后，距离公交司机的位置
                            if(distance == 0){
//                                float lineDistance = AMapUtils.calculateLineDistance(reliefDriverlatlng, busDriverlatlng);
//                                if(lineDistance <= 100){
//
//
//                                }

                            }

                        }
                    }
                });

            }
        });

        // 开始移动
        smoothMarker.startSmoothMove();

    }



    /**
     * 个性化定制的信息窗口视图的类
     * 如果要定制化渲染这个信息窗口，需要重载getInfoWindow(Marker)方法。
     * 如果只是需要替换信息窗口的内容，则需要重载getInfoContents(Marker)方法。
     */
    AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
        // 个性化Marker的InfoWindow 视图
        // 如果这个方法返回null，则将会使用默认的信息窗口风格，内容将会调用getInfoContents(Marker)方法获取
        @Override
        public View getInfoWindow(Marker marker) {

            return getInfoWindowView(marker);
        }

        // 这个方法只有在getInfoWindow(Marker)返回null 时才会被调用
        // 定制化的view 做这个信息窗口的内容，如果返回null 将以默认内容渲染
        @Override
        public View getInfoContents(Marker marker) {

            return getInfoWindowView(marker);
        }
    };

    View infoWindowLayout;
    TextView title;
    TextView snippet;

    /**
     * 自定义View并且绑定数据方法
     *
     * @param marker 点击的Marker对象
     * @return 返回自定义窗口的视图
     */
    private View getInfoWindowView(Marker marker) {
//        if (infoWindowLayout == null) {
//            infoWindowLayout = LayoutInflater.from(context).inflate(R.layout.custom_rescue_infowindow, null);
//            title = infoWindowLayout.findViewById(R.id.tv_rescue);
//            title.setText("5公里，15分钟");
//
//        }
//
//        return infoWindowLayout;
        return null;
    }


    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround(List<Double> coordPoints) {
        List<LatLng> list = readLatLngs(coordPoints);
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.mipmap.pic_route));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.pic_route)) //setCustomTextureList(bitmapDescriptors)
				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(120));
        LatLngBounds bounds = new LatLngBounds(list.get(0), list.get(list.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs(List<Double> coordPoints) {
        List<LatLng> points = new ArrayList<LatLng>();
        coords.clear();

        coords.add(114.368666);
        coords.add(30.412423);
        coords.add(114.373816);
        coords.add(30.417012);
        coords.add(114.374502);
        coords.add(30.419528);
        coords.add(114.376562);
        coords.add(30.421601);
        coords.add(114.377335);
        coords.add(30.423303);
        coords.add(114.378107);
        coords.add(30.424117);
        coords.add(114.381197);
        coords.add(30.43248);

        for (int i = 0; i < coordPoints.size(); i += 2) {
            points.add(new LatLng(coordPoints.get(i+1), coordPoints.get(i)));
        }
        return points;
    }

}

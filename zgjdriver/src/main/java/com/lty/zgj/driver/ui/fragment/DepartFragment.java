package com.lty.zgj.driver.ui.fragment;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
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
import com.lty.zgj.driver.WebSocket.WebSocketManager;
import com.lty.zgj.driver.WebSocket.WebSocketRequst;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.adapter.DepartAdapter;
import com.lty.zgj.driver.adapter.DepartFullAdapter;
import com.lty.zgj.driver.bean.DepartModel;
import com.lty.zgj.driver.bean.LoginWebWebSocketModel;
import com.lty.zgj.driver.bean.StartBustModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.GsonUtils;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.ui.activity.DepartOverActivity;
import com.lty.zgj.driver.ui.activity.LoginActivity;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

public class DepartFragment extends AbsBaseWebSocketFragment implements LocationSource, AMapLocationListener {

    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.tv_unfoldRv_down)
    ImageView tvUnfoldRvDown;
    @BindView(R.id.tv_unfoldRv_up)
    ImageView tvUnfoldRvUp;
    @BindView(R.id.ar)
    AutoRelativeLayout auAr;
    //    @BindView(R.id.al_autoLinearLayout)
//    AutoLinearLayout autoLinearLayout;
    @BindView(R.id.au_al)
    AutoLinearLayout auAl;
    @BindView(R.id.al_btn)
    AutoLinearLayout alBtn;
    @BindView(R.id.tv_btn)
    TextView tvBtn;
    @BindView(R.id.tv_travel_itinerary_null)
    AutoLinearLayout RavelItineraryNull;
    @BindView(R.id.tv_travel_itinerary)
    AutoRelativeLayout RavelItinerary;
    @BindView(R.id.login_btn_bg)
    AutoLinearLayout btnBg;
    @BindView(R.id.login_btn_bg_no)
    AutoLinearLayout btnBgNoClick;
    @BindView(R.id.tv_btn_no)
    TextView tvBtnNoClick;

    List<Double> pointsRepairShop = new ArrayList<Double>();
    List<Double> coords = new ArrayList<Double>();
    private float mapZoom;
    private LatLng mapTarget;
    private Polyline mPolyline;
    private AMap aMap;


    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private OnLocationChangedListener mListener;

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private String webSocketJson;
    //自定义定位小蓝点的Marker
    Marker locationMarker;


    //坐标和经纬度转换工具
    Projection projection;

    private MyLocationStyle myLocationStyle;
    private DepartAdapter departAdapter;
    private ViewGroup.LayoutParams layoutParams;
    private String THIS_FILE = "DepartFragment";
    private double latitude;
    private double longitude;
    private String city;
    private String street;
    private View mHeader;
    private DepartFullAdapter departFullAdapter;
    private List<DepartModel.ListBean> list = new ArrayList<>();
    private List<DepartModel.ListBean> departModelList;
    private int size;
    private List<DepartModel.PointListBean> pointList;
    private boolean isLoadData;
    private String scheduleId;
    private DepartModel mdepartModel;
    private Handler mycircleHandler;
    private Runnable runnable;


    public void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        tvBtn.setText("发车");
        checkPermissionsLocation();
        initMap();
        initRv();
//        layoutParams = autoLinearLayout.getLayoutParams();
//        addPolylineInPlayGround(coords);

        handlerExecuteTimerTask();
    }


    private void initRv() {
        setLayoutManager(xrecyclerview);
        mHeader = View.inflate(context, R.layout.depart_icon, null);
        getAdapter().setIconView(mHeader);
        xrecyclerview.setAdapter(getAdapter());
        fetchDepartData(23);
        isLoadData = false;
    }

    public DepartAdapter getAdapter() {
        if (departAdapter == null) {
            departAdapter = new DepartAdapter(context);

        } else {
            departAdapter.notifyDataSetChanged();
        }
        return departAdapter;

    }

    public DepartFullAdapter getFullAdapter() {
        if (departFullAdapter == null) {
            departFullAdapter = new DepartFullAdapter(context);

        } else {
            departFullAdapter.notifyDataSetChanged();
        }
        return departFullAdapter;

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
    private void initMap() {
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
        closeRoundProgressDialog();
        //刷新今日行程
        if (isLoadData) {
            fetchDepartData(23);
        }

        isLoadData = true;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        closeRoundProgressDialog();//关闭加载对话框
        CommonResponse.BodyBean body = response.getBody();
        int code = body.getStateCode();
        //token过期 去登录界面
        if (code == 104) {
            LoginActivity.launch(context);
            context.finish();
        } else {
            String data = body.getData();
            SharedPref.getInstance(context).putString(Constant.USER_INFO, data);
            LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
            String token = loginModel.getToken();//更新token
            int driverId = loginModel.getDriverId();
            SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, token);
            SharedPref.getInstance(context).putInt(Constant.WEBSOCKT_CONT, 1);
            SharedPref.getInstance(context).putInt(Constant.DRIVER_ID, driverId); //司机Id
            Log.e(THIS_FILE, "token-----" + token);
            Log.e("token", "token---SharedPref----" + token + "-----" + "传gps断线重连获取token");


//            if(!TextUtils.isEmpty(data)){
//                int driverId = loginModel.getDriverId();
//                gpsUploadData(driverId); //业务消息类型 msgId: 0x201
//            }
        }
    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {
        showToastMessage(String.format("登陆失败：%s", response));
        closeRoundProgressDialog();//关闭加载对话框
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

                    latitude = amapLocation.getLatitude();
                    longitude = amapLocation.getLongitude();
                    if (isFirstLoc) {
                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(amapLocation);
                        //添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                        //获取定位信息
                        isFirstLoc = false;
                    }
                    StringBuffer buffer = new StringBuffer();
                    city = amapLocation.getCity();
                    street = amapLocation.getStreet();

                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + city + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + street + "" + amapLocation.getStreetNum());
//                    sendText(buffer.toString());//调用 WebSocket 发送数据


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
//            mlocationClient.startLocation();
            mlocationClient.startLocation();

        }
    }


    private void handlerExecuteTimerTask() {


        mycircleHandler = new Handler();

        runnable = new Runnable() {
            @Override

            public void run() {
                //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
                Log.e(THIS_FILE, "----latitude------"+ latitude +"..longitude......."+ longitude);
                mycircleHandler.postDelayed(this, 5000);

            }

        };

        mycircleHandler.postDelayed(runnable, 5000);

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
        return R.layout.depart_fragment_1;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initData(savedInstanceState);
    }

    @Override
    protected void initView() {
        webSocketConnectLogin();//webSocket 登录鉴权

//        travelPathUploadData();//业务消息类型 msgId: 0x202
    }


    private void webSocketConnectLogin() {
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x102, token, null);
        sendText(webSocketJson);//登录鉴权
        Log.e("token", "token---SharedPref----" + token + "-----" + "登录鉴权传的token");
    }


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up,
            R.id.al_btn
    })

    public void unfoldRvOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_unfoldRv_down:
                //设置展开高度
//                layoutParams.height = ScreenUtils.getScreenHeight(context) - 700;
//                autoLinearLayout.setLayoutParams(layoutParams);
                xrecyclerview.setAdapter(getFullAdapter());
                list.clear();
                if (departModelList != null && departModelList.size() > 0) {
                    list.addAll(departModelList);
                    getFullAdapter().setData(list);
                }

                tvUnfoldRvDown.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
                tvUnfoldRvUp.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_unfoldRv_up:
//                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                autoLinearLayout.setLayoutParams(layoutParams);
                xrecyclerview.setAdapter(getAdapter());

                setStation();
                tvUnfoldRvDown.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                break;
            case R.id.al_btn:
//                sendGps();
                fetchStartBuslData(scheduleId, mdepartModel);
//                DepartOverActivity.launch(context, scheduleId, mdepartModel);
                break;
        }
    }

    private void fetchStartBuslData(final String scheduleId, final DepartModel mdepartModel) {
        ObjectLoader.getInstance().getStartBuslData(new ProgressSubscriber<StartBustModel>(new SubscriberOnNextListener<StartBustModel>() {
            @Override
            public void onNext(StartBustModel startBustModel) {
                //发车成功 去行程结束界面
                DepartOverActivity.launch(context, scheduleId, mdepartModel);
            }

            @Override
            public void onError(Throwable e) {

            }
        }, context), scheduleId);
    }


    private void sendGps() {
        String data = SharedPref.getInstance(context).getString(Constant.USER_INFO, null);
        if (!TextUtils.isEmpty(data)) {
            LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
            int driverId = loginModel.getDriverId();
            gpsUploadData(driverId); //业务消息类型 msgId: 0x201
        }
    }


    /**
     * 开始移动
     */
    public void startMove() {

        if (mPolyline == null) {
            ShowDialogRelative.toastDialog(context, "请先设置路线");
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

                            //救援车辆到达目的地后，距离公交司机的位置
                            if (distance == 0) {

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

        for (int i = 0; i < coordPoints.size(); i += 2) {
            points.add(new LatLng(coordPoints.get(i + 1), coordPoints.get(i)));
        }
        return points;
    }


    /**
     * Gps信息上传
     */
    private void gpsUploadData(int driverId) {
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        Log.e("token", "token---SharedPref----" + token + "-----" + "传gps需要的token");
        int websockt_cont = SharedPref.getInstance(context).getInt(Constant.WEBSOCKT_CONT, 0);

        long gpsTime = System.currentTimeMillis() / 1000;

        Map<String, Object> gpsUpload = WebSocketRequst.getInstance(context)
                .gpsUpload(22, 33, 114.432468, 30.452708, "662", 1, driverId, gpsTime);

        String socketJsonGps = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x201, token, gpsUpload);

        if (websockt_cont == 1) {
            sendText(socketJsonGps);
        } else {
            //webSocket 断开后重新鉴权
            String token_new = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
            webSocketConnectLogin(token_new);
        }
        Log.e(THIS_FILE, "gpsTime------" + gpsTime);
        Log.e(THIS_FILE, "token---SharedPref----" + token);
        Log.e(THIS_FILE, "socketJsonGps----" + socketJsonGps);

    }

    private void webSocketConnectLogin(String token) {
        webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x102, token, null);
        sendText(webSocketJson);//登录鉴权
        Log.e(THIS_FILE, "token---SharedPref----" + token);
    }


    /**
     * 行程轨迹上传
     */
    private void travelPathUploadData() {
        long stationTime = System.currentTimeMillis() / 1000;
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        Map<String, Object> travelPathUpload = WebSocketRequst.getInstance(context).travelPathUpload(2, 2, 2, "金融港四路", stationTime, stationTime, 2);
        String socketJsonTravelPath = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x202, token, travelPathUpload);
        sendText(socketJsonTravelPath);
        Log.e(THIS_FILE, "stationTime----" + stationTime);
        Log.e(THIS_FILE, "token---SharedPref----" + token);
        Log.e(THIS_FILE, "socketJsonTravelPath====================" + socketJsonTravelPath);
    }

    private void fetchDepartData(int id) {
        ObjectLoader.getInstance().getDepartModelData(new ProgressSubscriber<DepartModel>(new SubscriberOnNextListener<DepartModel>() {

            @Override
            public void onNext(DepartModel departModel) {
                if (departModel != null) {
                    RavelItinerary.setVisibility(View.VISIBLE);
                    RavelItineraryNull.setVisibility(View.GONE);
                    mdepartModel = departModel;

                    scheduleId = departModel.getId();
                    departModelList = departModel.getList();
                    pointList = departModel.getPointList();

                    size = departModelList.size();
                    setStation();
                    coords.clear();

                    for (DepartModel.PointListBean point : pointList) {
                        coords.add(point.getLongitude());
                        coords.add(point.getLatitude());
                    }
                    addPolylineInPlayGround(coords);


                    isDepart(departModel);


                } else {
                    RavelItinerary.setVisibility(View.GONE);
                    RavelItineraryNull.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable e) {

            }
        }, context), id);
    }


    /**
     * 是否可以发车
     * @param departModel
     */
    private void isDepart(DepartModel departModel) {
        String startTime = departModel.getStartTime();
        String dateTime = departModel.getDate();
        String stationTime = TimeUtils.formatStationTime(startTime);
        String stringDate = TimeUtils.getStringDateTime(dateTime);
        String stationDate = stringDate + " " + stationTime;//到站时间

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);

        String timeDifferenceHour = TimeUtils.getTimeDifferenceHour(dateString, stationDate);

        Log.e(THIS_FILE, "hours+++++++++" + timeDifferenceHour);

        if(Double.parseDouble(timeDifferenceHour) > 1){

            //早于到站时间 1小时 不能发车
            tvBtnNoClick.setText("未到发车时间");
            btnBgNoClick.setVisibility(View.VISIBLE);
            btnBg.setVisibility(View.GONE);
            alBtn.setEnabled(false);
        }else {

            //晚于到站时间可以发车
            tvBtn.setText("发车");
            btnBgNoClick.setVisibility(View.GONE);
            btnBg.setVisibility(View.VISIBLE);
            alBtn.setEnabled(true);

        }
    }

    private void setStation() {
        if (departModelList != null && departModelList.size() > 0) {
            if (size < 4) {

                list.clear();
                list.addAll(departModelList);
                getAdapter().setData(list);
            } else {
                list.clear();
                list.add(departModelList.get(0));
                list.add(departModelList.get(1));
                list.add(departModelList.get(size - 2));
                list.add(departModelList.get(size - 1));
                getAdapter().setData(list);
            }
        }
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentFirstVisible();
        if (isLoadData) {
            fetchDepartData(23);
        }

        isLoadData = true;
    }
}

package com.lty.zgj.driver.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
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
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
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
import com.lty.zgj.driver.bean.EndBusModel;
import com.lty.zgj.driver.bean.LoginWebWebSocketModel;
import com.lty.zgj.driver.bean.StartBustModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.GsonUtils;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.lty.zgj.driver.event.ReceiveDateEvent;
import com.lty.zgj.driver.event.ReceiveDateItemEvent;
import com.lty.zgj.driver.event.StartLocationEvent;
import com.lty.zgj.driver.event.StopExecuteTimerTaskEvent;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.ui.activity.DepartOverActivity;
import com.lty.zgj.driver.ui.activity.LoginActivity;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.CustomDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
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
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/6/6.
 * 发车
 */

public class DepartFragment extends AbsBaseWebSocketFragment implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {

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
    @BindView(R.id.tv_car_pai)
    TextView tvCarPai;
    @BindView(R.id.tv_station_start)
    TextView tvStationstart;
    @BindView(R.id.tv_station_end)
    TextView tvStationEnd;
    @BindView(R.id.tv_depart_time)
    TextView tvDepartTime;
    @BindView(R.id.au_ar_loading)
    AutoLinearLayout auArLoading;
    @BindView(R.id.au_ar_bounds)
    AutoRelativeLayout auArBounds;

    List<Double> pointsRepairShop = new ArrayList<Double>();
    List<Double> coords = new ArrayList<Double>();
    List<Double> coordsStation = new ArrayList<Double>();
    List<Double> coordsWalkRoute = new ArrayList<Double>();
    private float mapZoom;
    private LatLng mapTarget;
    private Polyline mPolyline;
    private Polyline mPolylineWalk;
    private AMap aMap;
    private CustomDialog mDialog;
    private TextView tvPlanTime;
    private View view_complete;


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
    //    private static Handler mycircleHandler;
//    private static Runnable runnable;
    private int driverId;


    private ArrayList<MarkerOptions> markerOptionlist = new ArrayList<>();
    private ArrayList<Marker> markerList;
    private List<LatLng> points;
    private int routeId;
    private int busId;
    private String dateTime;
    private String cityCode;
    private int status;

    private int DEPAR_BTN = 1001;
    private int END_BTN = 1002;
    private int CLICK_STATUS = DEPAR_BTN;
    private boolean isLoadDataOnResume;
    private RouteSearch mRouteSearch;
    private final int ROUTE_TYPE_WALK = 3;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private String endName;
    private Marker marker_time;
    private String startName;
    private boolean isUpdateGps = false; //将地图移动到定位点
    private boolean isPolylineWalk = true;
    private boolean isSendGps = false;

    private float distance;
    private float distance_next;

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initData(savedInstanceState);
    }


    public void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        checkPermissionsLocation();
        initRv();
        initGaodeMap();
//        layoutParams = autoLinearLayout.getLayoutParams();
//        addPolylineInPlayGround(coords);
    }

    @Override
    protected void initView() {
        webSocketConnectLogin();//webSocket 登录鉴权
    }


    private void webSocketConnectLogin() {
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        if (!TextUtils.isEmpty(token)) {
            Log.e("token", "token---SharedPref----" + token + "-----" + "登录鉴权传的token");
            webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x102, token, null);
            sendText(webSocketJson);//登录鉴权
        }
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        closeRoundProgressDialog();//关闭加载对话框
        CommonResponse.BodyBean body = response.getBody();
        int code = body.getStateCode();
        int msgId = response.getHeaderPacket().getMsgId();
        Log.e(THIS_FILE, "msgId----" + msgId);
        //token过期 去登录界面
        if (code == 104) {
            LoginActivity.launch(context);
            context.finish();
        } else {
            String data = body.getData();
            if (data != null) {
                SharedPref.getInstance(context).putString(Constant.USER_INFO, data);
                LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
                String token = loginModel.getToken();//更新token
                driverId = loginModel.getDriverId();
                cityCode = loginModel.getCityCode();

                SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, token);
                SharedPref.getInstance(context).putInt(Constant.WEBSOCKT_CONT, 1);
                SharedPref.getInstance(context).putInt(Constant.DRIVER_ID, driverId); //司机Id
                SharedPref.getInstance(context).putString(Constant.cityCode, cityCode); //司机Id
                Log.e(THIS_FILE, "token-----" + token);
                Log.e("token", "token---SharedPref----" + token + "-----" + "传gps断线重连获取token");
                Log.e(THIS_FILE, "driverId----" + driverId);

                fetchDepartData(driverId);//获取今日发车数据
            }

        }
    }


    private void initRv() {
        setLayoutManager(xrecyclerview);
        mHeader = View.inflate(context, R.layout.depart_icon, null);
        getAdapter().setIconView(mHeader);
        xrecyclerview.setAdapter(getAdapter());
        isLoadData = false;
        isLoadDataOnResume = false;
    }

    public DepartAdapter getAdapter() {
        if (departAdapter == null) {
            departAdapter = new DepartAdapter(context);
            departAdapter.setRecItemClick(departItemClick);
        } else {
            departAdapter.notifyDataSetChanged();
        }
        return departAdapter;

    }

    public DepartFullAdapter getFullAdapter() {
        if (departFullAdapter == null) {
            departFullAdapter = new DepartFullAdapter(context);
            departFullAdapter.setRecItemClick(RecItemFull);
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
    private void initGaodeMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            mRouteSearch = new RouteSearch(context);
            mRouteSearch.setRouteSearchListener(this);
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
        aMap.getUiSettings().setTiltGesturesEnabled(false);//设置地图是否可以倾斜
        aMap.getUiSettings().setRotateGesturesEnabled(true);//设置地图是否可以旋转
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //蓝点初始化
        setupLocationStyle();
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Log.e(THIS_FILE, "小蓝点定位经纬度-------" + "latitude----" + latitude
                        + "........." + "longitude--" + longitude);
            }
        });
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
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
        closeRoundProgressDialog();
        //刷新今日行程
        if (isLoadDataOnResume) {
            driverId = SharedPref.getInstance(context).getInt(Constant.DRIVER_ID, 0);
            fetchDepartData(driverId);
        }

        isLoadDataOnResume = true;
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
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    if (isFirstLoc) {
                        //将地图移动到定位点
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                        //添加图钉
//                      aMap.addMarker(getMarkerOptions(amapLocation));
                        if (isPolylineWalk) {
                            // 步行终点坐标
                            // 步行起点坐标
                            mStartPoint = new LatLonPoint(latitude, longitude);
                            searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);//定位成功 规划路径导航
                        }

                        isFirstLoc = false;
                    }

                    Log.e(THIS_FILE, "----latitude------" + latitude + "..longitude......." + longitude);

                    if (isUpdateGps) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                    }

                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("定位AmapErr", errText);
                }

            }
        }
    }


    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LatLng latLng = (LatLng) msg.obj;
            double lat = latLng.latitude;
            double lon = latLng.longitude;

            sendGps(latitude, longitude); //发送gps

            DepartModel.ListBean stationModel = getStationModel(latLng);
            //当前位置 到下一站的距离
            if (distance <= 50 || distance > distance_next) {
                Log.e(THIS_FILE, "distance-----" + distance);
                Log.e(THIS_FILE, "distance_next-----" + distance_next);
                int tripId = stationModel.getId();//行程id
                int stationId = stationModel.getStationId();

                String stationName = stationModel.getStationName();
//                tvBtn.setText("到达" + stationName);
                if (stationModel.getStationName().equals(endName)) {
                    tvBtn.setText("结束");
                    //到达最后一站 结束上传gps
                    mlocationClient.stopLocation();
                    isSendGps = false;
                    isUpdateGps = false;
                    mycircleHandler.removeCallbacks(runnable);
                    Log.e(THIS_FILE, "停止上传gps");
                }
                long stationTime = System.currentTimeMillis() / 1000;
                travelPathUploadData(tripId, routeId, stationId, stationName, stationTime, busId, scheduleId);
                stationModel.setUploadPoint(true);

            }
        }
    };

    /**
     * 计算当前定位点和目标点的距离，并显示，单位为米
     *
     * */
    private DepartModel.ListBean getStationModel(LatLng latLng) {

        if (departModelList != null && departModelList.size() > 0) {

            for (int i = 0; i < departModelList.size(); i++) {

                DepartModel.ListBean model = departModelList.get(i);

                //判断站点是否上传 没上传 就上传
                if (!model.isUploadPoint()) {
                    LatLng latLng2 = getLatLng(i);
                    distance = AMapUtils.calculateLineDistance(latLng, latLng2);
                    //当前位置 到下下一站的距离
                    if (i + 1 < departModelList.size()) {
                        LatLng latLng3 = getLatLng(i + 1);
                        distance_next = AMapUtils.calculateLineDistance(latLng, latLng3);
                    } else {
                        distance_next = 100000000;
                    }
                    Log.e(THIS_FILE, "获取当前站点的model");
                    return model;
                }

            }
        }

        return null;

    }


    @NonNull
    private LatLng getLatLng(int i) {
        double lon1 = departModelList.get(i).getLon();
        double lat1 = departModelList.get(i).getLat();
        return new LatLng(lat1, lon1);
    }


    /**
     * 定时器 5秒执行一次
     */
    Handler mycircleHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override

        public void run() {
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            if(isSendGps){
                mlocationClient.startLocation();
                Message message = handler.obtainMessage();
                LatLng latLng = new LatLng(latitude, longitude);
                message.obj = latLng;
                handler.sendMessage(message);
                mycircleHandler.postDelayed(this, 5000);
            }
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stopExecuteTimerTask(StopExecuteTimerTaskEvent event) {
        //退出登陆后关闭定时器 以及停止定位
        mlocationClient.stopLocation();
        mycircleHandler.removeCallbacks(runnable);
    }

    /**
     * 行程轨迹上传
     */
    private void travelPathUploadData(int scheduleTripId, int routeId, int stationId, String stationName, long stationTime, int busId, String
            scheduleId) {

        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        Map<String, Object> travelPathUpload = WebSocketRequst.getInstance(context).travelPathUpload(scheduleTripId, routeId, stationId,
                stationName, stationTime, busId, scheduleId);
        String socketJsonTravelPath = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x202, token, travelPathUpload);
        sendText(socketJsonTravelPath);
        Log.e(THIS_FILE, "上传站点时间----" + stationTime);
        Log.e(THIS_FILE, "token---SharedPref----" + token);
        Log.e(THIS_FILE, "socketJsonTravelPath====================" + socketJsonTravelPath);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        startLocationClient();
    }

    private void startLocationClient() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
        }

        //初始化定位参数
        initLocationOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }

    private void initLocationOption() {

        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        //定位精度:高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位缓存策略
        mLocationOption.setLocationCacheEnable(false);
        //gps定位优先
        mLocationOption.setGpsFirst(false);
        //设置定位间隔
        mLocationOption.setInterval(3000);
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        stopLocationClient();
    }

    private void stopLocationClient() {
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


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up,
            R.id.al_btn,
            R.id.au_ar_bounds
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
                auArBounds.setVisibility(View.GONE);
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
                auArBounds.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                break;
            case R.id.al_btn:

                if (CLICK_STATUS == DEPAR_BTN) {
                    fetchStartBuslData(scheduleId, mdepartModel);     //发车
                } else {
                    depart();
                }
                break;

            case R.id.au_ar_bounds:
                //全屏显示站点
                setLatLngBounds();
                break;
        }
    }

    private void depart() {
        mDialog = new CustomDialog(context, R.layout.custom_dialog_login_out_layout, "提示", "你是否确定结束发车?",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        fetchEndBusData(scheduleId);                     //发车中
                    }
                }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        }, "确认", "取消");
        mDialog.setCanotBackPress();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void setLatLngBounds() {
        List<LatLng> points = readLatLngs(coords);
        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
        for (int i = 0; i < points.size(); i++) {//trajectorylist为轨迹集合
            newbounds.include(points.get(i));
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 260));//第二个参数为四周留空宽度
    }


    // 开始发车
    private void fetchStartBuslData(final String scheduleId, final DepartModel mdepartModel) {
        ObjectLoader.getInstance().getStartBuslData(new ProgressSubscriber<StartBustModel>(new SubscriberOnNextListener<StartBustModel>() {
            @Override
            public void onNext(StartBustModel startBustModel) {
//                tvBtn.setText("到达" + startName);
                tvBtn.setText("进行中...");
                isUpdateGps = true;
                isPolylineWalk = false;
                //发车之后移除步行辅助线
                if (mPolylineWalk != null) {
                    mPolylineWalk.remove();
                    mPolylineWalk = null;
                }
                isSendGps = true;
                mycircleHandler.removeCallbacks(runnable);
                mycircleHandler.postDelayed(runnable, 5000);
                CLICK_STATUS = END_BTN;
            }

            @Override
            public void onError(Throwable e) {
            }
        }, context), scheduleId);
    }


    // 结束发车
    private void fetchEndBusData(final String scheduleId) {
        ObjectLoader.getInstance().getEndBusModelData(new ProgressSubscriber<EndBusModel>(new SubscriberOnNextListener<EndBusModel>() {
            @Override
            public void onNext(EndBusModel endBusModel) {
                mlocationClient.stopLocation();
                mycircleHandler.removeCallbacks(runnable);
                isSendGps = false;
                isUpdateGps = false;
                isPolylineWalk = true;
//                aMap.clear();
                DepartOverActivity.launch(context, scheduleId, mdepartModel);
                CLICK_STATUS = DEPAR_BTN;
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "Throwable----" + e.getMessage());
            }
        }, context), scheduleId);
    }


    private void sendGps(double lat, double lon) {
        String data = SharedPref.getInstance(context).getString(Constant.USER_INFO, null);
        if (!TextUtils.isEmpty(data)) {
            LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
            int driverId = loginModel.getDriverId();

            DecimalFormat df = new java.text.DecimalFormat("#.000000");
            String longitude_f = df.format(lon);
            double longitude = Double.parseDouble(longitude_f);

            String latitude_f = df.format(lat);
            double latitude = Double.parseDouble(latitude_f);

            if (cityCode != null) {
                gpsUploadData(scheduleId, routeId, longitude, latitude, cityCode, busId, driverId); //业务消息类型 msgId: 0x201
            }
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
        if (infoWindowLayout == null) {
            infoWindowLayout = LayoutInflater.from(context).inflate(R.layout.customer_start_marker, null);

        }

        return infoWindowLayout;

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
        textureList.add(BitmapDescriptorFactory.fromResource(R.mipmap.route));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }


        mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.route)) //setCustomTextureList
                // (bitmapDescriptors)
                .setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));


        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
        for (int i = 0; i < list.size(); i++) {//trajectorylist为轨迹集合
            newbounds.include(list.get(i));
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 200));//第二个参数为四周留空宽度

    }

    private void addmark(List<DepartModel.ListBean> stations) {

//        第一步添加marker到地图上：
        markerOptionlist.clear();
        for (int i = 0; i < stations.size(); i++) {//此处dataList是存有想要添加marker点的集合
            MarkerOptions markerOptions = new MarkerOptions();//初始化 MarkerOptions对象

//            if (i != 0 && i != dataList.size() - 1) {
//                markerOptions.position(new LatLng(dataList.get(i).getLat(), dataList.get(i).getLon()));
//            }

            if (i == 0) {
                markerOptions.position(new LatLng(stations.get(0).getLat(), stations.get(0).getLon()));
                markerOptions.anchor(0.5f, 0.6f);
                view_complete = View.inflate(context, R.layout.customer_start_marker, null);
                setMarker(stations, 0, markerOptions);
            } else if (i == stations.size() - 1) {
                markerOptions.position(new LatLng(stations.get(stations.size() - 1).getLat(), stations.get(stations.size() - 1).getLon()));
                markerOptions.anchor(0.5f, 0.6f);
                view_complete = View.inflate(context, R.layout.customer_end_marker, null);
                setMarker(stations, stations.size() - 1, markerOptions);
            } else {
                markerOptions.position(new LatLng(stations.get(i).getLat(), stations.get(i).getLon()));
                markerOptions.anchor(0.5f, 0.2f);
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


    private void setMarker(List<DepartModel.ListBean> stations, int i, MarkerOptions markerOptions) {
//        tvPlanTime = view_complete.findViewById(R.id.tv_planTime);
//        String planTime = stations.get(i).getPlanTime();
//        String stationTime = TimeUtils.formatStationTime(planTime);
//        tvPlanTime.setText(stationTime);
        Bitmap bitmap_complete = convertViewToBitmap(view_complete);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap_complete));//设置marker图标
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
    private void gpsUploadData(String scheduleId, int routeId, double longitude, double latitude, String cityCode, int busId, int driverId) {
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        Log.e("token", "token---SharedPref----" + token + "-----" + "传gps需要的token");
        int websockt_cont = SharedPref.getInstance(context).getInt(Constant.WEBSOCKT_CONT, 0);

        long gpsTime = System.currentTimeMillis() / 1000;

        Map<String, Object> gpsUpload = WebSocketRequst.getInstance(context)
                .gpsUpload(scheduleId, routeId, longitude, latitude, cityCode, busId, driverId, gpsTime);

        String socketJsonGps = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x201, token, gpsUpload);

        if (websockt_cont == 1) {
            sendText(socketJsonGps);
        } else {
            //webSocket 断开后重新鉴权
            webSocketConnectLogin();
        }
        Log.e(THIS_FILE, "上传gps点时间------" + gpsTime);
        Log.e(THIS_FILE, "token---SharedPref----" + token);
        Log.e(THIS_FILE, "socketJsonGps----" + socketJsonGps);

    }


    /**
     * 获取今日线路
     *
     * @param id
     */
    private void fetchDepartData(int id) {
        ObjectLoader.getInstance().getDepartModelData(new ProgressSubscriber<DepartModel>(new SubscriberOnNextListener<DepartModel>() {

            @Override
            public void onNext(DepartModel departModel) {
                getDepartDetailModel(departModel);
            }

            @Override
            public void onError(Throwable e) {

            }
        }, context), id);
    }

    private void getDepartDetailModel(DepartModel departModel) {

        if (departModel != null) {
            mdepartModel = departModel;
            auArLoading.setVisibility(View.GONE);
            RavelItinerary.setVisibility(View.VISIBLE);
            status = departModel.getStatus();
            endName = departModel.getEndName();
            startName = departModel.getStartName();
            String date = departModel.getDate();

            if (status == 1) {
                alBtn.setVisibility(View.VISIBLE);
//                tvBtn.setText("到达" + startName);
                tvBtn.setText("进行中...");
                isUpdateGps = true;
                isPolylineWalk = false;
                isSendGps = true;
                mycircleHandler.removeCallbacks(runnable);
                mycircleHandler.postDelayed(runnable, 5000);
                CLICK_STATUS = END_BTN;
            } else if (status == 0) {
                String dateTime = TimeUtils.getYMD_(date);
                //获取今日时间
                String dateToDay = TimeUtils.dateToStr(TimeUtils.getNow());

                if (dateTime.equals(dateToDay)) {
                    alBtn.setVisibility(View.VISIBLE);
                    tvBtn.setText("发车");
                } else {
                    alBtn.setVisibility(View.GONE);
                }


            }
            Log.e(THIS_FILE, "tatus----" + status);

            RavelItinerary.setVisibility(View.VISIBLE);
            RavelItineraryNull.setVisibility(View.GONE);


            tvCarPai.setText(departModel.getBusPlateNumber());
            tvStationstart.setText(startName);
            tvStationEnd.setText(endName);


            scheduleId = departModel.getId();
            departModelList = departModel.getList();
            routeId = departModel.getRouteId();
            busId = departModel.getBusId();
            dateTime = date;

            tvDepartTime.setText("今天" + TimeUtils.getMD(dateTime));


            //站点信息
//            aMap.clear();
            coordsStation.clear();
            for (DepartModel.ListBean pointStation : departModelList) {
                coordsStation.add(pointStation.getLon());
                coordsStation.add(pointStation.getLat());
                boolean uploadPoint = pointStation.isUploadPoint(); //站点是否上传
            }

            points = readLatLngs(coordsStation);//需要上传的站点信息

//            addStartAndEnd(departModelList); //设置起点和终点
            addmark(departModelList);       //添加站点信息图片

            pointList = departModel.getPointList();//辅助点信息

            size = departModelList.size();
            DepartModel.ListBean depart = departModelList.get(0);


            if (isPolylineWalk) {
                // 步行终点坐标
                mEndPoint = new LatLonPoint(depart.getLat(), depart.getLon());
                mStartPoint = new LatLonPoint(latitude, longitude);
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);//定位成功 规划路径导航
            }

            setStation();//设置今日站点信息

            //辅助点信息
            coords.clear();
            for (DepartModel.PointListBean point : pointList) {
                coords.add(point.getLongitude());
                coords.add(point.getLatitude());
            }

            if (mPolyline != null) {
                mPolyline.remove();
                mPolyline = null;
            }
            addPolylineInPlayGround(coords); //绘制辅助线

        } else {
            RavelItinerary.setVisibility(View.GONE);
            RavelItineraryNull.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置起点终点站
     *
     * @param departModelList
     */
    private void addStartAndEnd(List<DepartModel.ListBean> departModelList) {

        View view_start = View.inflate(getActivity(), R.layout.customer_start_marker, null);
        Bitmap bitmap_start = convertViewToBitmap(view_start);
        MarkerOptions startMarker = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_start));
        DepartModel.ListBean startStation = departModelList.get(0);
        LatLng startLatlng = new LatLng(startStation.getLat(), startStation.getLon());
        startMarker.position(startLatlng);
        aMap.addMarker(startMarker);


        View view_end = View.inflate(getActivity(), R.layout.customer_end_marker, null);
        Bitmap bitmap_end = convertViewToBitmap(view_end);
        MarkerOptions endMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap_end));
        DepartModel.ListBean endStation = departModelList.get(departModelList.size() - 1);
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
     * 是否可以发车
     *
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

        if (Double.parseDouble(timeDifferenceHour) > 1) {

            //早于到站时间 1小时 不能发车
            tvBtnNoClick.setText("未到发车时间");
            btnBgNoClick.setVisibility(View.VISIBLE);
            btnBg.setVisibility(View.GONE);
            alBtn.setEnabled(false);
        } else {

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
    }


    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void ononMoonStickyEvent(ReceiveDateEvent receiveDateEvent) {
        int tag = receiveDateEvent.getTag();
        driverId = SharedPref.getInstance(context).getInt(Constant.DRIVER_ID, 0);
        isFirstLoc = true;
        switch (tag) {
            case Constant.CLICK_DEPART_TAB:
                //点击 发车tab 加载数据
                fetchDepartData(driverId);
                break;

//            case Constant.CLICK_ITEM:
//                aMap.clear();
//                mlocationClient.startLocation();
////                startLocationClient();
//                //点击 待发车 item 获取待发车详情
//                String itemId = receiveDateEvent.getItemId();
//                fetchDepartDetailData(itemId);
//                Log.e(THIS_FILE, "receiveDateEvent-----" + itemId);
//                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void receiveDateItemEvent(ReceiveDateItemEvent event) {
        aMap.clear();
        //点击 待发车 item 获取待发车详情
        String itemId = event.getItemId();
        fetchDepartDetailData(itemId);
        Log.e(THIS_FILE, "receiveDateEvent-----" + itemId);
    }


    /**
     * 获取待出行详情
     *
     * @param itemId
     */
    private void fetchDepartDetailData(String itemId) {

        ObjectLoader.getInstance().getDepartDetailModelData(new ProgressSubscriber<DepartModel>(new SubscriberOnNextListener<DepartModel>() {
            @Override
            public void onNext(DepartModel departDetailModel) {
                getDepartDetailModel(departDetailModel);
            }

            @Override
            public void onError(Throwable e) {

                Log.e(THIS_FILE, "e-----" + e.getMessage());

            }
        }, context), itemId);

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    List<WalkPath> paths = result.getPaths();

                    coordsWalkRoute.clear();

                    for (WalkPath path : paths) {
                        List<WalkStep> pathSteps = path.getSteps();

                        for (WalkStep pathStep : pathSteps) {
                            List<LatLonPoint> pathStepPolylines = pathStep.getPolyline();

                            for (LatLonPoint pathStepPolyline : pathStepPolylines) {
                                coordsWalkRoute.add(pathStepPolyline.getLongitude());
                                coordsWalkRoute.add(pathStepPolyline.getLatitude());
                            }
                        }

                    }

                    if (mPolylineWalk != null) {
                        mPolylineWalk.remove();
                        mPolylineWalk = null;
                    }

                    addPolylineInPlayGroundWalk(coordsWalkRoute);


                    Log.e(THIS_FILE, "result----" + result.toString());
                }

            } else {

            }
        }

    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGroundWalk(List<Double> coordPoints) {
        List<LatLng> list = readLatLngs(coordPoints);
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.shape_dotted));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolylineWalk = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.shape_dotted))
                //setCustomTextureList
                // (bitmapDescriptors)
                .setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .color(Color.parseColor("#FF5349FC"))
                .setDottedLine(true)
                .width(18));


        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
        for (int i = 0; i < list.size(); i++) {
            newbounds.include(list.get(i));
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 200));//第二个参数为四周留空宽度

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private RecyclerItemCallback<DepartModel.ListBean, DepartAdapter.ViewHolder> departItemClick = new RecyclerItemCallback<DepartModel.ListBean, DepartAdapter.ViewHolder>() {

        @Override
        public void onItemClick(int position, DepartModel.ListBean model, int tag, DepartAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);
            onMapLoaded(model.getLat(), model.getLon());
//            setPlanTimeMarker(model);
        }
    };

    private RecyclerItemCallback<DepartModel.ListBean, DepartFullAdapter.ViewHolder> RecItemFull = new RecyclerItemCallback<DepartModel.ListBean, DepartFullAdapter.ViewHolder>() {

        @Override
        public void onItemClick(int position, DepartModel.ListBean model, int tag, DepartFullAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);

//            setPlanTimeMarker(model);
        }
    };


    private void setPlanTimeMarker(DepartModel.ListBean model) {
        String planTime = model.getPlanTime();

        View view_ = View.inflate(getActivity(), R.layout.customer_time, null);
        TextView view_time = view_.findViewById(R.id.tv_planTime);
        String stationTime = TimeUtils.formatStationTime(planTime);
        view_time.setText(stationTime);

        if (marker_time != null) {
            marker_time.remove();
        }

        Bitmap bitmap_time = convertViewToBitmap(view_);
        MarkerOptions timeMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap_time));
        timeMarker.anchor(0.5f, -0.3f);
        LatLng startLatlng = new LatLng(model.getLat(), model.getLon());
        timeMarker.position(startLatlng);
        marker_time = aMap.addMarker(timeMarker);
    }


    public void onMapLoaded(double latitude, double longitude) {
        LatLng marker1 = new LatLng(latitude, longitude);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void mainThread(StartLocationEvent event) {
        ShowDialogRelative.toastDialog(context, "开启定位");
        aMap.clear();
        mlocationClient.startLocation();
    }
}

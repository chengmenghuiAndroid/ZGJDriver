package com.lty.zgj.driver.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.lty.zgj.driver.adapter.HistoricalJourneyDetailAdapter;
import com.lty.zgj.driver.adapter.HistoricalJourneyDetailFullAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/6/21.
 */

public class HistoricalJourneyDetailActivity extends BaseXActivity implements AMapLocationListener, LocationSource {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_plate_numbers)
    TextView tvPlateNumbers;
    @BindView(R.id.tv_station_start)
    TextView tvStationStart;
    @BindView(R.id.icon_arrowhead)
    ImageView iconArrowhead;
    @BindView(R.id.tv_station_end)
    TextView tvStationEnd;
    @BindView(R.id.au_al)
    AutoRelativeLayout auAl;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.ar)
    AutoRelativeLayout ar;
    @BindView(R.id.al_autoLinearLayout)
    AutoLinearLayout alAutoLinearLayout;
    @BindView(R.id.tv_ticket_number)
    TextView tvTicketNumber;
    @BindView(R.id.tv_km)
    TextView tvKm;
    @BindView(R.id.xrecyclerview_bad_time)
    XRecyclerView xrecyclerviewBadTime;
    @BindView(R.id.ar_bad_time)
    AutoRelativeLayout arBadTime;
    @BindView(R.id.tv_unfoldRv_down)
    ImageView tvUnfoldRvDown;
    @BindView(R.id.tv_unfoldRv_up)
    ImageView tvUnfoldRvUp;
    @BindView(R.id.map)
    TextureMapView mapView;
//    @BindView(R.id.s_reboundScrollView)
//    ReboundScrollView sReboundScrollView;

    private HistoricalJourneyDetailAdapter detailAdapter;

    List<HistoricalJourneyDetailModel.StationsBean> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    List<Double> pointsRepairShop = new ArrayList<Double>();
    List<Double> coords = new ArrayList<Double>();
    List<Double> recoords = new ArrayList<Double>();
    private float mapZoom;
    private LatLng mapTarget;
    private Polyline mPolyline;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private String webSocketJson;


    private ViewGroup.LayoutParams layoutParams;
    private String THIS_FILE = "HistoricalJourneyDetailActivity";
    private View mHeader;
    private List<HistoricalJourneyDetailModel.StationsBean> stations;
    private int size;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private HistoricalJourneyDetailFullAdapter detailFullAdapter;

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private ArrayList<Marker> markerList;
    private ArrayList<MarkerOptions> markerOptionlist = new ArrayList<>();
    private TextView tvPlanTime;
    private View view_complete;
    private List<HistoricalJourneyDetailModel.RelStationsBean> relStations;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
        initViewData();

    }

    private void initViewData() {
        tvTitle.setText("行程详情");
        getUiDelegate().visible(true, navButton);
        checkPermissionsLocation();
        initRv();
        init();
//        layoutParams = alAutoLinearLayout.getLayoutParams();
        String id = getIntent().getStringExtra("id");
        fetchTripDetailData(id);

        int status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            tvStatus.setText("已完成");
        } else if (status == 0) {
            tvStatus.setText("待进行");
        }
        Log.e(THIS_FILE, "status----" + status);
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
        // 设置定位监听
        aMap.setLocationSource(this);
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.getUiSettings().setTiltGesturesEnabled(false);//设置地图是否可以倾斜
        aMap.getUiSettings().setRotateGesturesEnabled(true);//设置地图是否可以旋转
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //初始化定位蓝点样式类

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
        mLocationOption.setInterval(2000);
        mLocationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
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


    private void initRv() {
        setLayoutManager(xrecyclerview);
        mHeader = View.inflate(context, R.layout.depart_icon, null);
        getAdapter().setIconView(mHeader);
        xrecyclerview.setAdapter(getAdapter());
    }

    public HistoricalJourneyDetailAdapter getAdapter() {
        if (detailAdapter == null) {
            detailAdapter = new HistoricalJourneyDetailAdapter(context);
            detailAdapter.setRecItemClick(itemClick);
        } else {
            detailAdapter.notifyDataSetChanged();
        }
        return detailAdapter;

    }

    public HistoricalJourneyDetailFullAdapter getFullAdapter() {
        if (detailFullAdapter == null) {
            detailFullAdapter = new HistoricalJourneyDetailFullAdapter(context);

        } else {
            detailFullAdapter.notifyDataSetChanged();
        }
        return detailFullAdapter;

    }

    private RecyclerItemCallback<HistoricalJourneyDetailModel.StationsBean, HistoricalJourneyDetailAdapter.ViewHolder> itemClick = new RecyclerItemCallback<HistoricalJourneyDetailModel.StationsBean, HistoricalJourneyDetailAdapter.ViewHolder>() {
        /**
         * 单击事件
         *
         * @param position 位置
         * @param model    实体
         * @param tag      标签
         * @param holder   控件
         */
        @Override
        public void onItemClick(int position, HistoricalJourneyDetailModel.StationsBean model, int tag, HistoricalJourneyDetailAdapter.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);

            ShowDialogRelative.toastDialog(context, "点击了" + position);
        }
    };


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_historicaljourney_detail_1;

    }


    public static void launch(Activity activity, int status, int tripNo, String id) {
        Router.newIntent(activity)
                .to(HistoricalJourneyDetailActivity.class)
                .putInt("status", status)
                .putInt("tripNo", tripNo)
                .putString("id", id)
                .launch();
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

                    tvPlateNumbers.setText(historicalJourneyDetailModel.getBusNum());//车牌号
                    tvTicketNumber.setText("购票人数" + historicalJourneyDetailModel.getNums());
                    tvStationStart.setText(historicalJourneyDetailModel.getStartName());
                    tvStationEnd.setText(historicalJourneyDetailModel.getEndName());

                    String dateTime = historicalJourneyDetailModel.getDate();

                    tvTime.setText(TimeUtils.getYMD(dateTime));

                    int status = historicalJourneyDetailModel.getStatus();

                    if (status == 0) {
                        tvStatus.setText("未开始");
                    } else if (status == 1) {
                        tvStatus.setText("进行中");
                    }else if (status == 2) {
                        tvStatus.setText("已结束");
                    }else if (status == 3) {
                        tvStatus.setText("取消");
                    }

                    //计划辅助点信息
                    List<HistoricalJourneyDetailModel.AssitsBean> assits = historicalJourneyDetailModel.getAssits();
                    //辅助点信息
                    coords.clear();
                    if (assits != null && assits.size() > 0) {
                        for (HistoricalJourneyDetailModel.AssitsBean point : assits) {
                            coords.add(point.getLon());
                            coords.add(point.getLat());
                        }
                        addPolylineInPlayGround(coords, R.mipmap.route_re); //计划辅助点信息
                    }


                    //实际辅助点信息
                    List<HistoricalJourneyDetailModel.RelAssitsBean> relAssits = historicalJourneyDetailModel.getRelAssits();
                    //辅助点信息
                    recoords.clear();
                    if (relAssits != null && relAssits.size() > 0) {
                        for (HistoricalJourneyDetailModel.RelAssitsBean repoint : relAssits) {
                            recoords.add(repoint.getLon());
                            recoords.add(repoint.getLat());
                        }
                        addPolylineInPlayGround(recoords, R.mipmap.route); //实际辅助点信息
                    }

                    //添加终点起点
//                    addStartAndEnd(assits);

                    if (stations != null && stations.size() > 0) {
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


    private void addmark(List<HistoricalJourneyDetailModel.StationsBean> stations) {


//        第一步添加marker到地图上：
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

    private void setMarker(List<HistoricalJourneyDetailModel.StationsBean> stations, int i, MarkerOptions markerOptions) {
        tvPlanTime = view_complete.findViewById(R.id.tv_planTime);

        if (relStations != null && relStations.size() > 0) {
            for (HistoricalJourneyDetailModel.RelStationsBean relStation : relStations) {
                int stationId = stations.get(i).getStationId();
                int stationIRe = relStation.getStationId();
                if (stationId == stationIRe) {
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
        TextView tvPlanTimeStart = view_start.findViewById(R.id.tv_planTime);

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
                .width(18));


        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
        for (int i = 0; i < list.size(); i++) {//trajectorylist为轨迹集合
            newbounds.include(list.get(i));
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 300));//第二个参数为四周留空宽度
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

    private void setStation() {

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


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up,
    })

    public void unfoldRvOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_unfoldRv_down:
                //设置展开高度

                list.clear();
                xrecyclerview.setAdapter(getFullAdapter());
                list.clear();
                if (stations != null && stations.size() > 0) {
                    list.addAll(stations);
                    getFullAdapter().setData(list);
                }
                tvUnfoldRvDown.setVisibility(View.GONE);
                tvUnfoldRvUp.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                break;

            case R.id.tv_unfoldRv_up:
                xrecyclerview.setAdapter(getAdapter());
                setStation();
                tvUnfoldRvDown.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(amapLocation);
                    //点击定位按钮 能够将地图的中心移动到定位点
                    if (isFirstLoc) {
                        //将地图移动到定位点
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                        //添加图钉
//                      aMap.addMarker(getMarkerOptions(amapLocation));
                        //获取定位信息
                        isFirstLoc = false;
                    }
                    StringBuffer buffer = new StringBuffer();

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

    public void back(View view) {
        finish();
    }
}

package com.lty.zgj.driver.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.HistoricalJourneyDetailAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.core.tool.ScreenUtils;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.MapContainer;
import com.lty.zgj.driver.weight.ReboundScrollView;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

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

public class HistoricalJourneyDetailActivity extends BaseXActivity implements AMapLocationListener , LocationSource{

    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.al_autoLinearLayout)
    AutoLinearLayout autoLinearLayout;
    @BindView(R.id.tv_unfoldRv_down)
    TextView tvUnfoldRvDown;
    @BindView(R.id.tv_unfoldRv_up)
    TextView tvUnfoldRvUp;
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.s_reboundScrollView)
    ReboundScrollView reboundScrollView;
    @BindView(R.id.map_container)
    MapContainer mapContainer;
    @BindView(R.id.tv_plate_numbers)
    TextView tvPlateNumbers;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_ticket_number)
    TextView tvTicketNumber;
    @BindView(R.id.tv_startName)
    TextView tvStartName;
    @BindView(R.id.tv_endName)
    TextView tvEndName;

    private HistoricalJourneyDetailAdapter detailAdapter;

    List<HistoricalJourneyDetailModel.StationsBean> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    List<Double> pointsRepairShop = new ArrayList<Double>();
    List<Double> coords = new ArrayList<Double>();
    private float mapZoom;
    private LatLng mapTarget;
    private Polyline mPolyline;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private String webSocketJson;


    private ViewGroup.LayoutParams layoutParams;
    private String THIS_FILE = "DepartFragment";
    private View mHeader;
    private List<HistoricalJourneyDetailModel.StationsBean> stations;
    private int size;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;


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
        checkPermissionsLocation();
        initRv();
        init();
        layoutParams = autoLinearLayout.getLayoutParams();
        fetchTripDetailData(1, 5, 11);
        mapContainer.setScrollView(reboundScrollView);

        //禁止滑动
        setReboundScrollView();
        int status = getIntent().getIntExtra("status", 0);
        if(status == 1){
            tvStatus.setText("已完成");
        }else if(status == 2){
            tvStatus.setText("已取消");
        }
        Log.e(THIS_FILE, "status----"+status);
    }

    private void setReboundScrollView() {
        reboundScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }


        });
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

    private RecyclerItemCallback<HistoricalJourneyDetailModel.StationsBean,HistoricalJourneyDetailAdapter.ViewHolder> itemClick = new RecyclerItemCallback<HistoricalJourneyDetailModel.StationsBean, HistoricalJourneyDetailAdapter.ViewHolder>() {
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

            ShowDialogRelative.toastDialog(context, "点击了"+ position);
        }
    };


    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_historicaljourney_detail;

    }


    public static void launch(Activity activity, int status, int tripNo) {
        Router.newIntent(activity)
                .to(HistoricalJourneyDetailActivity.class)
                .putInt("status", status)
                .putInt("tripNo", tripNo)
                .launch();
    }

    private void fetchTripDetailData(int Id, int tripNo, int routeId) {
        ObjectLoader.getInstance().getTripDetailData(new ProgressSubscriber<HistoricalJourneyDetailModel>(new SubscriberOnNextListener<HistoricalJourneyDetailModel>() {
            @Override
            public void onNext(HistoricalJourneyDetailModel historicalJourneyDetailModel) {

                if (historicalJourneyDetailModel != null) {
                    stations = historicalJourneyDetailModel.getStations();
                    size = stations.size();
                    setStation();

                    tvPlateNumbers.setText(historicalJourneyDetailModel.getBusNum());//车牌号
                    tvTicketNumber.setText("购票人数"+historicalJourneyDetailModel.getNums());
                    tvStartName.setText(historicalJourneyDetailModel.getStartName());
                    tvEndName.setText(historicalJourneyDetailModel.getEndName());
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Throwable", "---" + e.getMessage());
            }
        }, context), Id, tripNo, routeId);
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
                layoutParams.height = ScreenUtils.getScreenHeight(context) - 1000;
                autoLinearLayout.setLayoutParams(layoutParams);

                list.clear();
                list.addAll(stations);
                getAdapter().setData(list);
                tvUnfoldRvDown.setVisibility(View.GONE);
                tvUnfoldRvUp.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_unfoldRv_up:
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                autoLinearLayout.setLayoutParams(layoutParams);
                setStation();
                tvUnfoldRvDown.setVisibility(View.VISIBLE);
                tvUnfoldRvUp.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {


                    if (isFirstLoc) {
                        //将地图移动到定位点
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(amapLocation);
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
}

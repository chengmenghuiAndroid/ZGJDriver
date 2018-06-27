package com.lty.zgj.driver.ui.activity;

import android.Manifest;
import android.app.Activity;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.adapter.DepartOverAdapter;
import com.lty.zgj.driver.adapter.DepartOverFullAdapter;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.DepartModel;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.weight.StatusBarUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.router.Router;
import cn.droidlover.xrecyclerview.XRecyclerView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/6/19.
 */

public class DepartOverActivity extends BaseXActivity implements AMapLocationListener, LocationSource{
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
    private List<DepartModel.ListBean> list = new ArrayList<>();
    private DepartOverFullAdapter departOverFullAdapter;
    private List<DepartModel.ListBean> departModelList;

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


        DepartModel  mdepartModel = (DepartModel) getIntent().getSerializableExtra("mdepartModel");
        departModelList = mdepartModel.getList();
        setStation();
    }

    private void setStation() {
        if (departModelList != null && departModelList.size() > 0) {
            int size = departModelList.size();

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


    @OnClick({
            R.id.tv_unfoldRv_down,
            R.id.tv_unfoldRv_up,

    })

    public void unfoldRvOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_unfoldRv_up:

                //设置展开高度
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                autoLinearLayout.setLayoutParams(layoutParams);

                xrecyclerview.setAdapter(getFullAdapter());
                list.clear();

                if (departModelList != null && departModelList.size() > 0) {
                    list.addAll(departModelList);
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

}

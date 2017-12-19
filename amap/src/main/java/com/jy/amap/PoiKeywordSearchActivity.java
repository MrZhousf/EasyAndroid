package com.jy.amap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jy.amap.adapter.PoiAdapter;
import com.jy.amap.util.AMapUtil;
import com.jy.amap.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhousf
 * @description : POI关键字搜索
 * @date : 2017/3/28.
 */
public class PoiKeywordSearchActivity extends CheckPermissionsActivity
        implements LocationUtil.LocationListener,View.OnClickListener,TextWatcher,
        PoiSearch.OnPoiSearchListener {

    private static final String TAG = PoiKeywordSearchActivity.class.getSimpleName();

    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progressDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private String currentCity = "";//当前城市
    private double fromLongitude = 0;//当前经度
    private double fromLatitude = 0;//当前纬度
    private double toLongitude = 0;//目的地经度
    private double toLatitude = 0;//目的地纬度
    private List<PoiItem> poiItems = new ArrayList<>();
    private ListView resultListView;
    private PoiAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_keyword_search);
        initView();
        location();
    }

    /**
     * 定位
     */
    private void location() {
        LocationUtil.getInstance().getLastLocation(this,this);
        showProgressDialog("正在定位...");
    }


    @Override
    public void onLocationChanged(boolean isSuccess, AMapLocation aMapLocation) {
        dismissProgressDialog();
        if (isSuccess) {
            fromLongitude = aMapLocation.getLongitude();//经度
            fromLatitude = aMapLocation.getLatitude();//纬度
            currentCity = aMapLocation.getCity();
        } else {
            Log.d(TAG,"定位失败:默认北京市");
            currentCity = "北京市";
        }
        keyWord = getIntent().getStringExtra("searchKey");
        if(!TextUtils.isEmpty(keyWord)){
            searchText.setText(keyWord);
            searchText.setSelection(searchText.getText().length());
            showProgressDialog("正在搜索:\n" + keyWord);// 显示进度框
        }
    }

    /**
     * 初始化视图组件
     */
    private void initView(){
        resultListView = (ListView) findViewById(R.id.resultListView);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoAMap(position);
            }
        });
        resultAdapter = new PoiAdapter(poiItems);
        resultListView.setAdapter(resultAdapter);
        TextView searButton = (TextView) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.clearFocus();
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
    }

    /**
     * 进入基础地图页面
     */
    private void gotoAMap(int position){
        if(poiItems != null && !poiItems.isEmpty()){
            PoiItem poi = poiItems.get(position);
            String title = poi.getTitle();
            if(poi.getLatLonPoint() != null){
                toLongitude = poi.getLatLonPoint().getLongitude();
                toLatitude = poi.getLatLonPoint().getLatitude();
                Bundle bundle = new Bundle();
                bundle.putDouble("fromLongitude", fromLongitude);
                bundle.putDouble("fromLatitude", fromLatitude);
                bundle.putDouble("toLongitude", toLongitude);
                bundle.putDouble("toLatitude", toLatitude);
                bundle.putString("currentCity", currentCity);
                bundle.putString("title",title);
                bundle.putString("snippet",poi.getSnippet());
                bundle.putBoolean("isFromPOI",true);
                AMapAPI.toAMapActivity(this,bundle);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchButton){//点击搜索按钮
            clickSearchBtn();
        }
    }

    /**
     * 点击搜索按钮
     */
    public void clickSearchBtn() {
        keyWord = AMapUtil.checkEditText(searchText);
        if ("".equals(keyWord)) {
            Toast.makeText(PoiKeywordSearchActivity.this, "请输入搜索关键字",Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog("正在搜索:\n" + keyWord);// 显示进度框
            doSearchQuery();
            hideInputBoard();
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        if(!progressDialog.isShowing()){
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    /**
     * 隐藏进度框
     */
    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(searchText.getText().toString(), "", currentCity);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 隐藏输入法键盘
     */
    private void hideInputBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    Log.d(TAG,"size of poiItems = "+poiItems.size());
                    if (poiItems != null && poiItems.size() > 0) {
                        resultAdapter.setData(poiItems);
                    } else {
                        Toast.makeText(PoiKeywordSearchActivity.this,"对不起，没有搜索到相关数据！",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(PoiKeywordSearchActivity.this,"对不起，没有搜索到相关数据！",Toast.LENGTH_SHORT).show();
            }
        } else if(rCode == AMapException.CODE_AMAP_CLIENT_UNKNOWHOST_EXCEPTION){
            Toast.makeText(PoiKeywordSearchActivity.this,"请检查网络状态！",Toast.LENGTH_SHORT).show();
        } else{
            Log.d(TAG,"onPoiSearched 搜索失败: rCode="+rCode);
        }
        dismissProgressDialog();// 隐藏对话框
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        Log.d(TAG,"onTextChanged :"+newText);
        keyWord = AMapUtil.checkEditText(searchText);
        doSearchQuery();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }




}

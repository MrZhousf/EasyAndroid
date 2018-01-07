package com.easyandroid.demo;

import android.app.SearchManager;
import android.os.Bundle;
import android.widget.TextView;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;

import butterknife.Bind;

public class SearchResultActivity extends BaseActivity {

    @Bind(R.id.TVSearch)
    TextView TVSearch;

    @Override
    protected int initLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "搜索";
        title.showSearch = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String searchContent = getIntent().getStringExtra(SearchManager.QUERY);
        TVSearch.setText(searchContent);
    }


}

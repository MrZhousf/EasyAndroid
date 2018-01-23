package com.easy.app.demo;

import android.app.SearchManager;
import android.os.Bundle;
import android.widget.TextView;

import com.easy.R;
import com.easy.app.core.base.HomeActivity;

import butterknife.Bind;

public class SearchResultActivity extends HomeActivity {

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

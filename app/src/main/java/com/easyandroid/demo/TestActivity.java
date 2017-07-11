package com.easyandroid.demo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseRefreshActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TestActivity extends BaseRefreshActivity {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<Integer> list = new ArrayList<>();

    TestAdapter adapter;


    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initTitle(TitleBar title) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TestAdapter();
        recyclerView.setAdapter(adapter);
        for (int i = 5; i <20 ; i++) {
            list.add(i);
        }
        adapter.addAll(list);

    }

    @Override
    protected void onRefreshTop() {

    }

    @Override
    protected void onRefreshBottom() {

    }

}

package com.easyandroid.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseRecyclerViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/7/6.
 */
public class TestAdapter extends BaseRecyclerViewAdapter<Integer, RecyclerView.ViewHolder> {

    private Context context;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((TestHolder) viewHolder).bind(getItem(position));
        super.onBindViewHolder(viewHolder, position);
    }

    class TestHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.linear_layout)
        LinearLayout linear_layout;


        private TestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Integer num){
            linear_layout.removeAllViews();
            for (int i = 0; i < num; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j <5 ; j++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) );
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    layout.addView(imageView);
                }
                linear_layout.addView(layout);
            }


        }


    }


}

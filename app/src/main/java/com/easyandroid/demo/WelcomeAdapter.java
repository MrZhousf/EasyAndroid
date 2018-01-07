package com.easyandroid.demo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseRecyclerViewAdapter;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/12/29.
 */
public class WelcomeAdapter extends BaseRecyclerViewAdapter<WelcomeAdapter.Info,RecyclerView.ViewHolder> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_welcome, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((MyViewHolder) viewHolder).bind(getItem(position));
        super.onBindViewHolder(viewHolder, position);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tv_info;

        private MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
        }

        void bind(Info info){
            tv_info.setText(info.name);
            cardView.setOnClickListener(info.onClickListener);
        }

    }

    public static class Info {
        String name;
        View.OnClickListener onClickListener;

        public Info(String name, View.OnClickListener onClickListener) {
            this.name = name;
            this.onClickListener = onClickListener;
        }
    }


}

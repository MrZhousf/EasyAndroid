package com.jy.amap.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.jy.amap.R;

import java.util.List;

/**
 * @author : zhousf
 * @description : POI关键字搜索适配器
 * @date : 2017/3/28.
 */

public class PoiAdapter extends BaseAdapter {

    private List<PoiItem> list;

    public PoiAdapter(List<PoiItem> list) {
        this.list = list;
    }

    public void setData(List<PoiItem> data){
        list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_route_inputs, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(list.get(position).getTitle()))
            viewHolder.title.setText(list.get(position).getTitle());
        if(!TextUtils.isEmpty(list.get(position).getSnippet()))
            viewHolder.info.setText(list.get(position).getSnippet());
        return convertView;
    }


    private final static class ViewHolder {
        TextView title;
        TextView info;
    }





}

package com.easy.app.demo.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easy.R;
import com.easy.app.core.base.BaseRecyclerViewAdapter;
import com.easy.app.core.util.GlideUtil;
import com.easy.app.core.util.StringUtil;
import com.easy.app.demo.weather.bean.Weather;
import com.easy.app.demo.weather.util.DateUtil;
import com.easy.lib.util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherAdapter extends BaseRecyclerViewAdapter<Weather, RecyclerView.ViewHolder> {

    private final int TYPE_TEMPERATURE = 0;
    private final int TYPE_SUGGESTION = 1;
    private final int TYPE_FUTURE = 2;

    public WeatherAdapter() {
    }

    @Override
    public int getItemCount() {
        return getList().size()*3;
//        if(isNotEmpty()){
//            return getFirstItem().status != null ? 3 : 0;
//        }else{
//            return super.getItemCount();
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0 || (position%3==0&&position>=3)){
            return TYPE_TEMPERATURE;
        }
        if(position ==1 ||(position%3==1)){
            return TYPE_SUGGESTION;
        }
        if(position ==2 ||(position%3==2)){
            return TYPE_FUTURE;
        }
//        if (position == 0) {
//            return TYPE_TEMPERATURE;
//        } else if (position == 1) {
//            return TYPE_SUGGESTION;
//        } else if (position ==2 ){
//            return TYPE_FUTURE;
//        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEMPERATURE:
                return new TemperatureViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temperature, parent, false));
            case TYPE_SUGGESTION:
                return new SuggestionViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false));
            case TYPE_FUTURE:
                return new FutureViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_future, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_TEMPERATURE:
                ((TemperatureViewHolder) viewHolder).bind(getFirstItem());
                break;
            case TYPE_SUGGESTION:
                ((SuggestionViewHolder) viewHolder).bind(getFirstItem());
                break;
            case TYPE_FUTURE:
                ((FutureViewHolder) viewHolder).bind(getFirstItem());
                break;
        }
        super.onBindViewHolder(viewHolder, position);
    }

    class TemperatureViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.weather_icon)
        ImageView weatherIcon;
        @Bind(R.id.tvCity)
        TextView tvCity;
        @Bind(R.id.temp_flu)
        TextView tempFlu;
        @Bind(R.id.temp_max)
        TextView tempMax;
        @Bind(R.id.temp_min)
        TextView tempMin;
        @Bind(R.id.temp_pm)
        TextView tempPm;
        @Bind(R.id.temp_quality)
        TextView tempQuality;
        @Bind(R.id.tvDetail)
        TextView tvDetail;

        TemperatureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Weather weather) {
            try {
                tvCity.setText(StringUtil.safeText(weather.basic.city));
                tvDetail.setText(StringUtil.safeText(weather.now.cond.txt));
                tempFlu.setText(String.format("%s℃", weather.now.tmp));
                tempMax.setText(
                        String.format("↑ %s ℃", weather.dailyForecast.get(0).tmp.max));
                tempMin.setText(
                        String.format("↓ %s ℃", weather.dailyForecast.get(0).tmp.min));
                tempPm.setText(String.format("PM2.5: %s μg/m³", StringUtil.safeText(weather.aqi.city.pm25)));
                tempQuality.setText(StringUtil.safeText("空气质量： ", weather.aqi.city.qlty));
                GlideUtil.loadImg(weatherIcon,
                        Util.SP.weather().getInt(weather.now.cond.txt, R.mipmap.type_two_sunny)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cloth_brief)
        TextView clothBrief;
        @Bind(R.id.cloth_txt)
        TextView clothTxt;
        @Bind(R.id.sport_brief)
        TextView sportBrief;
        @Bind(R.id.sport_txt)
        TextView sportTxt;
        @Bind(R.id.travel_brief)
        TextView travelBrief;
        @Bind(R.id.travel_txt)
        TextView travelTxt;
        @Bind(R.id.flu_brief)
        TextView fluBrief;
        @Bind(R.id.flu_txt)
        TextView fluTxt;

        SuggestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Weather weather) {
            try {
                clothBrief.setText(String.format("穿衣指数---%s", weather.suggestion.drsg.brf));
                clothTxt.setText(weather.suggestion.drsg.txt);
                sportBrief.setText(String.format("运动指数---%s", weather.suggestion.sport.brf));
                sportTxt.setText(weather.suggestion.sport.txt);
                travelBrief.setText(String.format("旅游指数---%s", weather.suggestion.trav.brf));
                travelTxt.setText(weather.suggestion.trav.txt);
                fluBrief.setText(String.format("感冒指数---%s", weather.suggestion.flu.brf));
                fluTxt.setText(weather.suggestion.flu.txt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FutureViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout llFuture;
        private TextView[] tvDate;
        private TextView[] tvTemp;
        private TextView[] tvDetail;
        private ImageView[] ivIcon;

        private List<Weather.DailyForecastEntity> dailyForecast;

        FutureViewHolder(View itemView) {
            super(itemView);
            llFuture = (LinearLayout) itemView.findViewById(R.id.llFuture);
            dailyForecast = getFirstItem().dailyForecast;
            if(null != dailyForecast && dailyForecast.size() > 0){
                tvDate = new TextView[dailyForecast.size()];
                tvTemp = new TextView[dailyForecast.size()];
                tvDetail = new TextView[dailyForecast.size()];
                ivIcon = new ImageView[dailyForecast.size()];
                for (int i = 0; i < dailyForecast.size(); i++) {
                    View view = View.inflate(itemView.getContext(), R.layout.item_future_row, null);
                    tvDate[i] = (TextView) view.findViewById(R.id.tvDate);
                    tvTemp[i] = (TextView) view.findViewById(R.id.tvTemp);
                    tvDetail[i] = (TextView) view.findViewById(R.id.tvDetail);
                    ivIcon[i] = (ImageView) view.findViewById(R.id.ivIcon);
                    llFuture.addView(view);
                }
            }
        }

        void bind(Weather weather) {
            try {
                if(null != dailyForecast && dailyForecast.size() > 0) {
                    tvDate[0].setText("今日");
                    tvDate[1].setText("明日");
                    for (int i = 0; i < weather.dailyForecast.size(); i++) {
                        if (i > 1) {
                            tvDate[i].setText(DateUtil.timeToWeek(weather.dailyForecast.get(i).date));
                        }
                        GlideUtil.loadImg(ivIcon[i],
                                Util.SP.weather().getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.type_two_sunny));
                        tvTemp[i].setText(
                                String.format("%s℃ - %s℃",
                                        weather.dailyForecast.get(i).tmp.min,
                                        weather.dailyForecast.get(i).tmp.max));
                        tvDetail[i].setText(
                                String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                        weather.dailyForecast.get(i).cond.txtD,
                                        weather.dailyForecast.get(i).wind.sc,
                                        weather.dailyForecast.get(i).wind.dir,
                                        weather.dailyForecast.get(i).wind.spd,
                                        weather.dailyForecast.get(i).pop));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






}

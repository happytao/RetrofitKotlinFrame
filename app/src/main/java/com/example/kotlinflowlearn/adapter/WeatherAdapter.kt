package com.example.kotlinflowlearn.adapter

import android.widget.TextView
import androidx.core.view.isInvisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kotlinflowlearn.R
import com.example.kotlinflowlearn.entity.PlusBean
import com.example.kotlinflowlearn.entity.WeatherBean

class WeatherAdapter(layoutResId: Int, data: MutableList<PlusBean>?) :
    BaseQuickAdapter<PlusBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: PlusBean) {
        if(item.temperature != null) {
            holder.setText(R.id.tv_temp_item, item.temperature)
        } else {
            holder.getView<TextView>(R.id.tv_temp_item).isInvisible = false
        }

        if(item.temperature != null) {
            holder.setText(R.id.tv_date_item, item.date)
        } else {
            holder.getView<TextView>(R.id.tv_date_item).isInvisible = false

        }

        if(item.temperature != null) {
            holder.setText(R.id.tv_direc_item, item.direct)
        } else {
            holder.getView<TextView>(R.id.tv_direc_item).isInvisible = false
        }

        if(item.temperature != null) {
            holder.setText(R.id.tv_weather_item, item.weather)
        } else {
            holder.getView<TextView>(R.id.tv_weather_item).isInvisible = false
        }

        holder.setText(R.id.tv_city_item,item.city)
        holder.setText(R.id.tv_country_item,item.district)
        holder.setText(R.id.tv_province_item,item.province)
    }
}

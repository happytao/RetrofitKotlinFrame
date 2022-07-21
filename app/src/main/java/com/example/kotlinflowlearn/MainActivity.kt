package com.example.kotlinflowlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinflowlearn.adapter.WeatherAdapter
import com.example.kotlinflowlearn.databinding.ActivityMainBinding
import com.example.kotlinflowlearn.entity.PlusBean
import com.example.kotlinflowlearn.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy { MainViewModel() }
    private val adapter:WeatherAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        dataBinding.viewModel = mainViewModel
        dataBinding.lifecycleOwner = this
        dataBinding.btnSearch.setOnClickListener {
            val city = dataBinding.etCityInput.text.toString()
            if(city.isNotEmpty()) {
                mainViewModel.getWeatherByCity(city)
            }
        }

        mainViewModel.liveWeatherBean.observe(this) {

        }

        mainViewModel.livePlusBean.observe(this){
            var plusBean = ArrayList<PlusBean>()
            plusBean.addAll(it)
            val myAdapter = WeatherAdapter(R.layout.item_future,plusBean)
            dataBinding.rvFuture.layoutManager = LinearLayoutManager(this)
            dataBinding.rvFuture.adapter = myAdapter

        }
    }
}
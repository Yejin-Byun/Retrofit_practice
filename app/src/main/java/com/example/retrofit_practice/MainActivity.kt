package com.example.retrofit_practice

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrofit_practice.data.DustItem
import com.example.retrofit_practice.retrofit.NetworkClient
import com.example.retrofit_practice.databinding.ActivityMainBinding
import com.skydoves.powerspinner.IconSpinnerAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var items = mutableListOf<DustItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.spinnerViewSido.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            communicateNetWork(setUpDustParameter(text))
        }

        binding.spinnerViewGoo.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->

            Log.d("miseya", "selectedItem: spinnerViewGoo selected >  $text")
            var selectedItem = items.filter { f -> f.stationName == text }
            Log.d("miseya", "selectedItem: sidoName > " + selectedItem[0].sidoName)
            Log.d("miseya", "selectedItem: pm10Value > " + selectedItem[0].pm10Value)

            binding.tvCityname.text = selectedItem[0].sidoName + "  " + selectedItem[0].stationName
            binding.tvDate.text = selectedItem[0].dataTime
            binding.tvP10value.text = selectedItem[0].pm10Value + " ㎍/㎥"

            when (getGrade(selectedItem[0].pm10Value)) {
                1 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#9ED2EC"))
                    binding.ivFace.setImageResource(R.drawable.miso1)
                    binding.tvP10grade.text = "좋음"
                }

                2 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#D6A478"))
                    binding.ivFace.setImageResource(R.drawable.miso2)
                    binding.tvP10grade.text = "보통"
                }

                3 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#DF7766"))
                    binding.ivFace.setImageResource(R.drawable.miso3)
                    binding.tvP10grade.text = "나쁨"
                }

                4 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#BB3320"))
                    binding.ivFace.setImageResource(R.drawable.miso4)
                    binding.tvP10grade.text = "매우나쁨"
                }
            }
        }
    }

    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch() {
        val responseData = NetworkClient.dustNetwork.getDust(param)
        Log.d("Parsing Dust ::", responseData.toString())

        val adapter = IconSpinnerAdapter(binding.spinnerViewGoo)
        items = responseData.response.dustBody.dustItem!!

        val goo = ArrayList<String>()
        items.forEach {
            Log.d("add Item :", it.stationName)
            goo.add(it.stationName)
        }

        runOnUiThread {
            binding.spinnerViewGoo.setItems(goo)
        }

    }

    private fun setUpDustParameter(sido: String): HashMap<String, String> {
        val authKey =
            "z0UoDXj/CpI0GgpHyeIHGc+mARA/RFiTyV6M0URLQyGju1hv60DLIB20PEF0lxbylTmTR9bj2wuXEScl68EDcA=="

        return hashMapOf(
            "serviceKey" to authKey,
            "returnType" to "json",
            "numOfRows" to "100",
            "pageNo" to "1",
            "sidoName" to sido,
            "ver" to "1.0"
        )
    }

    fun getGrade(value: String): Int {
        return try {
            val mValue = value.toInt()
            when {
                mValue in 0..30 -> 1
                mValue in 31..80 -> 2
                mValue in 81..100 -> 3
                else -> 4
            }
        } catch (e: NumberFormatException) {
            1
        }
    }
}
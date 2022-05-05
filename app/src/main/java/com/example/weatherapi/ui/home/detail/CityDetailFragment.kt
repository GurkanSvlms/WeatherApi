package com.example.weatherapi.ui.home.detail


import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapi.R
import com.example.weatherapi.databinding.FragmentCityDetailBinding
import com.example.weatherapi.model.cityResponse.forecast.ConsolidatedWeather
import com.example.weatherapi.ui.adapter.CityWeatherForecastAdapter
import com.example.weatherapi.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityDetailFragment : BaseFragment<FragmentCityDetailBinding,CityDetailViewModel>(
    FragmentCityDetailBinding::inflate
) {
    private val args : CityDetailFragmentArgs by navArgs()
    private val cityWeatherForecastList : ArrayList<ConsolidatedWeather> = arrayListOf()

    override val viewModel by viewModels<CityDetailViewModel>()

    override fun onCreateFinished() {
        viewModel.getCityForecast(args.cityDetail.woeId.toString())
        setRecyclerViewAdapter()
    }

    override fun initListeners() {
    }

    override fun observeEvents() {
        with(viewModel){
            cityResponse.observe(viewLifecycleOwner, Observer {
                it?.let {
                    it.consolidatedWeather?.forEach{ consolidateWeather->
                        cityWeatherForecastList.add(consolidateWeather)
                    }
                }
                (binding.rvWeatherForecast.adapter as CityWeatherForecastAdapter)
                    .updateList(cityWeatherForecastList)

                setWeatherIcon(cityWeatherForecastList[0].weatherStateAbbr.toString())
                binding.tvWeatherStateName.text = cityWeatherForecastList[0].weatherStateName.toString()
                binding.tvWeatherDegree.text = cityWeatherForecastList[0].maxTemp?.toInt().toString()+" °C"
                binding.tvCityTitle.text = args.cityDetail.title.toString()
                binding.tvWeatherDate.text = cityWeatherForecastList[0].applicableDate.toString()
                cityWeatherForecastList.removeAt(0)

            })
            isLoading.observe(viewLifecycleOwner, Observer {
                handleViewActions()
            })

            onError.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun setRecyclerViewAdapter(){
        val mLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvWeatherForecast.layoutManager = mLayoutManager
        binding.rvWeatherForecast.adapter = CityWeatherForecastAdapter()
    }

    private fun handleViewActions(isLoading: Boolean = false) {
        binding.rvWeatherForecast.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun setWeatherIcon(weatherStateAbbr : String){
        when(weatherStateAbbr){
            "c" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_c)
            "h" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_h)
            "hc" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_hc)
            "hr" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_hr)
            "lc" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_lc)
            "lr" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_lr)
            "s" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_s)
            "sl" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_sl)
            "sn" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_sn)
            "t" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_t)
        }
    }
}
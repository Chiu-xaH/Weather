package com.chiuxah.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chiuxah.weather.logic.model.Weather
import com.chiuxah.weather.logic.model.getSky
import com.chiuxah.weather.ui.place.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    val vm by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_main)

        //下翻页融合效果
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        if(vm.jingdu.isEmpty()) vm.jingdu = intent.getStringExtra("经度") ?: ""
        if(vm.weidu.isEmpty()) vm.weidu = intent.getStringExtra("纬度") ?: ""
        if(vm.placename.isEmpty()) vm.placename = intent.getStringExtra("位置") ?: ""

        //Log.d("接收数据测试","${vm.weidu}")
       // Log.d("接收数据测试","${vm.jingdu}")
        //Log.d("接收数据测试","${vm.placename}")

        //与适配器那里的putExtra下相对应，这里是接收端
        if (vm.jingdu.isNotEmpty() && vm.weidu.isNotEmpty()) {
            vm.refreshWeather(vm.jingdu,vm.weidu)
        } else { Toast.makeText(this,"经纬度无效",Toast.LENGTH_SHORT).show() }

        val swipeRefreshLayout : androidx.swiperefreshlayout.widget.SwipeRefreshLayout = findViewById(R.id.sw) //刷新控件
        val dangqian_b : Button = findViewById(R.id.dangqian_b)//定义按钮控件
        val dl : androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.dl)
        //Log.d("测试","断点2")
        //打开滑动菜单，openDrawer
        dangqian_b.setOnClickListener { dl.openDrawer(GravityCompat.START)}

        //监听侧边栏状态
        dl.addDrawerListener(//固定写法，都要重写
            object : DrawerLayout.DrawerListener {

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) { //侧边栏隐藏时要隐藏输入法
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

          }
        )



        vm.weatherLiveData.observe(this,Observer { result ->
            //Log.d("测试","断点C")
            val weather = result.getOrNull()
           // Log.d("测试","断点A")
            if (weather != null) {
              //  Log.d("测试","断点B")
                ssshow(weather)
               // Log.d("测试","断点4")
            }
                else {
                  //  Log.d("测试","断点5")

                    Toast.makeText(this,"无法获取信息",Toast.LENGTH_SHORT).show()
                   // Log.d("测试","断点S")
                    result.exceptionOrNull()?.printStackTrace()
                   // Log.d("测试","断点Q")
               // Log.d("测试","${result.exceptionOrNull()?.printStackTrace()}")
            }
            swipeRefreshLayout.isRefreshing = false
        })
       // Log.d("测试","断点F")

        swipeRefreshLayout.setColorSchemeColors(R.color.purple_700)
        Refresh()
        swipeRefreshLayout.setOnRefreshListener{
            Refresh()
            Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show()
        }//刷新操作对应控件


        vm.refreshWeather(vm.jingdu,vm.weidu)
                }
//刷新定义操作
     fun Refresh() {
        vm.refreshWeather(vm.jingdu,vm.weidu)
        val swipeRefreshLayout : androidx.swiperefreshlayout.widget.SwipeRefreshLayout = findViewById(R.id.sw) //刷新控件
        swipeRefreshLayout.isRefreshing = true
    }


    private fun ssshow(weather: Weather?) {
        //把数据获取并显示到相应控件上,啊啊这么多数据啊

        //加载控件id---dangqian.xml

        val placeName : TextView = findViewById(R.id.placeName)
        val currentTemp : TextView = findViewById(R.id.currentTemp)
        val currentSky : TextView = findViewById(R.id.currentSky)
        val currentAQI : TextView = findViewById(R.id.currentAQI)
        val nowLayout : RelativeLayout = findViewById(R.id.nowLayout)//背景

        //填充当前天气界面---dangqian.xml
        placeName.text = vm.placename
        weather?.realtime?.skycon?.let { getSky(it).bg }
            ?.let { nowLayout.setBackgroundResource(it) }
        currentTemp.text = "${weather?.realtime?.temperature?.toInt()} ℃"
        currentSky.text = weather?.realtime?.skycon?.let { getSky(it).info }//这个是啥啊，不懂
        currentAQI.text = "空气指数 ${weather?.realtime?.airQuality?.aqi?.chn?.toInt()}"
        //一连串都在数据模型里定义好了
        //在数据模型里Sky类有三个参数，info,bg,icon

        //加载控件id---yubao.xml
        val forecastLayout : LinearLayout = findViewById(R.id.forecastLayout)


        //填充预报界面---yubao.xml和yubao_item.xml
        forecastLayout.removeAllViews()
        for (i in 0 until weather?.daily?.skycon?.size!!) {
            //加载控件yubao_item.xml
            val view = LayoutInflater.from(this).inflate(R.layout.yubao_item,forecastLayout,false)
            val dateInfo: TextView? = view.findViewById(R.id.dateInfo) as  TextView?
            val skyIcon : ImageView? = findViewById(R.id.skyIcon) as ImageView?
            val skyInfo : TextView? = findViewById(R.id.skyInfo) as TextView?
            val temparetureInfo : TextView? = findViewById(R.id.temperatureInfo)

            //Log.d("测试","断点循环${i}")

            dateInfo?.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(weather?.daily?.skycon[i].date)//固定写法呗，我又大开眼界了🐎的

            skyIcon?.setImageResource(getSky(weather?.daily?.skycon[i].value).icon)
            skyInfo?.text = getSky(weather?.daily?.skycon[i].value).info
            temparetureInfo?.text = "${weather?.daily?.temperature!![i]?.min} ℃ ~ ${weather?.daily?.temperature!![i]?.max} ℃"
            forecastLayout.addView(view)
        }

        //加载控件id---shenghuozhishu.xml
        //val coldRiskImg : ImageView = findViewById(R.id.coldRiskImg)
        val coldRiskText : TextView = findViewById(R.id.coldRiskText)
        //val dressingImg : ImageView = findViewById(R.id.dressingImg)
        val dressingText : TextView = findViewById(R.id.dressingText)
        //val ultravioletImg : ImageView = findViewById(R.id.ultravioletImg)
        val ultravioletText : TextView = findViewById(R.id.ultravioletText)
        //val carWashingImg : ImageView = findViewById(R.id.carWashingImg)
        val carWashingText : TextView = findViewById(R.id.carWashingText)

        //加载控件id---weather_main.xml
        val weatherLayout : ScrollView = findViewById(R.id.weatherLayout)
        //Log.d("测试","断点12")
        //填充生活主页界面---shenghuozhishu.xml
        carWashingText.text = "${weather?.daily?.lifeIndex?.carWashing!![0].desc}"
        ultravioletText.text = "${weather?.daily?.lifeIndex?.ultraviolet!![0].desc}"
        dressingText.text = "${weather?.daily?.lifeIndex?.dressing!![0].desc}"
        coldRiskText.text = "${weather?.daily?.lifeIndex?.coldRisk!![0].desc}"

        weatherLayout.visibility = View.VISIBLE
    }
            }

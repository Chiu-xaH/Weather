package com.chiuxah.weather.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chiuxah.weather.R
import com.chiuxah.weather.WeatherActivity
import com.chiuxah.weather.logic.model.Place
//标准适配器其写法
class MyAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.NAME)
        val placeAddress: TextView = view.findViewById(R.id.ADDRESS)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,
            parent, false)


        //if (fragment.vm.isPlaceSaved()) {
          //  val intent = Intent(context, WeatherActivity::class.java).apply {
              //  putExtra("经度", "117")
              //  putExtra("纬度", "30")
              //  putExtra("位置", "宣城")//记得修改！！
           // }
          //  startActivity(intent)
          // fragment.activity?.finish()
          //  return
      //  }
        //列表点击事件
        val holder = ViewHolder(view)//固定写法吧也许，我都忘了。。。。
        holder.itemView.setOnClickListener {
            val dl : androidx.drawerlayout.widget.DrawerLayout? = view.findViewById(R.id.dl)
            //定义
            val position : Int = holder.adapterPosition
            val place = placeList[position]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.apply {
                    dl?.closeDrawers()
                    vm.jingdu = place.location.jingdu
                    vm.weidu = place.location.weidu
                    vm.placename = place.name
                    Refresh()//在WeatherActivity里的刷新操作
                }
            } else {

                val it = Intent(parent.context,WeatherActivity::class.java).apply {
                    putExtra("经度",place.location.jingdu)//发送数据到WA//目前有bug
                    putExtra("纬度",place.location.weidu)
                    putExtra("位置",place.name)
                }
                //Log.d("发送数据测试","${place.location.weidu}")
                // Log.d("发送数据测试","${place.location.jingdu}")
                // Log.d("发送数据测试","${place.name}")

                //  fragment.vm.savePlace(place)
                fragment.startActivity(it)
                fragment.activity?.finish()
            }


        }

        return holder//数组越界问题修改
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
    override fun getItemCount() = placeList.size
}
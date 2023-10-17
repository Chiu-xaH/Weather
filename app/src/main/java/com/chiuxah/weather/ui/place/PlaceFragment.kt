package com.chiuxah.weather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiuxah.weather.R

class PlaceFragment : androidx.fragment.app.Fragment() {
    private lateinit var adapter: MyAdapter
    val vm by lazy { ViewModelProvider(this).get(PaceViewModel::class.java) }
    var viewq: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewq = inflater?.inflate(R.layout.place, container, false)
        return viewq
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


            val rv: RecyclerView? = viewq?.findViewById(R.id.rv)
            val et: EditText? = viewq?.findViewById(R.id.et)
            val iv: ImageView? = viewq?.findViewById(R.id.iv)

            val lm = LinearLayoutManager(activity)
            rv?.layoutManager = lm

            adapter = MyAdapter(this, vm.list)
            rv?.adapter = adapter

            et?.addTextChangedListener { editable ->  //新方法,监听搜索框控件内容变化，每当变化，就执行方法
                val content = editable.toString()
                if (content != null) {
                    vm.getQuery(content)//content是内容
                } else {
                    rv?.visibility = View.GONE//隐藏rv
                    //Log.d("测试","断点2")
                    iv?.visibility = View.VISIBLE//显示iv
                    // Log.d("测试","断点3")
                    vm.list.clear()//清除列表
                    // Log.d("测试","断点4")
                    adapter.notifyDataSetChanged()//固定写法通知列表刷新
                }
            }
            vm.q.observe(viewLifecycleOwner, Observer { result ->
                val places = result.getOrNull()
                // Log.d("测试","断点5")
                if (places != null) {
                    rv?.visibility = View.VISIBLE
                    iv?.visibility = View.GONE
                    // Log.d("测试","断点6")
                    vm.list.clear()
                    vm.list.addAll(places)
                    //  Log.d("测试","断点7")
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "查询失败", Toast.LENGTH_SHORT).show()
                    // Log.d("测试","断点8")
                    result.exceptionOrNull()?.printStackTrace()
                }
            })
        }


    }

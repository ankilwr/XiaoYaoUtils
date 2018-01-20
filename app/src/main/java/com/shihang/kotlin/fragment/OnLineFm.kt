package com.shihang.kotlin.fragment

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.shihang.kotlin.R
import com.shihang.kotlin.adapter.OnLineFmAdapter
import com.shihang.kotlin.bean.OnLineBean
import com.shihang.kotlin.config.DefaultItemDecoration
import com.shihang.kotlin.extends.showFailureTost
import com.shihang.kotlin.http.HttpCallBack
import com.shihang.kotlin.utils.CommonUtil
import com.shihang.kotlin.utils.JsonUtil
import com.shihang.kotlin.view.HomeTabButton
import com.shihang.kotlin.view.HomeTabGroup.OnCheckedChangeListener
import com.shihang.pulltorefresh.PullRecyclerView.LoadListener
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.fm_online.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class OnLineFm : TitleFm(), OnCheckedChangeListener {

    override val layout: Int = R.layout.fm_online

    private var adapter: OnLineFmAdapter? = null
    private var page: Int? = null


    override fun onSelect(button: HomeTabButton, index: Int) {}
    override fun onUnSelect(button: HomeTabButton, index: Int) {}
    override fun onReSelect(button: HomeTabButton, index: Int) {}


    override fun initViews() {
        super.initViews()
        showTitle(true, R.string.title_online)
        showRightImage(true, R.drawable.title_pressed_search)
        pullView.setPullEnable(true, true)
        pullView.setPullListener(loadListener)
        adapter = OnLineFmAdapter(context)
        pullView.swipeRecyclerView.layoutManager = LinearLayoutManager(context)

        //DefaultItemDecoration(context) //默认线条，更多属性查看DefaultItemDecoration类
        //设置一个带有"30px" 左右边距的item线条
        pullView.swipeRecyclerView.addItemDecoration(DefaultItemDecoration(context, padding = AutoUtils.getPercentHeightSizeBigger(30)))

        //设置侧滑按钮 跟 侧滑按钮的点击监听(如果需要侧滑一定要在setAdapter之前创建好构建器)
        pullView.swipeRecyclerView.setSwipeMenuCreator(menuCreator)
        pullView.swipeRecyclerView.setSwipeMenuItemClickListener(menuClick)
        //设置adapter+空布局
        pullView.setAdapter(adapter, CommonUtil.getEmptyView(context, "暂时没有数据"))

        pullView.pullRefreshing()
    }


    private val menuCreator = SwipeMenuCreator { swipeLeftMenu, swipeRightMenu, viewType ->
        //创建侧滑按钮
        val menuButton = SwipeMenuItem(context)
        menuButton.setBackground(R.color.themeColor)
        menuButton.setTextColor(Color.WHITE)
        menuButton.textSize = 18
        menuButton.text = "删除"
        menuButton.height = ViewGroup.LayoutParams.MATCH_PARENT
        menuButton.width = AutoUtils.getPercentWidthSizeBigger(180)
        //swipeRightMenu.addMenuItem()右边侧滑按钮
        //swipeLeftMenu.addMenuItem()左边侧滑按钮
        swipeRightMenu.addMenuItem(menuButton)
    }

    //侧换按钮的点击事件
    private val menuClick = SwipeMenuItemClickListener { menuBridge ->
        menuBridge.closeMenu()
        adapter?.remove(menuBridge.adapterPosition)
    }


    private fun loadData(isRefresh: Boolean, pageNum: Int) {
        sendHttpRequest(pageNum, object : HttpCallBack {
            override fun result(success: Boolean, result: String) {
                val bean = JsonUtil.getBean(result, OnLineBean::class.java)
                if (!success || bean == null || !bean.httpCheck()) {
                    if (isRefresh) context.showFailureTost(result, bean, null)
                    pullView.loadError(isRefresh)
                    return
                }
                if (isRefresh) adapter?.resetNotify(bean.data) else adapter?.addNotify(bean.data)
                if (bean.hasNext()) page = pageNum + 1
                pullView.loadFinish(isRefresh, bean.hasNext())
            }
        })
    }


    private val loadListener: LoadListener = object : LoadListener {
        override fun onRefresh() {
            loadData(true, 1)
        }

        override fun onLoadMore() {
            loadData(false, page ?: 1)
        }
    }
























//------------------------------------- 以下数据该由服务端返回----------------------------------------------


    //模拟发送请求获取数据(随机返回成功或者失败)
    private fun sendHttpRequest(page: Int, callBack: HttpCallBack) {
        doAsync {
            val pageSize = 15
            val total = 50
            val objNumber = (page - 1) * pageSize
            val obj = JsonObject()
            // 设置为25%的几率会返回操作失败
            if (Random().nextInt(4) % 4 == 0) {
                obj.addProperty("status", "0")
                obj.addProperty("message", "操作失败")
            } else {
                obj.addProperty("status", "1")
                obj.addProperty("message", "操作成功")
                val array = JsonArray()
                val getNum = if(objNumber + pageSize > total) total else objNumber + pageSize
                (objNumber until getNum).map { array.add("测试数据${it + 1}") }
                obj.add("data", array)
                val pageObj = JsonObject()
                pageObj.addProperty("total", total)
                pageObj.addProperty("page", page)
                pageObj.addProperty("pageSize", pageSize)
                obj.add("page", pageObj)
            }
            Thread.sleep(1000)
            uiThread {
                callBack.result(true, obj.toString())
            }
        }
    }


}

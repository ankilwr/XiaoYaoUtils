package com.shihang.kotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shihang.kotlin.bean.BaseBean
import com.shihang.kotlin.extends.showFailureTost
import com.shihang.kotlin.http.HttpManager
import com.shihang.kotlin.ui.BaseUI


abstract class BaseFm : Fragment() {

    lateinit var context: BaseUI
    abstract val layout: Int
    abstract fun initViews()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context = activity as BaseUI
        return inflater!!.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        HttpManager.cancelHttpForTag(this)
        super.onDestroyView()
    }

    fun <T: View> getView(viewId: Int): T?{
        return view?.findViewById(viewId)
    }


    fun showFailureToast(result: String, bean: BaseBean?, def: String?) {
        context.showFailureTost(result, bean, def)
    }

    fun getStr(strId: Int): String {
        return context.resources.getString(strId)
    }

    fun getColor(color: Int): Int {
        return context.resources.getColor(color)
    }


}

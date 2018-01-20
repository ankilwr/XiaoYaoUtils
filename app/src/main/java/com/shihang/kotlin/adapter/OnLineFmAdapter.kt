package com.shihang.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shihang.kotlin.R

class OnLineFmAdapter(val context: Context) : BaseRecyclerAdapter<String>(context, null) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {
        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_online, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.setText(R.id.tvMessage, datas!![position])
    }

}

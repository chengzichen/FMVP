package com.dhc.mvp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import com.dhc.library.base.BaseActivity
import com.dhc.mvp.net.NetSampleActivity
import com.dhc.mvp.net.RoomSampleActivity
import com.dhc.mvp.net.RxCacheSampleActivity
import com.dhc.mvp.net.RxJavaSampleActivity

/**
 * @creator： denghc(desoce)
 * @updateTime：
 * @description： home
 */
class MainActivity : BaseActivity() {

    private var recyclerView: RecyclerView? = null
    private val menu = arrayOf("net Sample", "room Sample", "rxcache Sample", "protobuf Sample")

    override val layoutId: Int
        get() = R.layout.activity_main


    internal var adapter: RecyclerView.Adapter<*> = object : RecyclerView.Adapter<BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, null, false)

            return BaseViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

            holder.itemView.setOnClickListener {
                if (onItemClickListener != null)
                    onItemClickListener!!.itemClick(position, menu[position])
            }
            holder.onBind(menu[position])
        }

        override fun getItemCount(): Int {
            return menu.size
        }

    }
    private var onItemClickListener: OnItemClickListener? = null

    override fun initEventAndData(savedInstanceState: Bundle?) {
        initView()
        initData()
    }

    private fun initView() {
        recyclerView = `$`(R.id.rv_menu)
    }

    private fun initData() {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        onItemClickListener = object : OnItemClickListener {
            override fun itemClick(pos: Int, title: String) {
                router(pos, title)
            }
        }
        recyclerView!!.adapter = adapter
    }


    private fun router(pos: Int, title: String) {

        when (pos) {
            0 -> startActivity(Intent(this, NetSampleActivity::class.java))
            1 -> startActivity(Intent(this, RoomSampleActivity::class.java))
            2 -> startActivity(Intent(this, RxCacheSampleActivity::class.java))
            3 -> startActivity(Intent(this, RxJavaSampleActivity::class.java))
        }

    }


    interface OnItemClickListener {
        fun itemClick(pos: Int, title: String)
    }

    internal inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView

        init {
            itemView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tvTitle = itemView.findViewById(R.id.tv_title)
        }

        fun onBind(title: String) {
            tvTitle.text = title
        }

    }


}

package cn.zhaoshuhao.kotlinfirst

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTabHost
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.fragment.ARoundFragment
import cn.zhaoshuhao.kotlinfirst.fragment.MainFragment
import cn.zhaoshuhao.kotlinfirst.fragment.MineFragment
import cn.zhaoshuhao.kotlinfirst.fragment.MoreFragment

class MainActivity : AppCompatActivity() {

    val mTabText = arrayOf("主页", "周边", "我的", "更多")
    val mTabIcon = arrayOf(R.drawable.ic_tab_artists, R.drawable.ic_tab_albums,
            R.drawable.ic_tab_songs, R.drawable.ic_tab_playlists)
    val mTabTarget = arrayOf(MainFragment::class.java, ARoundFragment::class.java,
            MineFragment::class.java, MoreFragment::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabhost: FragmentTabHost = findViewById(android.R.id.tabhost) as FragmentTabHost
        tabhost.setup(this, supportFragmentManager, android.R.id.tabcontent)
        mTabText.forEach {
            val index = mTabText.indexOf(it)
            val inflate = layoutInflater.inflate(R.layout.tab_main, null, false)
            inflate.findViewById<ImageView>(R.id.id_iv_tab_icon).setImageResource(mTabIcon[index])
            inflate.findViewById<TextView>(R.id.id_tv_tab_text).text = it
            tabhost.addTab(tabhost.newTabSpec(it).setIndicator(inflate), mTabTarget[index], null)
        }
    }
}

fun <T> Activity.startActivity(other: Class<T>) {
    val target = Intent(this, other)
    startActivity(target)
    //设置进入和退出动画
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    finish()
}

inline fun <reified T : Context> Activity.startActivity() {
    startActivity(T::class.java)
}


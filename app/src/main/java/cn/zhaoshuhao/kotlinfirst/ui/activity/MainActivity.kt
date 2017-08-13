package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.fragment.*
import cn.zhaoshuhao.kotlinfirst.utils.LogType
import cn.zhaoshuhao.kotlinfirst.utils.componet
import cn.zhaoshuhao.kotlinfirst.utils.log
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity(), CheckoutToolbar {
    val mTabText = arrayOf("主页", "周边", "购物车", "我的")

    val mTabIcon = arrayOf(R.drawable.ic_tab_artists, R.drawable.ic_tab_albums,
            R.drawable.ic_tab_cart, R.drawable.ic_tab_songs)

    val mTabTarget = arrayOf(MainFragment(), ARoundFragment(),
            MineFragment(), MoreFragment())

    val logd = (::log.componet())(LogType.DEBUG)(MainActivity::class.java.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setDefaultFragment();
    }

    private fun initViews() {
        initToolbar()
        initDrawer()
        initBottomNavigationBar()
    }

    private fun initToolbar() = with(id_main_toolbar as Toolbar) {
        setSupportActionBar(this)
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    private fun initDrawer() = with(id_main_drawer) {
        val toggle = ActionBarDrawerToggle(this@MainActivity, this, id_main_toolbar as Toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle.syncState()
        addDrawerListener(toggle)
    }

    var mCurIndex = 0

    private fun initBottomNavigationBar() = with(id_main_bottom_navigation) {
        setMode(BottomNavigationBar.MODE_SHIFTING)
        setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        for (i in 0..3) {
            addItem(BottomNavigationItem(mTabIcon[i], mTabText[i]))
        }
        initialise()
        setFirstSelectedPosition(0)
//            selectTab(0)
        setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {}

            override fun onTabUnselected(position: Int) {}
            override fun onTabSelected(position: Int) {
                if (mCurIndex == position) return
                supportFragmentManager.beginTransaction().replace(R.id.id_main_fragment_content, mTabTarget[position]).commit()
                logd(position.toString())
                mCurIndex = position
            }

        })
    }

    private fun setDefaultFragment() {
        mCurIndex = 0
        supportFragmentManager.beginTransaction().replace(R.id.id_main_fragment_content, mTabTarget[0]).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (id_main_drawer.isDrawerOpen(Gravity.START)) {
            id_main_drawer.closeDrawer(Gravity.START)
            return
        }
        super.onBackPressed()
    }

    override fun toTarget(fragment: Fragment) {
        when (fragment) {
            is MainFragment -> title = mTabText[0]
            is ARoundFragment -> title = mTabText[1]
            is MineFragment -> title = mTabText[2]
            is MoreFragment -> title = mTabText[3]
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



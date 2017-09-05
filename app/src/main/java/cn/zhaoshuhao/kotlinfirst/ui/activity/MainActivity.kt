package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.startActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.contract.MainPresent
import cn.zhaoshuhao.kotlinfirst.fragment.*
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : BaseActivity(), CheckoutToolbar {
    private val mTabText = arrayOf("主页", "周边", "已购", "我的")

    private val mTabIcon = arrayOf(R.drawable.ic_tab_artists, R.drawable.ic_tab_albums,
            R.drawable.ic_tab_cart, R.drawable.ic_tab_songs)

    private val mTabTarget by lazy {
        val mainFragment = MainFragment()
        val mainPresent = MainPresent(this)
        mainFragment.setPresent(mainPresent)
        mainPresent.setView(mainFragment)
        arrayOf(mainFragment, ARoundFragment(),
                MineFragment(), MoreFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KPermission.requestOfLambda(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                activity = this) { g, d, n ->
            /*针对权限做不同处理*/
        }
        mTabTarget
        initFragmentUI();
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        /*做数据保存并在onCreate、onResotreInstanceState中恢复*/
        /*修复Fragment切换被replace回收的bug*/
    }


    override fun obtainLayoutID(): Int = R.layout.activity_main

    override fun initViews() {
        initBottomNavigationBar()
    }

    override fun initToolbar(): Unit = with(id_toolbar as Toolbar) {
        setSupportActionBar(this)
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.id_main_location -> {
                    toast("定位")
                    true
                }
                R.id.id_main_search -> {
                    false
                }
                R.id.id_main_scan -> {
                    KPermission.requestOfLambda(Manifest.permission.CAMERA, activity = this@MainActivity) { g, d, n ->
                        when {
                            g.size != 0 -> {
                                startActivity<ScanActivity>()
                                logd("授权成功")
                            }
                            d.size != 0 -> showTipDialog()
                            n.size != 0 -> gotoSettings()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        initDrawer()
    }

    private fun initDrawer() = with(id_main_drawer as DrawerLayout) {
        val toggle = ActionBarDrawerToggle(this@MainActivity, this, id_toolbar as Toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle.syncState()
        addDrawerListener(toggle)
        initNavigationView()
    }

    private fun initNavigationView() = with(id_main_navigation as NavigationView) {
        setNavigationItemSelectedListener {
            toast("${it.itemId}");
            id_main_drawer.closeDrawer(Gravity.START)
            true
        }
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

    private fun initFragmentUI() {
        mCurIndex = 0
        supportFragmentManager.beginTransaction()
                .replace(R.id.id_main_fragment_content, mTabTarget[0]).commit()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (mCurrentPage) {
            0 -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = true
                menu?.findItem(R.id.id_main_scan)?.isVisible = true
                menu?.findItem(R.id.id_main_location)?.isVisible = true
            }
            else -> {
                menu?.findItem(R.id.id_main_search)?.isVisible = false
                menu?.findItem(R.id.id_main_scan)?.isVisible = false
                menu?.findItem(R.id.id_main_location)?.isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun obtainMenuRes(): Int {
        return R.menu.main_activity_toolbar
    }

    override fun initMenuAction(menu: Menu?) {
        val item = menu?.findItem(R.id.id_main_search)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.isIconified = true
        searchView.queryHint = "吃喝玩乐"
        /*获取组件中控件的id，并通过id获得控件对象，修改其属性*/
        val searchTv: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
        searchTv.setTextColor(Color.WHITE)
        searchTv.setHintTextColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                logd("submit ${query ?: "empty"}"); return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                logd("text change : ${newText ?: "empty"}"); return false
            }
        })
    }

    override fun onBackPressed() {
        if (id_main_drawer.isDrawerOpen(Gravity.START)) {
            id_main_drawer.closeDrawer(Gravity.START)
            return
        }
        finish()
        super.onBackPressed()
    }

    private var mCurrentPage: Int = 0

    override fun toTarget(fragment: Fragment) {
        when (fragment) {
            is MainFragment -> mCurrentPage = 0
            is ARoundFragment -> mCurrentPage = 1
            is MineFragment -> mCurrentPage = 2
            is MoreFragment -> mCurrentPage = 3

        }
        title = mTabText[mCurrentPage]
        invalidateOptionsMenu()
    }

    /**
     * 引导用户到设置界面打开相机权限
     * */
    private fun gotoSettings() {

    }

    /**
     * 提示并引导用户相机权限的重要性
     * */
    private fun showTipDialog() {

    }
}



